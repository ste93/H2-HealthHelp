package unibo.pc.h2.h2_androidmonitor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Simple class to handle TCP connection to the server and message sending.
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class TCPClient {
 
    public static final String SERVER_IP = "172.24.1.1"; //Control Unit IP Address
    public static final int SERVER_PORT = 12345;

    private final String sensorID;

    private boolean run = false;

    private String mServerMessage;
    private OnMessageReceived mMessageListener = null;

    private PrintWriter outBuffer;
    private BufferedReader inBuffer;

    private Queue<String> dataQueue;
 
    /**
     * Constructor of the class.
     * @param listener specify the behaviour when a message is received from server.
     * @param sensorID the unique id of the sensor. Used to identify it on the control unit.
     */
    public TCPClient(String sensorID, OnMessageReceived listener) {
        mMessageListener = listener;
        this.dataQueue = new PriorityQueue<>();
        this.sensorID = sensorID;
    }

    private void sendMessage(String message) {
        if (outBuffer != null && !outBuffer.checkError()) {
            outBuffer.println(message);
            outBuffer.flush();
        }
    }

    /**
     * Try to connect to the remote host and initialize the I/O Buffer.
     * N.B. This must be invoked before run()
     */
    public void connect() {

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");

            Socket socket = new Socket(serverAddr, SERVER_PORT);
            Log.e("TCP Client", " -- C: Socket Created...");
            try {
                outBuffer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.e("TCP Client", " -- C: Buffer Initialization completed !");
                Log.e("TCP Client", " -- C: Connection Message Sent to Server !");
            } catch (IOException ex) {Log.e("TCP Client", "C: Connection Error !");}

        } catch (IOException ex) {Log.e("TCP Client", "C: Connection Error !");}

    }

    /**
     * Once connected (using connect() method) this method run continuously and send to remote host
     * the data stacked in the queue once available.
     */
    public void run() {

        run = true;
        String currentData = "";

        while (run) {
            if (!dataQueue.isEmpty()) {
                currentData = "sensorData(" + sensorID + "," + dataQueue.remove() + "," + Calendar.getInstance().getTimeInMillis() + ")";
                Log.e("TCP Client", "C: Sending data to server ! ");
                sendMessage(currentData);
            }
                /*Log.e("TCP Client", "C: Waiting for ACK from Server ... ");
                try {
                    mServerMessage = inBuffer.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                        Log.e("TCP Client", "C: Received message from Server ! ");
                        Log.e("TCP Client", "C: Received Message: '" + mServerMessage + "'");
                    }

                } catch (SocketException ex) {
                    Log.e("TCP Client", "Connection closed by the host");
                } catch (IOException e) {e.printStackTrace();}
            }*/
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

        if (outBuffer != null) {
            outBuffer.flush();
            outBuffer.close();
        }

        mMessageListener = null;
        inBuffer = null;
        outBuffer = null;
        mServerMessage = null;
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