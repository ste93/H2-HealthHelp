package core.pubsub;

import akka.actor.*;
import core.pubsub.subscriber.AdviceReceiverActor;
import core.pubsub.subscriber.PatientDataReceiverActor;
import core.pubsub.subscriber.RequestReceiverActor;

/**
 * Starts all the actors that communicate in the data centre.
 *
 * @author Giulia Lucchi
 */
public class MainActor extends AbstractActor {

    private ActorSystem actorSystem = ActorSystem.create("datacentre");

    @Override
    public void preStart() throws Exception {
        super.preStart();
        final ActorRef patientDataReceiverActor = actorSystem.actorOf(Props.create(PatientDataReceiverActor.class), "patientDataReceiver");
        final ActorRef adviceReceiverActor = actorSystem.actorOf(Props.create(AdviceReceiverActor.class), "adviceReceiver");
        final ActorRef requestReceiverActor = actorSystem.actorOf(Props.create(RequestReceiverActor.class), "requestReceiver");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}