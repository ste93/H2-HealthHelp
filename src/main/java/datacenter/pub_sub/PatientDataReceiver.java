package datacenter.pub_sub;

import core.pub_sub.SubscriberBehaviour;
import core.pub_sub.TopicSubscriber;

/**
 * Simple message subscriber for pub/sub communication for patient data.
 *
 * @author manuBottax
 */
public class PatientDataReceiver extends TopicSubscriber {

    private static final String EXCHANGE_NAME = "patientData";
    private static final String ROUTING_KEY = "datacentre.receive.patientdata";

    private SubscriberBehaviour behaviour = x -> {
        System.out.println("[PatientDataReceiver] Do something with my data ( " + x + " ).");
    };

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public PatientDataReceiver() {
        super(EXCHANGE_NAME, ROUTING_KEY );
        this.setBehaviour(behaviour);
    }
}
