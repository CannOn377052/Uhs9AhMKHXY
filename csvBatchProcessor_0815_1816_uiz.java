// 代码生成时间: 2025-08-15 18:16:57
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVBatchProcessor {

    private SparkSession sparkSession;

    public CSVBatchProcessor(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    /**
     * Process a list of CSV files and perform operations.
     * @param inputFiles List of CSV file paths to be processed.
     * @param outputDirectory The directory where processed files will be saved.
     * @throws IOException If there is an error reading or writing files.
     */

    public void processCSVFiles(List<String> inputFiles, String outputDirectory) throws IOException {
        try {
            // Read CSV files into DataFrames
            for (String filePath : inputFiles) {
                Dataset<Row> df = sparkSession
                        .read()
                        .option("header", "true")
                        .option("inferSchema", "true")
                        .csv(filePath);

                // Perform transformations here
                // For demonstration, let's just rename a column
                df = df.withColumnRenamed("oldColumnName", "newColumnName");

                // Write the processed DataFrame to the output directory
                df.write()
                        .option("header", "true")
                        .csv(outputDirectory + "/" + filePath);
            }
        } catch (Exception e) {
            // Log and rethrow the exception
            System.err.println("Error processing CSV files: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("CSV Batch Processor")
                .master("local[*]")
                .getOrCreate();

        CSVBatchProcessor processor = new CSVBatchProcessor(spark);

        // Example usage: replace with actual file paths and output directory
        List<String> csvFiles = Arrays.asList("path/to/file1.csv", "path/to/file2.csv");
        String outputDir = "path/to/output";
        try {
            processor.processCSVFiles(csvFiles, outputDir);
        } catch (IOException e) {
            System.err.println("Failed to process CSV files: " + e.getMessage());
        }

        spark.stop();
    }
}
