// 代码生成时间: 2025-08-09 20:10:05
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

public class DataModelSparkApp {

    // Define a simple data model class
    public static class Person {
        private String name;
        private int age;

        public Person() { }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
        // Create Spark session
        SparkSession spark = SparkSession.builder()
                .appName("DataModelSparkApp")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        // Sample data
        List<Tuple2<String, Integer>> data = Arrays.asList(
            new Tuple2<>("John", 30),
            new Tuple2<>("Jane", 25),
            new Tuple2<>("Doe", 40)
        );

        // Convert to RDD and then to Dataset to show how to handle data models
        JavaRDD<Tuple2<String, Integer>> rdd = sc.parallelize(data);

        // Convert RDD to Dataset using a schema
        Dataset<Row> dataset = spark.createDataFrame(rdd, new Tuple2("name", "age\).getClass());

        // Register the temporary table
        dataset.createOrReplaceTempView("people");

        // Perform a query
        Dataset<Row> result = spark.sql("SELECT * FROM people WHERE age > 30");

        // Show the results of the query
        result.show();

        // Stop the Spark context
        sc.close();
    }
}
