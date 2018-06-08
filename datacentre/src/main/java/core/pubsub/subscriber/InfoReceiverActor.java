package core.pubsub.subscriber;

import akka.actor.AbstractActor;

/**
 * Created by lucch on 08/06/2018.
 */
public class InfoReceiverActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        InfoReceiver infoReceiver = new InfoReceiver();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
