import java.util.Scanner;

/**
 * The TG JDBC runner that runs TG reader to read data from JDBC database and
 * pass the data to TG writer.
 *
 * @author Emily Zhou
 * @version 7/31/18
 */
public class TGJDBCRunner
{
    /**
     * Main method that runs the operation.
     */
    public static void main(String[] args) throws Exception
    {
        /*
         * Get the config so we know how to talk to RDBMS and TG
         */
        if (args.length == 0 ) {
            System.out.println("Please provide the config file and/or password");
            System.exit(1);
        }
        TGJdbcConfig tgjdbccfg = new TGJdbcConfig(args[0]);
        /* User has the option to provide the password in command line */
        if (args.length > 1 ) {
            tgjdbccfg.db_password = args[1];
        }
        /*
         * Connet to DB and read the data.
         * Using TGJdbcConfig will make the API simple...
         */
        TGJdbcReader myDBReader = new TGJdbcReader(tgjdbccfg);
        myDBReader.getConnection();
        myDBReader.getResultSet();

        /** Setup the Writer to TG
         * Using TGJdbcConfig will make the API simple...
         */
        JdbcTGWriter myTGWriter = new JdbcTGWriter(tgjdbccfg);
        /*
         *  Now do the loading
         */
        while (myDBReader.next()) {
            String row_as_string = myDBReader.record2string();
            System.out.println("Read record from database: " + row_as_string);
            myTGWriter.BatchPost(row_as_string);
            System.out.println("Posted record to TG: " + row_as_string);
            myDBReader.showProgress();
        }
        myTGWriter.close();        
    }
}