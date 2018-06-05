package core.pubsub.subscriber;

import akka.actor.*;
import core.pubsub.subscriber.PatientDataReceiver;

/**
 * Manages the communication between control unit and data centre.
 *
 * @author Giulia Lucchi
 */
public class PatientDataReceiverActor extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        PatientDataReceiver subscriber = new PatientDataReceiver();
        System.out.println(" ----->PatientDataReceiver STARTED.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
