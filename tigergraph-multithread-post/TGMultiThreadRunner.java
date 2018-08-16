import java.util.Scanner;
import java.util.ArrayList; 
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Random;

/**
 * The TG multi-thread runner that runs TG reader to read data from JDBC database and
 * pass the data to TG writer.
 * 
 * It creates a number of threads to run TG Runnable concurrently which pulls data from
 * TG reader and pass the data to TG writer to post to TG database.cat benchmark.csvfile
 *
 * The program writes the bench mark number to benchmark.csv file after a run 
 * completes.The benchmark.csv file can be viewed in a csv graph viewer 
 * for example http://evanplaice.github.io/jquery-csv/examples/flot.html
 *
 * @author Emily Zhou
 * @version 7/31/18
 */
public class TGMultiThreadRunner
{
    static String BENCHMARK_OUTFILE = "./benchmark.csv";
    /**
     * Main method that runs the operation.
     * 
     * @param args command line arguments to pass in, expected a file name that contains
     *        json formatted configurations for accessing JDBC database and TigerGraph
     * @throw Exception if fails
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
        int threadCount = (int) tgjdbccfg.run_threads;
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            Thread thr = new Thread(new TGWriterThreadRunnable(myDBReader,
                        tgjdbccfg));
            threads.add(thr);
        }
        for (int i =  0; i < threadCount; i++) 
        {
            Thread thr = threads.get(i);
            thr.start();
        }
        for (int i = 0; i < threadCount; i++) {
            Thread thr = threads.get(i);
            System.out.println("Waiting for thread " + thr + " complete ..");
            thr.join();
            System.out.println("Thread " + thr + " completed");
        }     
        long timeTaken = TGWriterThreadRunnable.getEndTime() - 
                         TGWriterThreadRunnable.getStartTime();
        long recordCount = TGWriterThreadRunnable.getRecordCount();
        System.out.println("ThreadCount: " + threadCount +",  TimeTaken:"
            + timeTaken + " ms, " + "RecordCount: " + recordCount);
        writeBenchMark(threadCount, timeTaken, recordCount);
    }
    
    /**
     * This method append bench mark number as 1 line to the csv file.
     * 
     * @param threadCount the number of threads used for the run
     * @param timeTaken the time taken in milliseconds for the run
     * @param recordCount the number of records processed in the run
     * @throw Exception if fails
     */
    public static void writeBenchMark(int threadCount, long timeTaken, long recordCount)
    throws Exception 
    {
        File fout = new File(BENCHMARK_OUTFILE);
        boolean addHeader = false;
        if (fout.length() == 0) 
        {
            addHeader = true;
        }
        try (FileOutputStream fos = new FileOutputStream(fout,true); 
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos)))
        {
            if (addHeader) {
                String line = String.format("\"ThreadCount\",\"TimeTaken(ms) %d POST\"", 
                        recordCount);
                bw.write(line);
                bw.newLine();
            }
            String line = String.format("%d,%d", threadCount, timeTaken);
            bw.write(line);
            bw.newLine();
        }  
    }
}