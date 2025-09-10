// 代码生成时间: 2025-09-11 04:37:29
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Excel表格自动生成器
public class ExcelGenerator {

    // 构造函数，初始化SparkSession
    public ExcelGenerator() {
        SparkSession spark = SparkSession.builder()
                .appName("Excel Generator")
                .master("local[*]")
                .getOrCreate();
    }

    // 生成Excel表格
    public void generateExcel(String path, String sheetName, Dataset<Row> data) {
        try {
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();

            // 创建一个名为sheetName的sheet
            if (workbook.getSheet(sheetName) == null) {
                workbook.createSheet(sheetName);
            }

            // 将数据写入Excel
            int rowNum = 0;
            for (Row row : data.collect()) {
                rowNum++;
                org.apache.poi.ss.usermodel.Row excelRow = workbook.getSheet(sheetName).createRow(rowNum);
                int cellNum = 0;
                for (int i = 0; i < row.size(); i++) {
                    excelRow.createCell(cellNum++).setCellValue(row.get(i).toString());
                }
            }

            // 将工作簿写入文件
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

            System.out.println("Excel file has been generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error generating Excel file: " + e.getMessage());
        }
    }

    // 读取Excel文件并返回Dataset<Row>
    public Dataset<Row> readExcel(String path) {
        try {
            File excelFile = new File(path);
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = WorkbookFactory.create(inputStream);

            // 读取第一个sheet的数据
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            SparkSession spark = SparkSession.builder()
                    .appName("Excel Reader")
                    .master("local[*]")
                    .getOrCreate();

            // 将Excel数据转换为Dataset<Row>
            Dataset<Row> data = spark.read()
                    .format("com.crealytics.spark.excel")
                    .option("header", "true")
                    .load(excelFile.getAbsolutePath());

            inputStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error reading Excel file: " + e.getMessage());
            return null;
        }
    }

    // 主方法，用于执行程序
    public static void main(String[] args) {
        try {
            ExcelGenerator excelGenerator = new ExcelGenerator();
            // 示例：读取Excel文件并生成新的Excel文件
            Dataset<Row> data = excelGenerator.readExcel("input.xlsx");
            excelGenerator.generateExcel("output.xlsx", "MySheet", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
