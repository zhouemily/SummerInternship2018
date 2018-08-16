import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Posts graph input data to TG database using TG REST API.
 *
 * @author Emily Zhou
 * @version 7/31/18
 */
public class JdbcTGWriter
{
    private TGJdbcConfig tgjdbc_config = null;
    private String url; // url = "http://10.85.228.2:9000/ddl?sep=" + "\2" + "&eol=" + "\10" + "&tag=job1";    private long tg_batch_size = 1;
    private StringBuffer StrBuf = null;
    private long msg_count = 0;
    private boolean force_flush;

    /**
     * Constructor for objects of class JdbcTGWriter.
     * 
     * @param tgjdbc_config the configuration to use
     * @throw Exception if fails
     * 
     */
    public JdbcTGWriter(TGJdbcConfig tgjdbc_config) throws Exception
    {
        this.tgjdbc_config = tgjdbc_config;
        this.url = tgjdbc_config.tg_url + "/ddl/" +
        tgjdbc_config.tg_graph +
        "?tag=" + tgjdbc_config.tg_loadjob +
        "&sep=" + tgjdbc_config.tg_separator_url +
        "&eol=" + tgjdbc_config.tg_eol_url;
        this.StrBuf = new StringBuffer();
        this.force_flush = true;
        System.out.println(this.url);
    }

    /**
     * Does batch posting to TG database.
     * 
     * @param data_string the data to be posted to TG
     * @throw Exception if fails
     */
    public void BatchPost(String data_string) throws Exception
    {
        try
        { 
            if (data_string.length() > 0) {
                this.StrBuf.append(data_string+this.tgjdbc_config.tg_eol_ascii);
                msg_count++;
            }

            if (msg_count == this.tgjdbc_config.tg_batch_size ||
            force_flush == true) {

                String urlParameters  = StrBuf.toString();
                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;

                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                conn.setDoOutput( true );
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                    wr.write( postData );
                }

                int status = conn.getResponseCode();
                //System.out.println("POST returned status: " + status);
                consumeResponse(conn);
                conn.disconnect();
                /** Reset for next round */
                this.msg_count = 0;
                this.StrBuf.setLength(0);
                if (status != 200) {
                    throw new Exception("POST returned status: " + status);
                }                
            }
        }
        catch (IOException ex) {

            ex.printStackTrace();
        }
    }

    /**
     * Checks that all the buffered data is sent.
     */
    public void close() 
    {
        this.force_flush = true;
        try {
            BatchPost("");
        } catch (Exception e) {
            System.out.println("JbcTGWriter.close() got exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Process the response from TG.
     * 
     * @param conn the HTTP connection
     * @throw Exception if fails
     */
    private void consumeResponse(HttpURLConnection con) throws Exception {
        try (BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            StringBuffer resp = new StringBuffer();
            while ((line = is.readLine()) != null) {
                resp.append(line);
            }
            //System.out.println("Got response: " + resp);
        }
    }
}

