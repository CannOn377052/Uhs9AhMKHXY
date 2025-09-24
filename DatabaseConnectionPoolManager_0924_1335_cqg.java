// 代码生成时间: 2025-09-24 13:35:31
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * DatabaseConnectionPoolManager class manages the database connection pool.
 * It uses Apache Commons DBCP library for the connection pool management.
 */
public class DatabaseConnectionPoolManager {
    
    // Connection pool map to store the data sources
    private static ConcurrentHashMap<String, DataSource> connectionPools = new ConcurrentHashMap<>();

    /**
     * Initializes the connection pool and adds it to the map.
     * 
     * @param url Database URL
     * @param username Database username
     * @param password Database password
     * @param poolName Unique pool name for identification
     * @param maxTotal Maximum number of active connections
     * @param maxIdle Maximum number of idle connections
     * @param minIdle Minimum number of idle connections
     */
    public static synchronized void initializeConnectionPool(
            String url, String username, String password, String poolName, 
            int maxTotal, int maxIdle, int minIdle) {
        
        try {
            // Create a BasicDataSource instance
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            basicDataSource.setMaxTotal(maxTotal);
            basicDataSource.setMaxIdle(maxIdle);
            basicDataSource.setMinIdle(minIdle);

            // Add the data source to the connection pools map
            connectionPools.putIfAbsent(poolName, basicDataSource);
        } catch (Exception e) {
            // Handle exceptions such as SQLException or IllegalArgumentException
            e.printStackTrace();
        }
    }

    /**
     * Returns a connection from the connection pool.
     * 
     * @param poolName Unique pool name for identification
     * @return Connection object
     */
    public static Connection getConnection(String poolName) {
        try {
            // Get the data source from the connection pools map
            DataSource dataSource = connectionPools.get(poolName);
            if (dataSource == null) {
                throw new IllegalStateException("There's no connection pool initialized with the name: " + poolName);
            }
            // Get a connection from the data source
            return dataSource.getConnection();
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the connection pool and removes it from the map.
     * 
     * @param poolName Unique pool name for identification
     */
    public static synchronized void closeConnectionPool(String poolName) {
        // Get the data source from the connection pools map
        DataSource dataSource = connectionPools.get(poolName);
        if (dataSource != null) {
            try {
                // Close the data source
                dataSource.close();
                // Remove the data source from the connection pools map
                connectionPools.remove(poolName);
            } catch (Exception e) {
                // Handle exceptions such as SQLException or IllegalStateException
                e.printStackTrace();
            }
        }
    }

    // Main method for demonstration purposes
    public static void main(String[] args) {
        // Initialize a connection pool
        initializeConnectionPool("jdbc:mysql://localhost:3306/mydatabase", "root", "password", "myPool", 10, 5, 2);
        
        // Get a connection from the pool
        Connection connection = getConnection("myPool");
        
        // Use the connection
        // ...
        
        // Close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Close the connection pool
        closeConnectionPool("myPool");
    }
}