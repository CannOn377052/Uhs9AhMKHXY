// 代码生成时间: 2025-08-13 07:53:20
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A utility class for decompressing gzip compressed tar archives.
 */
public class FileDecompressor {

    /**
     * Decompresses a gzip compressed tar archive into the specified directory.
     * 
     * @param sourcePath the path to the compressed file
     * @param destinationPath the path to the directory where files will be decompressed
     * @throws IOException if an I/O error occurs
     */
    public void decompressGzipTar(String sourcePath, String destinationPath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new IOException("Source file does not exist: " + sourcePath);
        }

        try (TarArchiveInputStream tarInput = new TarArchiveInputStream(
                new GzipCompressorInputStream(new FileInputStream(sourcePath)))) {

            // Iterate over the entries in the tar
            TarArchiveEntry entry;
            while ((entry = tarInput.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue; // Skip directories for now
                }

                // Decompress the file into the destination path
                File outputFile = new File(destinationPath, entry.getName());
                File outputDir = outputFile.getParentFile();
                if (!outputDir.exists() && !outputDir.mkdirs()) {
                    throw new IOException("Failed to create output directory: " + outputDir.getAbsolutePath());
                }

                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = tarInput.read(buffer)) > 0) {
                        out.write(buffer, 0, len); // Write buffer to output file
                    }
                }
            }
        }
    }

    /**
     * Main method for testing the decompression tool.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileDecompressor decompressor = new FileDecompressor();
        try {
            decompressor.decompressGzipTar("path/to/source.tar.gz", "path/to/destination");
            System.out.println("Decompression completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Decompression failed: " + e.getMessage());
        }
    }
}
