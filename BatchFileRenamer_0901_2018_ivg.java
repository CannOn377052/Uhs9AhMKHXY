// 代码生成时间: 2025-09-01 20:18:34
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BatchFileRenamer {

    public static void main(String[] args) {
        // Check if the required arguments are provided
        if (args.length < 2) {
            System.err.println("Usage: BatchFileRenamer <sourceDirectory> <targetDirectory>");
            System.exit(1);
        }

        String sourceDirectory = args[0];
        String targetDirectory = args[1];

        // Set up Spark configuration
        SparkConf conf = new SparkConf().setAppName("BatchFileRenamer");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Get all files in the source directory as an RDD
            JavaRDD<String> fileList = sc.textFile(sourceDirectory)
                .flatMap(line -> {
                    try {
                        List<String> files = new ArrayList<>();
                        Files.walk(Paths.get(line)).forEach(path -> {
                            if (Files.isRegularFile(path)) {
                                files.add(path.toString());
                            }
                        });
                        return files.iterator();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            // Rename files
            fileList.foreach(file -> {
                try {
                    // Construct the new file name
                    String fileName = new File(file).getName();
                    String newFileName = "renamed_" + fileName;
                    String newFilePath = targetDirectory + "/" + newFileName;

                    // Check if file already exists
                    File targetFile = new File(newFilePath);
                    if (targetFile.exists()) {
                        System.err.println("File already exists: " + newFilePath);
                        return;
                    }

                    // Rename the file
                    Files.move(Paths.get(file), Paths.get(newFilePath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("File renaming completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
