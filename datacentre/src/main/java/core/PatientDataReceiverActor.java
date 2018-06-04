package core;

import akka.actor.*;
import core.pubsub.core.EmergencyMessage;
import core.pubsub.core.PatientDataReceiver;

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
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
