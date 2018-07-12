package controlUnitSimulator.simulator.core;


import controlUnitSimulator.simulator.dbmanager.associations.AssociationsManager;
import controlUnitSimulator.simulator.dbmanager.associations.AssociationsManagerImpl;
import controlUnitSimulator.simulator.dbmanager.associations.PatientManager;
import controlUnitSimulator.simulator.dbmanager.associations.PatientManagerImpl;
import controlUnitSimulator.simulator.dbmanager.h2application.H2dbManager;
import controlUnitSimulator.simulator.dbmanager.h2application.H2dbManagerImpl;
import controlUnitSimulator.simulator.dbmanager.h2application.User;
import controlUnitSimulator.simulator.dbmanager.h2application.UserRole;
import controlUnitSimulator.simulator.mqtt.PatientDataPublisher;

import java.util.ArrayList;
import java.util.List;

public class ControlUnit extends Thread{

    private static final int ASSOCIATION_AMOUNT = 3;

    private List<Patient> patientList;
    private List<PatientDataPublisher> publisherList;
    private PatientManager patientManager;
    private AssociationsManager associationsManager;
    private H2dbManager h2dbManager;

    private boolean run = false;
    private boolean stop = false;

    public ControlUnit(int patients, int countFrom, int lastDoctorIndex){

        this.patientList = new ArrayList<>();
        this.publisherList = new ArrayList<>();
        this.patientManager = new PatientManagerImpl();
        this.associationsManager = new AssociationsManagerImpl();
        this.h2dbManager = new H2dbManagerImpl();

        for (int i = countFrom; i < countFrom + patients; i ++){
            String patientId = "patient.test." + i;
            Patient p = new Patient(patientId);

            publisherList.add(new PatientDataPublisher(patientId));
            // add patient to application db
            h2dbManager.registration(new User(patientId,"test", "test", "patient" + i, "TESTTESTTEST", "123456789", "test@test.com", UserRole.PATIENT.getRole()));
            // add patient to association
            patientManager.createNewUser(patientId, "patient" , "test" + i , "TSTTST12A34B345C");
            try {
                for( int j = 0; j < ASSOCIATION_AMOUNT ; j++ ) {
                    String doctorID = "test.doctor" + (int) (Math.random() * lastDoctorIndex);
                    System.out.println("Associating with doctor " + doctorID);
                    p.addDoctor(doctorID);
                    associationsManager.createNewAssociation(patientId, doctorID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            patientList.add(p);
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
                    Thread.sleep(((long)(Math.random() * 5000)) + 10000);
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

        String message =  "{\"type\" : \"" + "temperature" + "\", " +
                "\"message\" : { " +
                    "\"patientId\" : \"" + patientId + "\"," +
                    "\"value\" : \"" + value + "\"," +
                    "\"unit\" : \"celsius\" , " +
                    "\"timestamp\" : \"" + "25/06/2018 17:30:00:132" + "\"," +
                    "\"output\" : {" +
                        "\"level\" : \"" + level + "\" ," +
                        "\"description\" : \"" + description + "\"" +
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
        for (Patient patient : patientList){
            try {
                for ( String doctor : patient.getAssociatedDoctor()) {
                    associationsManager.deleteAssociation(patient.getPatientID(), doctor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[CONTROL UNIT] Association deleted");
        for (Patient patient : patientList){
            patientManager.deleteUser(patient.getPatientID());
            h2dbManager.deleteUser(patient.getPatientID(), UserRole.PATIENT);
        }
        System.out.println("[CONTROL UNIT] Patient deleted");
    }
}
