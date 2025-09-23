// 代码生成时间: 2025-09-23 18:03:26
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackupSyncTool {

    private JavaSparkContext sc;
    private String sourcePath;
    private String backupPath;

    // Constructor to initialize the Spark context and the paths
    public FileBackupSyncTool(JavaSparkContext sc, String sourcePath, String backupPath) {
        this.sc = sc;
        this.sourcePath = sourcePath;
        this.backupPath = backupPath;
    }

    // Method to list files from the source directory
    private List<String> listFilesInSource() throws IOException {
        return Files.walk(Paths.get(sourcePath))
                .filter(Files::isRegularFile)
                .map(path -> path.toAbsolutePath().toString())
                .collect(Collectors.toList());
    }

    // Method to copy files from source to backup directory
    private void copyFileToBackup(String filePath) {
        try {
            Files.copy(Paths.get(filePath), Paths.get(backupPath, Paths.get(filePath).getFileName().toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error copying file: " + filePath + " - " + e.getMessage());
        }
    }

    // Method to sync files from source to backup directory
    public void syncFiles() {
        try {
            List<String> files = listFilesInSource();

            // Convert the list of files to an RDD
            JavaRDD<String> fileRDD = sc.parallelize(files);

            // Perform the file copy operation
            fileRDD.foreach(file -> copyFileToBackup(file));

            sc.stop();
        } catch (IOException e) {
            System.err.println("Error listing files: " + e.getMessage());
        }
    }

    // Main method to run the backup and sync tool
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: FileBackupSyncTool <sourcePath> <backupPath> <masterUrl>");
            return;
        }

        String sourcePath = args[0];
        String backupPath = args[1];
        String masterUrl = args[2];

        // Initialize the Spark context
        JavaSparkContext sc = new JavaSparkContext(masterUrl, "FileBackupSyncTool");

        // Create an instance of the FileBackupSyncTool
        FileBackupSyncTool tool = new FileBackupSyncTool(sc, sourcePath, backupPath);

        // Sync the files from source to backup
        tool.syncFiles();
    }
}
