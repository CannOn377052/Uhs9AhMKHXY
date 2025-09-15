// 代码生成时间: 2025-09-15 19:40:07
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.*;

public class MathematicalTools {

    private SparkSession spark;

    // 构造函数，初始化SparkSession
    public MathematicalTools() {
        spark = SparkSession.builder()
            .appName("Mathematical Tools")
            .getOrCreate();
    }

    // 加法运算
    public double add(double a, double b) {
        return a + b;
    }

    // 减法运算
    public double subtract(double a, double b) {
        return a - b;
    }

    // 乘法运算
    public double multiply(double a, double b) {
        return a * b;
    }

    // 除法运算
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a / b;
    }

    // 求幂运算
    public double power(double a, double b) {
        return Math.pow(a, b);
    }

    // 关闭SparkSession
    public void stop() {
        if (spark != null) {
            spark.stop();
        }
    }

    // 测试方法
    public static void main(String[] args) {
        try {
            MathematicalTools tools = new MathematicalTools();

            double resultAdd = tools.add(5, 3);
            double resultSubtract = tools.subtract(5, 3);
            double resultMultiply = tools.multiply(5, 3);
            double resultDivide = tools.divide(5, 3);
            double resultPower = tools.power(5, 3);

            System.out.println("Addition: 5 + 3 = " + resultAdd);
            System.out.println("Subtraction: 5 - 3 = " + resultSubtract);
            System.out.println("Multiplication: 5 * 3 = " + resultMultiply);
            System.out.println("Division: 5 / 3 = " + resultDivide);
            System.out.println("Power: 5 ^ 3 = " + resultPower);

            tools.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
