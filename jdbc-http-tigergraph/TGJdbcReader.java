import java.sql.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * Read graph input data from a JDBC database.
 *
 * @author Emily Zhou
 * @version 7/31/18
 */
public class TGJdbcReader
{
    private Connection conn = null;
    public ResultSet rs = null;
    private ResultSetMetaData rsmd  = null;
    private TGJdbcConfig tgjdbc_config =  null;
    long Reading_Count;
    long Reading_Start_Time;
    
    /**
     * Load JDBC driver.
     * 
     * @param JDBC_DRIVER, e.g., com.mysql.jdbc.Driver 
     * @throw Exception if fails
     */
    private void find_driver(String JDBC_DRIVER) throws Exception
    {
        Class.forName(JDBC_DRIVER);
    }

    /**
     * Constructor for objects of class TGJdbcReader.
     * 
     * @param JDBC_DRIVER, e.g., com.mysql.jdbc.Driver 
     * @throw Exception if fails
     */
    public TGJdbcReader(String JDBC_DRIVER) throws Exception
    {
        find_driver(JDBC_DRIVER);
    }

    /**
     * Default constructor for TGJdbcReader is using mysql driver.
     * 
     * @throw Exception if fails
     */
    public TGJdbcReader() throws Exception
    {
        find_driver("com.mysql.cj.jdbc.Driver");
    }

    /**
     * Constructor for objects of class TGJdbcReader.
     * 
     * @param Tgjdbc_config the configuration to use
     * @throw Exception if fails
     */
    public TGJdbcReader(TGJdbcConfig tgjdbc_config) throws Exception
    {
        this.tgjdbc_config = tgjdbc_config;
        find_driver(this.tgjdbc_config.jdbc_driver);
    }

    /**
     * Gets connection to given database.
     * 
     * @param JDBC_URL, database URL, e.g., jdbc:mysql://localhost/test
     * @param USER, db user, e.g., tigergraph
     * @param PASS, db password, e.g., tigergraph
     * @throw Exception if fails
     */
    public void getConnection(String JDBC_URL, String USER, String PASS)
    throws Exception
    {
        this.conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    /**
     * Gets connection to given database.
     * 
     * @throw Exception if fails
     */
    public void getConnection() throws Exception
    {
        this.conn = DriverManager.getConnection(
            this.tgjdbc_config.jdbc_url,
            this.tgjdbc_config.db_user, 
            this.tgjdbc_config.db_password);
    }

    /**
     * Gets the ResultSet from JDBC query.
     * 
     * @param sql_select_string the query string
     * @throw Exception if fails
     */
    public void getResultSet(String sql_select_string)  throws Exception
    {
        Statement stmt = conn.createStatement();
        this.rs = stmt.executeQuery(sql_select_string);
        this.rsmd = this.rs.getMetaData();
        Reading_Count = 0;
        Reading_Start_Time = System.currentTimeMillis();
    }

    /**
     * Gets the cached ResultSet.
     * 
     * @throw Exception if fails
     */
    public void getResultSet()  throws Exception
    {
        this.getResultSet(this.tgjdbc_config.db_query);
    }

    /**
     * Returns the next row reccord.
     * 
     * @return the row reccord
     * @throw Exception if fails
     */
    public boolean next()  throws Exception {
        return this.rs.next();
    }

    /* return string from a row record */
    /**
     * Converts a record (JDBC table row data) to string format.
     * 
     * @param separator the separator between fields in string format
     * @return the reccord in string format
     * @throw Exception if fails
     */
    public String record2string(char separator)  throws Exception {
        StringBuilder sb = new StringBuilder();
        int numberOfColumns = rsmd.getColumnCount();
        for (int i = 1; i <= numberOfColumns; i++) {
            sb.append(rs.getString(i));
            if (i < numberOfColumns) {
                sb.append(separator);
            }
        }
        Reading_Count++;
        return sb.toString();
    }

    /**
     * Converts a record (JDBC table row data) to string format.
     * 
     * @return the reccord in string format
     * @throw Exception if fails
     */
    public String record2string()  throws Exception {
        return record2string(this.tgjdbc_config.tg_separator_ascii);
    }

    /**
     * Shows progress while retrieving records from JDBC database.
     * 
     * @throw Exception if fails
     */
    public void showProgress() throws Exception {
        if ( Reading_Count % 100000 == 0) {
            System.out.print(".");
            if ( Reading_Count % 2000000 == 0){
                System.out.print(" " + Reading_Count + "\t");
                long end = System.currentTimeMillis();
                NumberFormat formatter = new DecimalFormat("#0.000");
                System.out.println(formatter.format((end - Reading_Start_Time) / 1000d) + " seconds");
            }
        }
    }

    /**
     * Closes JDBC ResultSet and connection.
     * 
     * @throw Exception if fails
     */
    public void close() throws Exception
    {
        if (rs != null) rs.close();
        if (conn != null) conn.close();
    }
}

