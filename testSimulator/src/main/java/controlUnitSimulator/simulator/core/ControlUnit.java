package controlUnitSimulator.simulator.core;

import controlUnitSimulator.simulator.mqtt.PatientDataPublisher;
import core.SensorType;
import core.UserRole;
import core.dbmanager.associations.AssociationsManager;
import core.dbmanager.associations.AssociationsManagerImpl;
import core.dbmanager.associations.PatientManager;
import core.dbmanager.associations.PatientManagerImpl;
import core.dbmanager.h2dbManager.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to simulate the behaviour of a single Control Unit.
 * Every control unit has a certain amount of simulated patient associated with one or multiple doctors (1-3).
 *
 * N.B.: For simplicity only temperature-related messages are handled in the current version.
 *
 * @author ManuBottax
 */
public class ControlUnit extends Thread{

    private static final int ASSOCIATION_AMOUNT = 3;

    private List<Patient> patientList;
    private List<PatientDataPublisher> publisherList;
    private PatientManager patientManager;
    private AssociationsManager associationsManager;
    private UserManager h2dbManager;
    private DataSensorManager h2SensorManager;

    private boolean run = false;
    private boolean stop = false;

    /**
     * Create a single simulated control unit with a specific amount of patients.
     * Every patient is created and associated with the doctor in H2 environment.
     *
     * @param patients - The amount of patients in thi control unit.
     * @param countFrom - A progressive value used for having unique ID for the patient also in different control unit.
     * @param lastDoctorIndex - The index of the last doctors created that can be associated with patient.
     */
    public ControlUnit(int patients, int countFrom, int lastDoctorIndex){

        this.patientList = new ArrayList<>();
        this.publisherList = new ArrayList<>();
        this.patientManager = new PatientManagerImpl();
        this.associationsManager = new AssociationsManagerImpl();
        this.h2dbManager = new UserManagerImpl();
        this.h2SensorManager = new DataSensorManagerImpl();

        for (int i = countFrom; i < countFrom + patients; i ++){
            String patientId = "patient.test." + i;
            Patient patient = new Patient(patientId);
            PatientDataPublisher publisher = new PatientDataPublisher(patientId);

            publisherList.add(publisher);
            // add patient to application db
            h2dbManager.registration(new User(patientId,"test", "test", "patient" + i, "TESTTESTTEST", "123456789", "test@test.com", UserRole.PATIENT.getRole()));
            // add patient to association db
            patientManager.createNewUser(patientId, "patient" , "test" + i , "TSTTST12A34B345C");
            try {
                // associate patient with some random doctors.
                // N.B.: If attempt to associate with a doctor already associated to him nothing happened, so this can
                // generate 1 to 3 different association randomly.
                for( int j = 0; j < ASSOCIATION_AMOUNT ; j++ ) {
                    int id = (int) (Math.random() * lastDoctorIndex);
                    String doctorID;
                    if ( id != 0) {
                        doctorID = "test.doctor" + id;
                    }
                    else {
                        doctorID = "test.doctor";
                    }
                    System.out.println("Associating with doctor " + doctorID);
                    patient.addDoctor(doctorID);
                    associationsManager.createNewAssociation(patientId, doctorID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Adding Temperature Sensor to " + patientId + " !");
            boolean isAssociated = h2SensorManager.addNewSensorType(patientId, SensorType.TEMPERATURE);
            System.out.println("Associated ? " + isAssociated);

            patientList.add(patient);
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
                        try {
                            Thread.sleep(((long)(Math.random() * 3000)));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try {
                    // wait some time before send new message.
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[CONTROL UNIT] STOPPING, going to clear");
            clearDB();
    }

    /**
     * Generate fake message to publish to data center.
     * @param patientId - the id of the patient that send that message.
     * @return the message generated. (json format string)
     */
    private String generateFakeMessage(String patientId){

        double value = ((Math.random() * 7) + 36);
        int level;
        String description;

        if (value >= 33 && value < 37 ){
            level = 1;
            description = "Temperature ok";
        } else if ( value >= 37 && value <= 41) {
            level = 2;
            description = "Warning: High Temperature.";
        } else {
            level = 3;
            description = "Emergency: Critical Temperature.";
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

    /**
     * Delete
     */
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
