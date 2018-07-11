import java.util.Scanner;
/**
 * Runs the selcetion of data in the database tests.
 *
 * @author Emily Zhou
 * @version 7/6/18
 */
public class MyJDBCSelectRunner
{
    /**
     * Main method that runs the operation.
     */
    public static void main(String[] args) throws Exception
    {
        MyJDBCSelect my = new  MyJDBCSelect();
        Scanner sc = new Scanner(System.in);
        System.out.println ("Name you would like to FIND:");
        String name = sc.nextLine();
        my.selectRecord(name);
        System.out.println("DONE!");
    }
}