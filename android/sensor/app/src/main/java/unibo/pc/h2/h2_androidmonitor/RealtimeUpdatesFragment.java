package unibo.pc.h2.h2_androidmonitor;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple fragment to handle chart representation of data read from the sensors.
 * This implementation handle two data set, the first one for Infrared data and the latter for Red Light sensor.
 *
 * @author ManuBottax
 * @version 0.1 - May 2018
 */
public class RealtimeUpdatesFragment extends Fragment {

    private List<DataPoint> dataset;
    private LineGraphSeries<DataPoint> redData;
    private LineGraphSeries<DataPoint> IRData;

    private double lastRedValX = 2d;
    private double lastIRValX = 2d;

    private GraphView graph;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        dataset = new ArrayList<>();

        graph = (GraphView) rootView.findViewById(R.id.graph);

        IRData = new LineGraphSeries<>();
        IRData.setAnimated(true);
        IRData.setDrawAsPath(true);

        redData = new LineGraphSeries<>();
        redData.setAnimated(true);
        redData.setDrawAsPath(true);
        redData.setColor(Color.RED);

        graph.addSeries(redData);
        graph.addSeries(IRData);
        graph.setTitle("HRM data Graph");

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Add a data to the Red Light dataset.
     * @param valy the value to be added.
     */
    public void addRedData(double valy) {
        redData.appendData(new DataPoint(lastRedValX,valy),false, 100000);
        lastRedValX ++;
    }

    /**
     * Add a data to the Infrared dataset.
     * @param valy the value to be added.
     */
    public void addIRData(double valy) {
        IRData.appendData(new DataPoint(lastIRValX,valy),false, 100000);
        lastIRValX ++;
    }

    /**
     * Reset the chart and delete all previuos plotted data.
     */
    public void reset() {
        graph.removeAllSeries();

        IRData = new LineGraphSeries<>();
        IRData.setAnimated(true);
        IRData.setDrawAsPath(true);

        redData = new LineGraphSeries<>();
        redData.setAnimated(true);
        redData.setDrawAsPath(true);
        redData.setColor(Color.RED);

        graph.addSeries(redData);
        graph.addSeries(IRData);
    }
}
