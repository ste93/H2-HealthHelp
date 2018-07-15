package controlUnitSimulator.simulator.mqtt;

/**
 * Simple message publisher for pub/sub communication of patient's data.
 *
 * @author manuBottax
 */
public class PatientDataPublisher {

    private static final String EXCHANGE_NAME = "patientData";
    private static final String ROUTING_KEY = "datacentre.receive.patientdata";

    private static final String REMOTE_HOST_IP = "2.34.233.198";
    private static final int REMOTE_HOST_PORT = 8088;

    private TopicPublisher publisher;
    private String patientID;

    /**
     * Default constructor for the PatientDataPublisher class.
     *
     * @param patientID - The ID of the patient whose data are measured.
     */
    public PatientDataPublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new TopicPublisher(EXCHANGE_NAME, REMOTE_HOST_IP, REMOTE_HOST_PORT);
    }

    /**
     * publish some data of the patient to the folder.
     *
     * @param data - the data string to be sent to the folder.
     */
    public void sendData(String data) {
        this.publisher.publishMessage(data, ROUTING_KEY);
    }

    /**
     * method for closing the connection to the message broker.
     * After invoking this the class cannot publish more message on the topic.
     */
    public void close() {
        this.publisher.close();
    }

    public String getPatientId() {
        return this.patientID;
    }
}
