package core.pubsub.subscriber;

import akka.actor.AbstractActor;

/**
 * Created by lucch on 05/06/2018.
 */
public class RequestReceiverActor extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        RequestReceiver subscriber = new RequestReceiver();
        System.out.println(" ----->RequestRequesterReceiver STARTED.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
