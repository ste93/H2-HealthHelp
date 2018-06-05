package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.message.AdviceMessage;

/**
 * Created by lucch on 05/06/2018.
 */
public class AdvicePublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "advice";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private H2dbManager h2dbManage = new H2dbManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisher(EXCHANGE_NAME,HOST_IP,PORT);

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AdviceMessage.class, message -> {
            h2dbManage.addAdvice(message.getMessage());

            this.publisher.publishMessage(message.getMessage(), "patient."+message.getPatientId()+".receive.advice");
        }).build();
    }
}
