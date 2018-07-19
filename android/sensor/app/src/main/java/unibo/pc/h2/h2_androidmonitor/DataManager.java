package unibo.pc.h2.h2_androidmonitor;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import static unibo.pc.h2.h2_androidmonitor.MainActivity.TAG;

/**
 * Singleton class to handle sensor data from different sources.
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class DataManager {

    private static DataManager instance = null;

    private List<Float> redDataList1;
    private List<Float> redDataList2;
    private List<Float> irDataList1;
    private List<Float> irDataList2;
    private List<Float> beatList;
    private List<Float> beatIRList;

    private int IRListNum;
    private int IRElemCount;

    private int redListNum;
    private int redElemCount;

    private boolean emergency;

    /**
     * Get the unique instance of the class
     * @return the instance of the Singleton class
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {
        redDataList1 = new ArrayList<>();
        redDataList2 = new ArrayList<>();
        irDataList1 = new ArrayList<>();
        irDataList2 = new ArrayList<>();
        beatList = new ArrayList<>();
        beatIRList = new ArrayList<>();
        IRListNum = 1;
        IRElemCount = 0;
        redListNum = 1;
        redElemCount = 0;
        emergency = false;
    }


    /**
     * Get the size of the list of calculated beat from Red Light Sensor data
     * @return the size of the list
     */
    public int getBeatRedSize() {
        return beatList.size();
    }

    /**
     * Get the size of the list of calculated beat from InfraRed Sensor data
     * @return the size of the list
     */
    public int getBeatIRSize() {
        return beatIRList.size();
    }

    /**
     * Get the list of calculated beat from Red Light Sensor data
     * @return the list
     */
    public List<Float> getBeatRed() {
        return beatList;
    }

    /**
     * Get the list of calculated beat from InfraRed Sensor data
     * @return the list
     */
    public List<Float> getBeatIR() {
        return beatIRList;
    }

    /**
     * Add a single Infrared data from the sensor to the list and compute if there are enough data
     * to perform the analysis. if there are enough perform the analysis and return a true value to
     * the caller in order to notify that an analysis is available.
     * All analyzed data (beat value) are saved in beatIR list.
     *
     * N.B. if emergency flag is active no analysis is performed and sensor data will be lost in order
     * to simulate emergency with low beat rate.
     *
     * @param value the value read from the Infrared sensor
     * @return if analysis is available
     */
    public boolean addIRData(float value) {

        if (IRListNum == 1) {
            irDataList1.add(value);
            IRElemCount++;
            if (IRElemCount % 128 == 0) {
                irDataList2.clear();
                IRListNum = 2;
                IRElemCount = 0;

                float beat;
                if (emergency){
                    beat = (float) Math.random();
                    Log.d(TAG, "Generated Emergency beat -> " + beat);
                }
                else {
                    beat = SignalAnalyser.analyzeData(irDataList1);
                    Log.d(TAG, "Measured IR beat -> " + beat);
                }

                beatIRList.add(beat);
                return true;
            }
        } else if (IRListNum == 2) {
            irDataList2.add(value);
            IRElemCount++;
            if (IRElemCount % 128 == 0) {
                irDataList1.clear();
                IRListNum = 1;
                IRElemCount = 0;

                float beat;
                if (emergency){
                    beat = (float) Math.random();
                    Log.d(TAG, "Generated Emergency beat -> " + beat);
                }
                else {
                    beat = SignalAnalyser.analyzeData(irDataList2);
                    Log.d(TAG, "Measured IR beat -> " + beat);
                }

                beatIRList.add(beat);
                return true;
            }
        }

        return false;
    }

    /**
     * Add a single Red Light data from the sensor to the list and compute if there are enough data
     * to perform the analysis. if there are enough perform the analysis and return a true value to
     * the caller in order to notify that an analysis is available.
     * All analyzed data (beat value) are saved in beatRed list.
     *
     * N.B. if emergency flag is active no analysis is performed and sensor data will be lost in order
     * to simulate emergency with low beat rate.
     *
     * @param value the value read from the Red Light sensor
     * @return if analysis is available
     */
    public boolean addRedData(float value) {
        if (redListNum == 1 ) {
            redDataList1.add(value);
            redElemCount++;
            if (redElemCount % 128 == 0) {
                redDataList2.clear();
                redListNum = 2;
                redElemCount = 0;

                float beat;
                if (emergency){
                    beat = (float) Math.random();
                    Log.d(TAG, "Generated Emergency Red Beat -> " + beat);
                }
                else {
                    beat = SignalAnalyser.analyzeData(redDataList1);
                    Log.d(TAG, "Measured Red beat -> " + beat);
                }

                beatList.add(beat);
                return true;
            }
        }

        else if (redListNum == 2 ) {
            redDataList2.add(value);
            redElemCount++;
            if (redElemCount % 128 == 0) {
                redDataList1.clear();
                redListNum = 1;
                redElemCount = 0;

                float beat;
                if (emergency){
                    beat = (float) Math.random();
                    Log.d(TAG, "Generated Emergency beat -> " + beat);
                }
                else {
                    beat = SignalAnalyser.analyzeData(redDataList2);
                    Log.d(TAG, "Measured IR beat -> " + beat);
                }

                beatList.add(beat);
                return true;
            }
        }

        return false;
    }

    /**
     * Set the value of the emergency flag.
     * If true it simulate a medical emergency with low beat rate and discard sensor data.
     * If false hande sensor data normally.
     * The default value is false.
     *
     * @param isEmergency the value to be set to the flag.
     */
    public void setEmergency(boolean isEmergency) {
        emergency = isEmergency;
    }
}
