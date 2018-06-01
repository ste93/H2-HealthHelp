package core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * Created by lucch on 01/06/2018.
 */
public class MainActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        final ActorRef patientDataReceiver = getContext().actorOf(Props.create(PatientDataReceiverActor.class), "patientDataReceiver");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
