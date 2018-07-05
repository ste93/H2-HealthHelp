package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.AdviceMessage;
import core.pubsub.message.MessagesUtils;

/**
 * Created by lucch on 05/06/2018.
 */
public class AdvicePublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "advice";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final H2dbManager h2dbManage = new H2dbManagerImpl();
    private final MessagesUtils utils = new MessagesUtils();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AdviceMessage.class, message -> {
            String messageToInsert = utils.convertToFormatApi(message.getMessage());
            h2dbManage.addAdvice(messageToInsert);
            this.publisher.publishMessage(message.getMessage(), "patient."+message.getPatientId()+".receive.advice");
        }).build();
    }
}
