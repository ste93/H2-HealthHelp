package core.pubsub;

import akka.actor.*;
import core.pubsub.publisher.*;
import core.pubsub.subscriber.*;

/**
 * Starts all the actors that communicate in the data centre.
 *
 * @author Giulia Lucchi
 */
public class MainActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();

        System.out.println(" DATA CENTER configuration and launching");
        System.out.println(" -------- Start Subscriber Actor -------");

        /* Create Subscriber Actor */
        final ActorRef patientDataReceiverActor = getContext().actorOf(Props.create(PatientDataReceiverActor.class), "patientDataReceiver");
        final ActorRef adviceReceiverActor = getContext().actorOf(Props.create(AdviceReceiverActor.class), "adviceReceiver");
        final ActorRef requestReceiverActor = getContext().actorOf(Props.create(PatientDataRequestActor.class), "requestReceiver");
        final ActorRef adviceRequestActor = getContext().actorOf(Props.create(AdviceRequestActor.class), "adviceRequester");
        final ActorRef drugReceiverActor = getContext().actorOf(Props.create(DrugReceiverActor.class), "drugReceiverActor");
        final ActorRef drugRequesterActor = getContext().actorOf(Props.create(DrugRequestActor.class), "drugRequesterActor");
        final ActorRef infoReceiveActor = getContext().actorOf(Props.create(InfoRequestActor.class), "infoReceiveActor");

        System.out.println(" -------- Start Publisher Actor -------");

        /* Create Publisher Actor */
        final ActorRef advicePublisherActor = getContext().actorOf(Props.create(core.pubsub.publisher.AdvicePublisherActor.class), "advicePublisherActor");
        final ActorRef historyPublisherActor = getContext().actorOf(Props.create(HistoryPublisherActor.class), "historyPublisherActor");
        final ActorRef infoPublisherActor = getContext().actorOf(Props.create(InfoPublisherActor.class), "infoPublisherActor");
        final ActorRef levelPublisherActor = getContext().actorOf(Props.create(LevelPublisherActor.class), "levelPublisherActor");
        final ActorRef multiAdvicePublisherActor = getContext().actorOf(Props.create(MultiAdvicePublisherActor.class), "multiAdvicePublisherActor");
        final ActorRef multiDrugPublisherActor = getContext().actorOf(Props.create(MultiDrugPublisherActor.class), "multiDrugPublisherActor");

        System.out.println(" -------- Finished  -------");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
