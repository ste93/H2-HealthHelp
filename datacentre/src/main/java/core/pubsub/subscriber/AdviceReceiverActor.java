package core.pubsub.subscriber;

import akka.actor.AbstractActor;

/**
 * Created by lucch on 05/06/2018.
 */
public class AdviceReceiverActor extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        AdviceReceiver subscriber = new AdviceReceiver();
        System.out.println(" ----->AdviceReceiver STARTED.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
