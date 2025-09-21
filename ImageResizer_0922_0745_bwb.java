// 代码生成时间: 2025-09-22 07:45:54
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.SparkSession.Builder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class ImageResizer {
    // Configuration for Spark
    private static final String MASTER = "local[*]";
    private static final String APP_NAME = "ImageResizer";
    private static final String INPUT_PATH = "path/to/input/images";
    private static final String OUTPUT_PATH = "path/to/output/images";
    private static final int TARGET_WIDTH = 800;
    private static final int TARGET_HEIGHT = 600;

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster(MASTER)
                .setAppName(APP_NAME);
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        try {
            // Load images and resize them
            List<String> imagePaths = Arrays.asList(
                // List of image paths to be processed
            );
            Dataset<Row> images = spark.read()
                .textFile(INPUT_PATH)
                .as(Encoders.STRING())
                .map(path -> new ImageFile(path.trim()), Encoders.bean(ImageFile.class));

            images = resizeImages(images);

            // Save the resized images
            images.flatMap(ImageFile::saveToDisk, Encoders.string())
                .collect();

            System.out.println("Images resized and saved successfully!");
        } catch (Exception e) {
            System.err.println("Error resizing images: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.stop();
        }
    }

    /**
     * Resize the images using Spark's RDD operations.
     *
     * @param images Dataset of image paths
     * @return Dataset of resized images
     */
    private static Dataset<Row> resizeImages(Dataset<Row> images) {
        return images.map(imageFile -> {
            try {
                resizeImage(imageFile);
                return imageFile;
            } catch (IOException e) {
                throw new RuntimeException("Failed to resize image: " + imageFile.getPath(), e);
            }
        }, Encoders.bean(ImageFile.class));
    }

    /**
     * Resize a single image.
     *
     * @param imageFile ImageFile object containing image path and dimensions
     * @throws IOException if an I/O error occurs
     */
    private static void resizeImage(ImageFile imageFile) throws IOException {
        File originalFile = new File(imageFile.getPath());
        BufferedImage originalImage = ImageIO.read(originalFile);
        BufferedImage resizedImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage.getScaledInstance(TARGET_WIDTH, TARGET_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        // Save the resized image to the output path
        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(OUTPUT_PATH + File.separator + imageFile.getFileName());
        ImageIO.write(resizedImage, "jpg", outputFile);
    }

    /**
     * Helper class to hold image file information.
     */
    public static class ImageFile {
        private String path;
        private String fileName;

        public ImageFile(String path) {
            this.path = path;
            this.fileName = new File(path).getName();
        }

        public String getPath() {
            return path;
        }

        public String getFileName() {
            return fileName;
        }

        // Method to save the image to disk
        public List<String> saveToDisk() {
            // Implement the logic to save the resized image to disk
            // This method should be implemented as a flatMap operation in the resizeImages method
            return Arrays.asList(OUTPUT_PATH + File.separator + fileName);
        }
    }
}
