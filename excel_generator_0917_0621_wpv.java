// 代码生成时间: 2025-09-17 06:21:07
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelGenerator {
    
    private SparkSession sparkSession;
    private String outputPath;
    
    /**
     * 构造函数，初始化SparkSession和输出路径
     * @param sparkSession Spark会话
     * @param outputPath 输出Excel文件的路径
     */
    public ExcelGenerator(SparkSession sparkSession, String outputPath) {
        this.sparkSession = sparkSession;
        this.outputPath = outputPath;
    }
    
    /**
     * 根据数据集生成Excel文件
     * @param data 数据集
     * @param sheetName 工作表名称
     * @param columnNames 列名列表
     */
    public void generateExcel(Dataset<Row> data, String sheetName, List<String> columnNames) {
        try {
            Workbook workbook = new XSSFWorkbook();
            // 创建工作表
            int sheetIndex = workbook.getSheetIndex(sheetName);
            if (sheetIndex == -1) {
                sheetIndex = workbook.createSheet(sheetName).getSheetIndex();
            }
            
            // 添加列名行
            if (columnNames != null && !columnNames.isEmpty()) {
                Row headerRow = workbook.getSheetAt(sheetIndex).createRow(0);
                for (int i = 0; i < columnNames.size(); i++) {
                    headerRow.createCell(i).setCellValue(columnNames.get(i));
                }
            }
            
            // 添加数据行
            int rowIndex = 1;
            for (Row row : data.collectAsList()) {
                Row excelRow = workbook.getSheetAt(sheetIndex).createRow(rowIndex++);
                for (int i = 0; i < row.size(); i++) {
                    excelRow.createCell(i).setCellValue(row.getString(i));
                }
            }
            
            // 写入文件
            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                workbook.write(outputStream);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Excel file.", e);
        }
    }
    
    /**
     * 主函数，用于运行程序
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SparkSession spark = SparkSession
            .builder()
            .appName("Excel Generator")
            .master("local[*]")
            .getOrCreate();
        
        String outputPath = "path_to_output_excel_file.xlsx";
        
        // 创建Excel生成器实例
        ExcelGenerator excelGenerator = new ExcelGenerator(spark, outputPath);
        
        // 示例数据集
        List<Row> dataList = Arrays.asList(
            spark.createDataFrame(Arrays.asList(
                new Row(new Object[] {"John", 28}),
                new Row(new Object[] {"Jane", 22}),
                new Row(new Object[] {"Bob", 34})
            ), Row.class).collectAsList()
        );
        
        // 转换数据集为Dataset<Row>
        Dataset<Row> data = spark.createDataFrame(dataList, Row.class);
        
        // 生成Excel文件
        excelGenerator.generateExcel(data, "Sheet1", Arrays.asList("Name", "Age"));
        
        spark.stop();
    }
}