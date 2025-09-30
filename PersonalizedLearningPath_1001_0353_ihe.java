// 代码生成时间: 2025-10-01 03:53:31
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import java.util.Arrays;
import java.util.List;

/**
 * 个性化学习路径程序，使用SPARK框架处理数据
 */
public class PersonalizedLearningPath {

    /**
     * 主方法，程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 初始化SPARK会话
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("PersonalizedLearningPath")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());

        try {
            // 加载课程数据
            JavaRDD<String> courseData = sc.textFile("courses.txt");

            // 解析课程数据
            JavaRDD<Course> courses = courseData.map(line -> {
                String[] fields = line.split(",");
                return new Course(fields[0], fields[1], fields[2]);
            });

            // 加载学习者数据
            JavaRDD<String> learnerData = sc.textFile("learners.txt");

            // 解析学习者数据
            JavaRDD<Learner> learners = learnerData.map(line -> {
                String[] fields = line.split(",");
                return new Learner(fields[0], fields[1], Integer.parseInt(fields[2]));
            });

            // 计算个性化学习路径
            Dataset<Row> learningPaths = calculateLearningPaths(courses, learners, sparkSession);

            // 保存结果到文件
            learningPaths.write().save("learning_paths.parquet");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
            sparkSession.stop();
        }
    }

    /**
     * 计算个性化学习路径
     * @param courses 课程数据
     * @param learners 学习者数据
     * @param sparkSession SPARK会话
     * @return 个性化学习路径
     */
    public static Dataset<Row> calculateLearningPaths(JavaRDD<Course> courses, JavaRDD<Learner> learners, SparkSession sparkSession) {
        // 将RDD转换为DataFrame
        Dataset<Row> coursesDF = sparkSession.createDataFrame(courses, Course.class);
        Dataset<Row> learnersDF = sparkSession.createDataFrame(learners, Learner.class);

        // 根据学习者的能力水平和课程难度计算个性化学习路径
        Dataset<Row> learningPaths = coursesDF.join(learnersDF, coursesDF.col("course_id").equalTo(learnersDF.col("course_id"))).
                select(coursesDF.col("course_id"), coursesDF.col("course_name"), coursesDF.col("difficulty"), learnersDF.col("learner_id"), learnersDF.col("learner_name"), learnersDF.col("ability_level"));

        // 过滤出难度与学习者能力水平相匹配的课程
        learningPaths = learningPaths.filter(learningPaths.col("difficulty").leq(learningPaths.col("ability_level")));

        return learningPaths;
    }

    /**
     * 课程类
     */
    public static class Course {
        private String courseId;
        private String courseName;
        private String difficulty;

        public Course(String courseId, String courseName, String difficulty) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.difficulty = difficulty;
        }

        // Getter和Setter方法
        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
    }

    /**
     * 学习者类
     */
    public static class Learner {
        private String learnerId;
        private String learnerName;
        private int abilityLevel;

        public Learner(String learnerId, String learnerName, int abilityLevel) {
            this.learnerId = learnerId;
            this.learnerName = learnerName;
            this.abilityLevel = abilityLevel;
        }

        // Getter和Setter方法
        public String getLearnerId() {
            return learnerId;
        }

        public void setLearnerId(String learnerId) {
            this.learnerId = learnerId;
        }

        public String getLearnerName() {
            return learnerName;
        }

        public void setLearnerName(String learnerName) {
            this.learnerName = learnerName;
        }

        public int getAbilityLevel() {
            return abilityLevel;
        }

        public void setAbilityLevel(int abilityLevel) {
            this.abilityLevel = abilityLevel;
        }
    }
}