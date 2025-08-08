// 代码生成时间: 2025-08-08 12:12:08
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.util.ArrayList;
import java.util.List;

/**
 * A mathematical toolbox using Apache Spark to perform calculations.
 * This class provides a set of mathematical operations that can be performed on a dataset.
 */
public class MathToolbox {

    private SparkSession spark;

    public MathToolbox(SparkSession spark) {
        this.spark = spark;
    }

    /**
     * Adds a column to the dataset which is the square of the specified column.
     * @param dataset The dataset on which to perform the operation.
     * @param columnName The name of the column to square.
     * @return A new dataset with the squared column added.
     */
    public Dataset<Row> squareColumn(Dataset<Row> dataset, String columnName) {
        try {
            return dataset.withColumn("squareOf" + columnName, dataset.col(columnName).multiply(dataset.col(columnName)));
        } catch (Exception e) {
            throw new RuntimeException("Error squaring column: " + columnName, e);
        }
    }

    /**
     * Adds a column to the dataset which is the sum of two specified columns.
     * @param dataset The dataset on which to perform the operation.
     * @param columnName1 The name of the first column to add.
     * @param columnName2 The name of the second column to add.
     * @return A new dataset with the sum column added.
     */
    public Dataset<Row> addColumns(Dataset<Row> dataset, String columnName1, String columnName2) {
        try {
            return dataset.withColumn("sumOf" + columnName1 + columnName2, dataset.col(columnName1).plus(dataset.col(columnName2)));
        } catch (Exception e) {
            throw new RuntimeException("Error adding columns: " + columnName1 + " and " + columnName2, e);
        }
    }

    /**
     * Calculates the mean of a specified column in the dataset.
     * @param dataset The dataset from which to calculate the mean.
     * @param columnName The name of the column to calculate the mean of.
     * @return The mean of the specified column as a Double value.
     */
    public Double meanColumn(Dataset<Row> dataset, String columnName) {
        try {
            return dataset.agg(dataset.col(columnName).avg()).head().getAs("avg(" + columnName + ")");
        } catch (Exception e){
            throw new RuntimeException("Error calculating mean of column: " + columnName, e);
        }
    }

    /**
     * Main method to run the MathToolbox with sample data.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("MathToolbox")
                .master("local[*]")
                .getOrCreate();

        MathToolbox toolbox = new MathToolbox(spark);

        // Sample data
        List<Row> rows = new ArrayList<>();
        rows.add(RowFactory.create(1, 2));
        rows.add(RowFactory.create(3, 4));
        rows.add(RowFactory.create(5, 6));

        Dataset<Row> data = spark.createDataFrame(rows, new StructType(new StructField[]{
                new StructField("col1", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("col2", DataTypes.IntegerType, false, Metadata.empty())
        }));

        // Perform operations
        Dataset<Row> squared = toolbox.squareColumn(data, "col1");
        Dataset<Row> summed = toolbox.addColumns(data, "col1", "col2");
        Double mean = toolbox.meanColumn(data, "col1");

        // Show results
        squared.show();
        summed.show();
        System.out.println("Mean of col1: " + mean);

        // Stop the Spark context
        spark.stop();
    }
}
