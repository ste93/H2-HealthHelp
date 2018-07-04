package controlUnitSimulator.simulator.core;


import controlUnitSimulator.simulator.dbmanager.associations.AssociationsManager;
import controlUnitSimulator.simulator.dbmanager.associations.AssociationsManagerImpl;
import controlUnitSimulator.simulator.dbmanager.associations.PatientManager;
import controlUnitSimulator.simulator.dbmanager.associations.PatientManagerImpl;
import controlUnitSimulator.simulator.mqtt.PatientDataPublisher;

import java.util.ArrayList;
import java.util.List;

public class ControlUnit extends Thread{

    private final String associatedDoctorID;

    private List<String> patientList;
    private List<PatientDataPublisher> publisherList;
    private PatientManager patientManager;
    private AssociationsManager associationsManager;

    private boolean run = false;
    private boolean stop = false;

    public ControlUnit(int patients, int countFrom, String doctorID){
        this.associatedDoctorID = doctorID;
        System.out.println("[CONTROL UNIT] This control unit is associated with doctor : " + this.associatedDoctorID);
        this.patientList = new ArrayList<>();
        this.publisherList = new ArrayList<>();
        this.patientManager = new PatientManagerImpl();
        this.associationsManager = new AssociationsManagerImpl();

        for (int i = countFrom; i < countFrom + patients; i ++){
            String patientId = "patient.test." + i;
            patientList.add(patientId);
            publisherList.add(new PatientDataPublisher(patientId));
            patientManager.createNewUser(patientId, "patient" , "test" + i , "TSTTST12A34B345C");
            try {
                associationsManager.createNewAssociation(patientId, this.associatedDoctorID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("[CONTROL UNIT] Patient created : " + patientId);
        }

        System.out.println(" --- Control Unit n° " + ((countFrom + 10) / 10 ) + " initialized ! --- ");

        run = true;
    }

    @Override
    public void run() {
        super.run();
        startSimulation();
    }

    private void startSimulation() {
            while (!stop) {
                if (run) {
                    for (PatientDataPublisher pub : publisherList) {
                        String message = generateFakeMessage(pub.getPatientId());
                        pub.sendData(message);
                    }
                }

                try {
                    Thread.sleep(((long)(Math.random() * 5000)) + 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[CONTROL UNIT] STOPPING, going to clear");
            clearDB();
    }

    private String generateFakeMessage(String patientId){

        double value = ((Math.random() * 7) + 36);
        int level;
        String description;

        if (value >= 33 && value < 37 ){
            level = 1;
            description = "everything ok";
        } else if ( value <= 41) {
            level = 2;
            description = "warning, something strange is happening";
        } else {
            level = 3;
            description = "just start digging";
        }

        String message =  "{\"type\" : " + "temperature" + ", " +
                "\"message\" : { " +
                    "\"patientID\" : " + patientId + "," +
                    "\"value\" : " + value + "," +
                    "\"unit\" : \"celsius\" , " +
                    "\"timestamp\" : " + "25/06/2018 17:30:00:132" + "," +
                    "\"output\" : {" +
                        "\"level\" : " + level + "," +
                        "\"description\" : " + description +
                    "}" +
                "}" +
                "}";

        System.out.println("[GENERATED MESSAGE] " + message);
        return message;
    }

    public void restartSimulation() {
        run = true;
    }

    public void stopSimulation() {
        stop = true;
    }

    public void pauseSimulation() {
        run = false;
    }

    private void clearDB() {
        System.out.println("[CONTROL UNIT] Cleaning up database !");
        for (String patient : patientList){
            try {
                associationsManager.deleteAssociation(patient, this.associatedDoctorID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[CONTROL UNIT] Association deleted");
        for (String patient : patientList){
            patientManager.deleteUser(patient);
        }
        System.out.println("[CONTROL UNIT] Patient deleted");
    }
}
