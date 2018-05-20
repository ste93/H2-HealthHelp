package unibo.pc.h2.h2_androidmonitor;

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

    private TCPClient socket;
    private String sensorId;

    public ConnectionTask(String sensorId) {
        this.sensorId = sensorId;
        this.socket = new TCPClient(sensorId, new TCPClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                Log.d("TCP Task", message);
            }
        });

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
       this.socket.connect();
       this.socket.run();
       Log.d("TCP TASK", "doInBackground: Connected to the Internet");
       while(true){}
   }
}

