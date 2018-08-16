import java.sql.*;

/**
 * Allows the data of a chosen student to be retrived from the database
 * named test.
 * 
 * @author Emily Zhou
 * @version 7/6/18
 */
public class MyJDBCSelect extends MyJDBC
{ 
    /**
     * Constructor for MyJDBCSelect.
     */
    public MyJDBCSelect() throws Exception
    {
        super();
    }

    /**
     * Selects the data of a student to be retirved from the database.
     *
     * @param the name of the student
     */
    public void selectRecord(String studentName) throws Exception
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getJDBCConnection();
            System.out.println("Connected database successfully");
            System.out.println("Select a record from table ...");
            stmt = conn.createStatement();
            String sql = "SELECT * from grades where name=" + "'"+ 
                         studentName + "'";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("name");
                String lettergradegrade = rs.getString("lettergrade");
                System.out.println("name: " + name + ", lettergrade: " 
                + lettergradegrade);
            }

            rs.close();
            
            System.out.println("Goodbye!");
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();

        }
    }
}
