import java.util.Scanner;

/**
 * This is a Runnable that a thread will run to process data from TG reader and pass the
 * data to TG writer.
 *
 * @author Emily Zhou
 * @version 8/7/18
 */
public class TGWriterThreadRunnable implements Runnable
{
    private static long startTime = 0;
    private static long endTime = 0;

    private TGJdbcReader myDBReader;
    private JdbcTGWriter myTGWriter;
    private long recordCount = 0;

    /**
     * Constructor for objects of class TGWriterThreadRunnable.
     * 
     * @param myDBReader the TG reader object
     * @param tgxxconfig  The TG configuration object
     * @throw Exception if fails
     */
    public TGWriterThreadRunnable(TGJdbcReader myDBReader, 
    TGJdbcConfig tgjdbccfg) throws Exception
    {
        this.myDBReader = myDBReader;
        this.myTGWriter = new JdbcTGWriter(tgjdbccfg);
    }

    /**
     * This method gets 1 record a time from TG reader then pass it to TG writer to post
     * to TG database until no more records in TG reader. It also calls recordStartTime()
     * at beginning and recordEndTime at the end.
     */
    public void run()
    {
        recordStartTime();
        System.out.println("TGWriterThreadRunner " + this + " run() started");
        try 
        {
            //Thread.sleep(1000);
            String row_as_string = null;
            while ((row_as_string=myDBReader.record2string()) != null) 
            {
                //System.out.println("Read record from database: " + row_as_string);
                myTGWriter.BatchPost(row_as_string);
                //System.out.println("Posted record to TG: " + row_as_string);
                //myDBReader.showProgress();
                recordCount++;
            }
        }
        catch (Exception e)
        {
            System.out.println("run() got exception: " + e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        } 
        finally 
        {
            myTGWriter.close();
            recordEndTime();
        }
    }

    /**
     * Set the current system time to the class variable startTime
     * if it has not been set already.
     */
    private static synchronized void recordStartTime()
    {
        if (startTime == 0) 
        {
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Set the current system time to the class variable endTime.
     */   
    private static synchronized void recordEndTime()
    {
        endTime = System.currentTimeMillis();
    }

    /**
     * Returns the start time.
     * 
     * @return the start time
     */ 
    public static synchronized long getStartTime()
    {
        return startTime;
    }

    /**
     * Returns the end time.
     * 
     * @return the end time
     */ 
    public static synchronized long getEndTime()
    {
        return endTime;
    }

    /**
     * Returns the count of records processed by this instance
     * 
     * @return the recordCount
     */ 
    public long getRecordCount() {
        return recordCount;
    }
}
