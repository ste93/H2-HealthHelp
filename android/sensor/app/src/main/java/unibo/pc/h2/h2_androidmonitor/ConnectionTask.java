package unibo.pc.h2.h2_androidmonitor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.Serializable;

/**
 * Simple class to handle AsynchTask for connection to the Control Unit trough a TCP socket.
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class ConnectionTask extends AsyncTask<Void, Void, Void> implements Serializable {

    private HttpPoster socket;
    private String sensorId;
    private boolean initialized = false;

    public ConnectionTask(Context context, String hostIp, String hostPort, String sensorID) {
        this.sensorId = sensorID;
        this.socket = new HttpPoster(context, hostIp, hostPort, sensorId);
    }

    /**
     * Send a message with the information about the sensor and his user.
     */
    public void sendConfigMessage() {
        this.socket.sendConfigurationMessage();
    }

    /**
     * Add some sensor data to the Tcp socket in order to send it to the remote host.
     * @param data the sensor data string to be sent to the remote host.
     */
    public void addData(String data){
        this.socket.addData(data);
    }

    /**
     * Close the connection to the remote host
     */
    public void stop(){
        this.socket.stopClient();
    }

   @Override
   protected Void doInBackground(Void... params) {
        if (!initialized){
            this.socket.sendConfigurationMessage();
            initialized = true;
        }
       this.socket.run();
       Log.d("TCP TASK", "doInBackground: Connected to the Internet");
       while(true){}
   }
}

