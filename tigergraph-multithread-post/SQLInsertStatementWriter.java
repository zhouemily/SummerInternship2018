import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Random;

/**
 * This utility class write specified number of SQL INSERT statements to a file.
 *
 * @author Emily Zhou
 * @version 8/9/18
 */
public class SQLInsertStatementWriter
{
    static String OUT_FILENAME = "out.txt";
    static String INSERT_STATEMENT_FORMATTER = "insert into SocialUser values(%d,'Emily%d',%d,%d);";
    /**
     * Main method that runs the operation.
     * 
     * @param args null
     * @throw Exception if fails
     */
    public static void main(String[] args) throws IOException 
    {
        File fout = new File(OUT_FILENAME);
        try (FileOutputStream fos = new FileOutputStream(fout); 
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos)))
        {
            for (int i = 1; i <= 10000; i++) 
            {
                double rAsFloat = 2 * Math.random( );
                int num = (int)rAsFloat;
                String line = String.format(INSERT_STATEMENT_FORMATTER,i,i,num,System.currentTimeMillis());
                bw.write(line);
                bw.newLine(); 
                //System.out.println(line);
            }
        }
    }
}
