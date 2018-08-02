import java.util.Scanner;
/**
 * Runs the entry of data in the database tests.
 *
 * @author Emily Zhou
 * @version 7/6/18
 */
public class MyJDBCInsertRunner
{
    
    /**
     * Main method that runs the entry of data into the database tests.
     */
    public static void main(String[] args) throws Exception
    {
        MyJDBCInsert my = new  MyJDBCInsert();
        Scanner sc = new Scanner(System.in);
        System.out.println ("Name you would like to ENTER:");
        String name = sc.nextLine();
        System.out.println ("LetterGrade you would like to ENTER:");
        String lettergrade = sc.nextLine();
        StudentReccord newreccord = new StudentReccord(name,lettergrade);
        my.insertRecord(newreccord);
        System.out.println("DONE!");
    }
}
