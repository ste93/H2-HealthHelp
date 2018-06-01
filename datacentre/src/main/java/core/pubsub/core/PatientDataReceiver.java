package core.pubsub.core;

import akka.actor.AbstractActor;

/**
 * Simple message subscriber for pub/sub communication for patient data.
 *
 * @author manuBottax
 */
public class PatientDataReceiver extends AbstractTopicSubscriber{

    private static final String QUEUE_NAME = "patientData.queue";
    private static final String EXCHANGE_NAME = "patientData";
    private static final String ROUTING_KEY = "datacentre.receive.patientdata";

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private SubscriberBehaviour behaviour = x -> {
        System.out.println("[PatientDataReceiver] " + x + " .");
    };

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public PatientDataReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

}