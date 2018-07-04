package controlUnitSimulator.simulator;

import javax.swing.*;
import java.awt.*;

import controlUnitSimulator.simulator.core.ControlUnit;
import controlUnitSimulator.simulator.dbmanager.associations.DoctorManager;
import controlUnitSimulator.simulator.dbmanager.associations.DoctorManagerImpl;

import java.util.ArrayList;
import java.util.List;

public class controlUnitSimulator {

    private static List<ControlUnit> controlUnitList = new ArrayList<>();

    private static final String STATUS_WAITING = "Waiting for input parameters. Press 'Start' to run simulation";
    private static final String STATUS_INITIALIZING = "Initializing... WAIT FOR SIMULATOR TO GET READY...";
    private static final String STATUS_READY = "Ready";
    private static final String STATUS_RUNNING = "Running";
    private static final String STATUS_CLOSING = "Closing";

    private static boolean running = false;

    private static JLabel statusValueLabel;
    private static JButton startButton;
    private static JButton stopButton;

    private static boolean firstRun = true;

    private static DoctorManager doctorManager;
    private static List<String> doctorList;

    public static void main(String[] args) {

        doctorManager = new DoctorManagerImpl();
        doctorList = new ArrayList<>();

        ////// GUI ///////////////////////////////////////////////
        JFrame frame = new JFrame("H2 Test Simulator GUI");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500,300);

        JPanel statusPanel = new JPanel(new FlowLayout());
        JLabel statusLabel = new JLabel("Status : ");
        statusValueLabel = new JLabel(STATUS_WAITING);

        statusPanel.add(statusLabel);
        statusPanel.add(statusValueLabel);

        JPanel configPanel = new JPanel(new FlowLayout());
        JLabel controlUnitLabel = new JLabel("N° of Control Unit  ");
        JSpinner controlUnitSpinner = new JSpinner(new SpinnerNumberModel(10,1,10000,1));
        JLabel patientLabel = new JLabel("N° of patient per Control Unit  ");
        JSpinner patientSpinner = new JSpinner(new SpinnerNumberModel(10,1,100,1));

        configPanel.add(controlUnitLabel);
        configPanel.add(controlUnitSpinner);
        configPanel.add(patientLabel);
        configPanel.add(patientSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        startButton = new JButton("Start");
        startButton.addActionListener((e) -> {

            if(firstRun){
                int controlUnit = (int) controlUnitSpinner.getValue();
                int patient = (int) patientSpinner.getValue();
                statusValueLabel.setText(STATUS_INITIALIZING);
                patientSpinner.setEnabled(false);
                controlUnitSpinner.setEnabled(false);
                SwingUtilities.invokeLater(() -> initializeSimulator(controlUnit, patient));
                //TODO: perchè non lo visualizza ???? -> concorrenza !

            } else {
                restartSimulation();
            }
        });

        stopButton = new JButton("Stop & Close");
        stopButton.addActionListener((e) -> stopSimulation());

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        frame.add(statusPanel, BorderLayout.PAGE_START);
        frame.add(configPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);

        ///////////////////////////////////////////////////////////////////////////////7
    }

    private static void initializeSimulator(int controlUnitNumber, int patientPerControlUnit){
        if (firstRun) {
            System.out.println(" --- Initializing simulator for " + controlUnitNumber + " control Unit with " + patientPerControlUnit + " patients each --- ");
            System.out.println(" --- Total : " + (controlUnitNumber * patientPerControlUnit) + " patients --- ");
            String doctorID = "test.doctor";

            for (int i = 0; i < controlUnitNumber; i ++){
                System.out.println("Control Unit n° " + i);
                System.out.println("Creating doctor : " + doctorID);
                doctorManager.createNewUser(doctorID, "doctor", "test", "TESTTESTTEST");
                doctorList.add(doctorID);
                ControlUnit cu = new ControlUnit(patientPerControlUnit, i * patientPerControlUnit, doctorID);
                cu.setName("Control Unit " + i);
                controlUnitList.add(cu);
                doctorID = "test.doctor" + (i+1);
            }

            System.out.println(" --- Initialization completed, starting simulation --- ");
            statusValueLabel.setText(STATUS_RUNNING);
            stopButton.setEnabled(true);
            startButton.setText("Pause");
            running = true;
            for (ControlUnit cu : controlUnitList) {
                cu.start();
            }
            firstRun = false;
        }
    }

    private static void restartSimulation() {

        if (!running) {
            statusValueLabel.setText(STATUS_RUNNING);
            stopButton.setEnabled(true);
            startButton.setText("Pause");
            running = true;
            for (ControlUnit cu : controlUnitList) {
                cu.restartSimulation();
            }

        } else {
            statusValueLabel.setText(STATUS_READY);
            stopButton.setEnabled(true);
            startButton.setText("Start");
            running = false;
            for (ControlUnit cu : controlUnitList) {
                cu.pauseSimulation();
            }
        }
    }

    private static void stopSimulation(){
        statusValueLabel.setText(STATUS_CLOSING);
        stopButton.setEnabled(false);
        startButton.setEnabled(false);
        int i = 0;
        for( ControlUnit cu : controlUnitList ){
            cu.stopSimulation();
            System.out.println("[MAIN] Stopping Simulation Thread " + i + "!");
            i++;
        }

        for (ControlUnit cu : controlUnitList) {
            try {
                cu.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[MAIN] Thread " + cu.getName() + " terminated ");
        }

        //TODO : stop after all the clear is done.
        /*try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("[MAIN] Deleting Test Doctor");
        for (String doctor : doctorList) {
            doctorManager.deleteUser(doctor);
        }
        System.out.println("[MAIN] Deletion Completed");

        System.out.println("[MAIN] Closing");
        System.exit(0);
    }
}
