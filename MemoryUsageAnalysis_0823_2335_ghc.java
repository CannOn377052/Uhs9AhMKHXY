// 代码生成时间: 2025-08-23 23:35:05
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import scala.Serializable;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.List;

public class MemoryUsageAnalysis {

    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();

    // 打印当前内存使用情况
    public static void printMemoryUsage() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        System.out.println("Heap Memory Usage");
        System.out.println("Initial: " + heapMemoryUsage.getInit());
        System.out.println("Used: " + heapMemoryUsage.getUsed());
        System.out.println("Committed: " + heapMemoryUsage.getCommitted());
        System.out.println("Max: " + heapMemoryUsage.getMax());
        System.out.println("-----");

        System.out.println("Non-Heap Memory Usage");
        System.out.println("Initial: " + nonHeapMemoryUsage.getInit());
        System.out.println("Used: " + nonHeapMemoryUsage.getUsed());
        System.out.println("Committed: " + nonHeapMemoryUsage.getCommitted());
        System.out.println("Max: " + nonHeapMemoryUsage.getMax());
        System.out.println("-----");
    }

    // 打印GC活动
    public static void printGCActivity() {
        for (GarbageCollectorMXBean gc : garbageCollectorMXBeans) {
            long(gc) count = gc.getCollectionCount();
            long(gc) time = gc.getCollectionTime();

            System.out.println("GC Name: " + gc.getName());
            System.out.println("Collection Count: " + count);
            System.out.println("Collection Time: " + time);
            System.out.println("-----");
        }
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Memory Usage Analysis");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            printMemoryUsage();
            printGCActivity();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
