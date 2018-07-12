package unibo.pc.h2.h2_androidmonitor;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.net.ssl.HttpsURLConnection;

/**
 * Simple class to handle Http POST to the server.
 *
 * @author ManuBottax
 * @version 0.1.1 - June 2018
 */
public class HttpPoster {

    private URL url;
    private String hostIP;
    private String hostPort;

    private String sensorID;
    private boolean run = false;

    private HttpURLConnection connection;

    private Queue<String> dataQueue;

    private Context ctx;
 
    /**
     * Constructor of the class.
     * @param hostIP the server IP to connect with for making POST request.
     * @param hostPort The server port.
     * @param sensorID the unique id of the sensor. Used to identify it on the control unit.
     */
    public HttpPoster(Context context, String hostIP, String hostPort, String sensorID) {
        this.dataQueue = new PriorityQueue<>();
        this.sensorID = sensorID;
        this.ctx = context;
        this.hostIP = hostIP;
        this.hostPort = hostPort;

        try {
            this.url = new URL("http://" + hostIP + ":" + hostPort + "/api/sensors/"+ sensorID +"/data");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void sendConfigurationMessage() {

        String sensorInfo = "{}";

        try {
            FileInputStream input = ctx.openFileInput("h2Sensor.config");
            try {
                FileChannel fc = input.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                sensorInfo = Charset.defaultCharset().decode(bb).toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        URL url = null;
        try {
            url = new URL("http://" + this.hostIP + ":" + this.hostPort + "/api/sensors/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("HTTP Client","HTTP REQUEST - URI : " + url.toString());
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(150000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("HTTP Client","HTTP REQUEST - Connected");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            Log.d("HTTP Client", "sending Message : " + sensorInfo);
            writer.write(sensorInfo);
            writer.flush();
            writer.close();
            Log.d("HTTP Client","HTTP REQUEST - MESSAGE SENT");

            Log.d("HTTP Client","HTTP REQUEST - WAITING FOR RESPONSE ...");
            int responseCode=connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("HTTP Client","HTTP REQUEST - RESPONSE : OK");
            }
            else {
                Log.d("HTTP Client","HTTP REQUEST - RESPONSE : ERROR");
                Log.d("HTTP Client","HTTP REQUEST - ERROR :" + responseCode);
                Log.d("HTTP Client","HTTP REQUEST - ERROR :" + connection.getResponseMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            Log.d("HTTP Client","HTTP REQUEST - URI : " + url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(150000);
            connection.setConnectTimeout(150000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("HTTP Client","HTTP REQUEST - Connected");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            JSONObject json = new JSONObject();
            try {
                json.put("sensorID", sensorID);
                json.put("data", message);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("HTTP Client", "sending Message : " + message);
            Log.d("HTTP Client", "Sending JSON : " + json.toString());
            writer.write(json.toString());
            writer.flush();
            writer.close();
            Log.d("HTTP Client","HTTP REQUEST - MESSAGE SENT");

            Log.d("HTTP Client","HTTP REQUEST - WAITING FOR RESPONSE ...");
            int responseCode=connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("HTTP Client","HTTP REQUEST - RESPONSE : OK");
            }
            else {
                Log.d("HTTP Client","HTTP REQUEST - RESPONSE : ERROR");
                Log.d("HTTP Client","HTTP REQUEST - ERROR :" + responseCode);
                Log.d("HTTP Client","HTTP REQUEST - ERROR :" + connection.getResponseMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method run continuously and send to remote host
     * the data stacked in the queue once available.
     */
    public void run() {

        run = true;

        while (run) {
            if (!dataQueue.isEmpty()) {
                sendMessage(dataQueue.remove());
            }
        }

    }

    /**
     * Add a data string to the queue to be sent to the remote host.
     *
     * @param data message to be sent to the host.
     */
    public void addData(String data){
        this.dataQueue.add(data);
    }

    /**
     * Close the connection to the remote host.
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        //sendMessage("Closing Connection");

        run = false;

        if(connection != null)
            connection.disconnect();
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground

    /**
     * Specify the behaviour when a message is received from server.
     */
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}