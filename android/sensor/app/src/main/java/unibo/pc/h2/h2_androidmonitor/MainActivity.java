package unibo.pc.h2.h2_androidmonitor;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.sensorextension.Ssensor;
import com.samsung.android.sdk.sensorextension.SsensorEvent;
import com.samsung.android.sdk.sensorextension.SsensorEventListener;
import com.samsung.android.sdk.sensorextension.SsensorExtension;
import com.samsung.android.sdk.sensorextension.SsensorManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Single Activity application to handle Samsung's heartbeat sensor in order to read vital parameter
 * from patient and send it to the datacenter.
 * Part of the H2 - Health Help project
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class MainActivity extends Activity {

    public static final String TAG = "H2-HeartBeatMonitor";

    //todo: il sensor id dovrà essere gestito in modo univoco.
    //private static final String sensorID = "AHB-42";

    private String sensorID = null;
    SsensorManager SSensorManager = null;
    SsensorExtension SsensorExtension = null;
    Ssensor IRSensor = null;
    Ssensor redLightSensor = null;
    SSListenerIR IRlistener = null;
    SSListenerRED redLightListener = null;

    Activity mContext;

    ToggleButton senseButton = null;
    ToggleButton emergencyButton = null;
    ToggleButton connectionConfButton = null;
    private TextView beatLabel = null;
    private RealtimeUpdatesFragment chartFragment = null;

    private ConnectionTask tcpTask;

    boolean emergency;

    String hostIp;
    String hostPort;

    @TargetApi(23) @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emergency = false;

        senseButton = (ToggleButton) findViewById(R.id.sense_button);
        emergencyButton = (ToggleButton) findViewById(R.id.emergency_button);
        connectionConfButton = (ToggleButton) findViewById(R.id.connection_button);
        beatLabel = (TextView) findViewById(R.id.beatLabel);

        chartFragment =  (RealtimeUpdatesFragment) getFragmentManager().findFragmentById(R.id.chart_fragment);

        //// Behaviour of the Sense Button //////
        IRlistener = new SSListenerIR();
        redLightListener = new SSListenerRED();
        if (senseButton != null) {
            senseButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    senseButton.setSelected(!senseButton.isSelected());

                    try {
                        if (!senseButton.isSelected()) {
                            // HRM OFF
                            senseButton.setText(senseButton.getTextOff());
                            senseButton.setSelected(false);
                            senseButton.setChecked(false);
                            if (SSensorManager != null) {
                                SSensorManager.unregisterListener(IRlistener, IRSensor);
                                SSensorManager.unregisterListener(redLightListener, redLightSensor);
                            }

                        } else {
                            senseButton.setText(senseButton.getTextOn());
                            senseButton.setSelected(true);
                            senseButton.setChecked(true);
                            SsensorExtension = new SsensorExtension();
                            try {
                                SsensorExtension.initialize(mContext);
                                SSensorManager = new SsensorManager(mContext, SsensorExtension);
                                IRSensor = SSensorManager.getDefaultSensor(Ssensor.TYPE_HRM_LED_IR);
                                redLightSensor = SSensorManager.getDefaultSensor(Ssensor.TYPE_HRM_LED_RED);
                            } catch (SsdkUnsupportedException e) {
                                Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_LONG).show();
                                mContext.finish();
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
                                mContext.finish();
                            }
                            // HRM ON

                            chartFragment.reset();
                            if (SSensorManager != null) {
                                SSensorManager.registerListener(IRlistener, IRSensor, SensorManager.SENSOR_DELAY_NORMAL);
                                SSensorManager.registerListener(redLightListener, redLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        ErrorToast(e);
                    }
                }
            });
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] { Manifest.permission.BODY_SENSORS }, 101);
                    return;
                }
            }
        }

        //// Behaviour of the Emergency Button //////
        if (emergencyButton != null) {
            emergencyButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {


                    emergencyButton.setSelected(!emergencyButton.isSelected());

                    try {
                        if (emergencyButton.isSelected()) {
                            DataManager.getInstance().setEmergency(true);

                        } else {
                            DataManager.getInstance().setEmergency(false);
                        }
                    } catch (IllegalArgumentException e) {
                        ErrorToast(e);
                    }
                }
            });
        }

        //// Behaviour of the Connection Button //////
        if (connectionConfButton != null) {
            connectionConfButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    connectionConfButton.setSelected(!connectionConfButton.isSelected());

                    try {
                        if (connectionConfButton.isSelected()) {

                            ////////////////////////////////////////

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Connect to .. ");

                            // Set up the input
                            final EditText input = new EditText(mContext);
                            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
                            builder.setView(input);

                            // Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String connectionString = input.getText().toString();
                                    hostIp = connectionString.split(":")[0];
                                    hostPort = connectionString.split(":")[1];
                                    if (hostIp != null && hostPort != null) {
                                        sensorID = getMacAddress();
                                        Log.d("HTTP", "HTTP : connect to " + hostIp);
                                        Log.d("HTTP", "HTTP : connect to " + hostPort);
                                        tcpTask = new ConnectionTask(hostIp, hostPort, sensorID);
                                        tcpTask.execute();
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();

                            //////////////////////////////////

                        }

                        else
                        {
                            if (tcpTask != null)
                                tcpTask.stop();
                            tcpTask = null;
                        }

                    } catch (IllegalArgumentException e) {
                        ErrorToast(e);
                    }
                }
            });
        }
    }

    /**
     * Handle message error using Toast.
     * @param exception the exception
     */
    public void ErrorToast(IllegalArgumentException exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (IRSensor != null) {
                SSensorManager.unregisterListener(IRlistener, IRSensor);
            }

            if (redLightSensor != null) {
                SSensorManager.unregisterListener(redLightListener, redLightSensor);
            }

            senseButton.setSelected(false);
            senseButton.setText(senseButton.getTextOff());
        } catch (IllegalArgumentException e) {
            this.finish();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    /**
     * Specify what to do when a beat value is available.
     * this implementation calculate the average value and send it to the remote host.
     */
    private float averageBeat(){
        float avgBeat = 0f;
        List<Float> beatRed = DataManager.getInstance().getBeatRed();
        List<Float> beatIR = DataManager.getInstance().getBeatIR();

        for(int i = 0; i < beatRed.size() && i < beatIR.size(); i ++ ){
            chartFragment.addRedData(beatRed.get(i));
            chartFragment.addIRData(beatIR.get(i));
            avgBeat += beatRed.get(i);
            avgBeat += beatIR.get(i);
        }
        avgBeat = avgBeat / (beatRed.size() + beatIR.size());

        if (tcpTask != null )
            this.tcpTask.addData("" + avgBeat);
        beatLabel.setText("Beat : " + avgBeat);
        return avgBeat;
    }

    /**
     * Specify the behaviour of the listener of the value of Infrared sensor ( what to do with read data )
     */
    private class SSListenerIR implements SsensorEventListener {

        private boolean complete = false;

        @Override
        public void OnAccuracyChanged(Ssensor arg0, int arg1) {}

        @Override
        public void OnSensorChanged(SsensorEvent event) {
            complete = DataManager.getInstance().addIRData(event.values[0]);
            if (complete){
                averageBeat();
            }
        }
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    /*public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }*(

    /**
     * Specify the behaviour of the listener of the value of Red Light sensor ( what to do with read data )
     */
    private class SSListenerRED implements SsensorEventListener {

        private boolean complete = false;

        @Override
        public void OnAccuracyChanged(Ssensor arg0, int arg1) {}

        @Override
        public void OnSensorChanged(SsensorEvent event) {
            /* complete = */DataManager.getInstance().addRedData(event.values[0]);
            /*if (complete){
                averageBeat();
            }*/
        }
    }
}