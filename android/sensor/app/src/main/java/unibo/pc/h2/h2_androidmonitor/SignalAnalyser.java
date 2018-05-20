package unibo.pc.h2.h2_androidmonitor;

import android.util.Log;
import java.util.List;
import uk.me.berndporr.iirj.Butterworth;
import static android.content.ContentValues.TAG;

/**
 * Simple signal analyser capable to find heart beat from sensor's data.
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class SignalAnalyser {

    private static final int SEGMENT_LENGTH = 128;

    /**
     * Calculate heart beat from a list of data read from a sensor.
     * @param dataList a list of data measured by sensor. it should be at least 128 value.
     * @return the value of the heart beat
     */
    public static float analyzeData( List<Float> dataList) {

        Log.d(TAG, "analyzeData: data collected -> " + dataList.size());
        Log.d(TAG, "analyzeData: min  ->" + getMinimum(dataList));
        Log.d(TAG, "analyzeData: max  ->" + getMax(dataList));


        double[] sample = new double[SEGMENT_LENGTH];
        for (int i = 0; i < SEGMENT_LENGTH; i++) {
            sample[i] = dataList.get(i);
        }

        double[] hamming = new double[SEGMENT_LENGTH];

        for (int i = 0; i < SEGMENT_LENGTH; i++) {
            hamming[i] = (float) ((0.54 - (0.46 * Math.cos(Math.PI * 2 * (double) i / (double) (SEGMENT_LENGTH - 1)))));
        }

        double[] im = new double[SEGMENT_LENGTH];
        double[] re = new double[SEGMENT_LENGTH];

        for (int i = 0; i < SEGMENT_LENGTH; i++) {
            re[i] = dataList.get(i) * hamming[i];
        }

        FFT fft = new FFT(re.length);
        fft.fft(re, im);

        Butterworth filter = new Butterworth();

        filter.bandPass(2, 125, 2, 0.5);

        for (int i = 0; i < SEGMENT_LENGTH; i++) {
            re[i] = filter.filter(re[i]);
            im[i] = filter.filter(im[i]);
        }

        double[] fftMagnitude = fftMagnitude(re, im);

        double peak = Double.MIN_VALUE;
        for (int i = 0; i < 128; i++) {
            if (fftMagnitude[i] > peak) {
                peak = fftMagnitude[i];
            }
        }

        double beat = getBeat(peak);

        Log.d(TAG, "analyzeData: peak : " + peak);
        Log.d(TAG, "analyzeData: beat : " + beat);
        Log.d(TAG, "analyzeData: analisi terminata");

        return (float) beat;
    }

    private static double getBeat(double peak) {
        double s = 57.14;
        return (peak / 100000) * s;
    }

    private static float getMinimum(List<Float> list){
        float min = Float.MAX_VALUE;
        for (float f : list) {
            if ( f < min ){
                min = f;
            }
        }
        return min;
    }

    private static float getMax(List<Float> list){
        float max = Float.MIN_VALUE;
        for (float f : list) {
            if ( f > max ){
                max = f;
            }
        }
        return max;
    }

    private static double[] fftMagnitude(double[] re, double[] im) {
        if (re.length != im.length)
            return null;
        double[] fftMag = new double[re.length];
        for (int i = 0; i < re.length; i++) {
            fftMag[i] = Math.sqrt(Math.pow(re[i], 2) + Math.pow(im[i], 2));
        }
        return fftMag;
    }
}
