import java.sql.*;

/**
 * Allows data to be added and removed from the database named test.
 *
 * @author Emily Zhou
 * @version 7/6/18
 */
public class MyJDBC
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/test";  
    static final String USER = "root";
    static final String PASS = "tigergraph";  
    /**
     * Constructor for objects of class MyJDBC.
     */
    public MyJDBC() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to a selected database...");
    }
    
    /**
     * Constructor for objects of class MyJDBC.
     */
    public Connection getJDBCConnection() throws Exception
    {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}

