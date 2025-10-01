// 代码生成时间: 2025-10-01 20:33:54
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/fileupload")
public class FileUploadComponent {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadComponent.class);
    private final JavaSparkContext sparkContext;
    private final SparkSession sparkSession;

    public FileUploadComponent(JavaSparkContext sparkContext, SparkSession sparkSession) {
        this.sparkContext = sparkContext;
        this.sparkSession = sparkSession;
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(FormDataMultiPart formDataMultiPart) {
        try {
            List<FormDataBodyPart> bodyParts = formDataMultiPart.getBodyParts();
            if (bodyParts == null || bodyParts.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No file to upload").build();
            }

            FormDataBodyPart filePart = bodyParts.get(0);
            if (filePart == null || !filePart.getMediaType().toString().equals(MediaType.APPLICATION_OCTET_STREAM)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid file type").build();
            }

            InputStream inputStream = filePart.getValueAs(InputStream.class);
            String fileName = filePart.getContentDisposition().getFileName();
            if (fileName == null || fileName.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("File name is missing").build();
            }

            // Save the file to a temporary location
            String tempFilePath = "/tmp/" + fileName;
            Files.copy(inputStream, Paths.get(tempFilePath));

            // Process the file using Spark
            // This is a placeholder for actual file processing logic
            // For example, you could use DataFrameReader to read the file and perform operations

            return Response.status(Response.Status.OK).entity("File uploaded successfully").build();
        } catch (IOException e) {
            logger.error("Error uploading file", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error uploading file").build();
        }
    }
}
