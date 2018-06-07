package core.pubsub.subscriber;

import akka.actor.AbstractActor;

public class AdviceRequestActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        AdviceRequest subscriber = new AdviceRequest();
        System.out.println(" ----->AdviceReceiver STARTED.");
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().build();
    }

}
