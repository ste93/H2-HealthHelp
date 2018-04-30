package user_server.pub_sub;

import core.pub_sub.TopicPublisher;

/**
 * Simple message publisher for pub/sub communication for history request.
 *
 * @author manuBottax
 */
public class HistoryRequestPublisher {

    private static final String EXCHANGE_NAME = "historyRequest";
    private static final String ROUTING_KEY = "datacentre.request.history";

    private TopicPublisher publisher;
    private String patientID;

    /**
     * Default constructor for the HistoryRequestPublisher class.
     *
     * @param patientID - The ID of the patient.
     */
    public HistoryRequestPublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new TopicPublisher(EXCHANGE_NAME);
    }

    /**
     * publish some data of the patient to the folder.
     *
     * @param request - the request string to be sent to the folder.
     */
    public void sendRequest(String request) {
        this.publisher.publishMessage(this.patientID + "@" + request, ROUTING_KEY);
    }

    /**
     * method for closing the connection to the message broker.
     * After invoking this the class cannot publish more message on the topic.
     */
    public void close() {
        this.publisher.close();
    }


}
