package core;

import akka.actor.AbstractActor;
import core.pubsub.core.PatientDataReceiver;

/**
 * Created by lucch on 01/06/2018.
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
