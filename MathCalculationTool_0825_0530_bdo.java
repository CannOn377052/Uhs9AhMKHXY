// 代码生成时间: 2025-08-25 05:30:51
package com.example.mathtool;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathCalculationTool {

    private SparkSession spark;

    /**
     * Constructor to initialize a Spark session.
     */
    public MathCalculationTool() {
        SparkConf conf = new SparkConf().setAppName("MathCalculationTool").setMaster("local[*]");
        this.spark = SparkSession.builder().config(conf).getOrCreate();
    }

    /**
     * Calculate the sum of all elements in the dataset.
     *
     * @param data The dataset containing numerical values.
     * @return The sum of all elements.
     */
    public double calculateSum(Dataset<Row> data) {
        return data.agg("value").first().getDouble(0);
    }

    /**
     * Calculate the average of all elements in the dataset.
     *
     * @param data The dataset containing numerical values.
     * @return The average of all elements.
     */
    public double calculateAverage(Dataset<Row> data) {
        long count = data.count();
        if (count == 0) {
            throw new IllegalArgumentException("Cannot calculate average of an empty dataset.");
        }
        return calculateSum(data) / count;
    }

    /**
     * Calculate the variance of all elements in the dataset.
     *
     * @param data The dataset containing numerical values.
     * @return The variance of all elements.
     */
    public double calculateVariance(Dataset<Row> data) {
        double sum = calculateSum(data);
        double mean = calculateAverage(data);
        return data.selectExpr("(value - mean) * (value - mean)")
                 .agg("value").first().getDouble(0) / data.count();
    }

    /**
     * Calculate the standard deviation of all elements in the dataset.
     *
     * @param data The dataset containing numerical values.
     * @return The standard deviation of all elements.
     */
    public double calculateStandardDeviation(Dataset<Row> data) {
        return Math.sqrt(calculateVariance(data));
    }

    /**
     * Main method to test the MathCalculationTool class.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        MathCalculationTool tool = new MathCalculationTool();
        JavaSparkContext sc = new JavaSparkContext(tool.spark.sparkContext());
        JavaRDD<Integer> numbers = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
        Dataset<Row> data = tool.spark.createDataFrame(numbers, Integer.class).toDF("value");

        try {
            System.out.println("Sum: " + tool.calculateSum(data));
            System.out.println("Average: " + tool.calculateAverage(data));
            System.out.println("Variance: " + tool.calculateVariance(data));
            System.out.println("Standard Deviation: " + tool.calculateStandardDeviation(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
            tool.spark.stop();
        }
    }
}
