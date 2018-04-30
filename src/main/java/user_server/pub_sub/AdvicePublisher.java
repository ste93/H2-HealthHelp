package user_server.pub_sub;

import core.pub_sub.TopicPublisher;

/**
 * Simple message publisher for pub/sub communication of doctor's advice.
 *
 * @author manuBottax
 */
public class AdvicePublisher {

    private static final String EXCHANGE_NAME = "advice";
    private static final String ROUTING_KEY = "datacentre.receive.advice";

    private TopicPublisher publisher;
    private String patientID;

    /**
     * Default constructor for the AdvicePublisher class.
     *
     * @param patientID - The ID of the patient.
     */
    public AdvicePublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new TopicPublisher(EXCHANGE_NAME);
    }

    /**
     * publish some data of the patient to the folder.
     *
     * @param advice - the advice string to be sent to the folder.
     */
    public void sendAdvice(String advice) {
        this.publisher.publishMessage(this.patientID + "@" + advice, ROUTING_KEY);
    }

    /**
     * method for closing the connection to the message broker.
     * After invoking this the class cannot publish more message on the topic.
     */
    public void close() {
        this.publisher.close();
    }

}