import java.sql.*;

/**
 * Allows data to be added from the database named tests.
 *
 * @author Emily Zhou
 * @version 7/6/18
 */
public class MyJDBCInsert extends MyJDBC
{
    /**
     * Constructor for objects of class MyJDBCInsert.
     */
    public MyJDBCInsert() throws Exception
    {
        super();
    }

    /**
     * Imputs information into the database called test.
     */
    public void insertRecord(StudentReccord student) throws Exception
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getJDBCConnection();
            System.out.println("Connected database successfully");
            System.out.println("Inserting record into the table...");
            stmt = conn.createStatement();
            String sql = "INSERT INTO grades VALUES (" + "'" 
            + student.getName() + "'," + "'" + student.getGrade() + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted record into the table");
            System.out.println("Goodbye!");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}
