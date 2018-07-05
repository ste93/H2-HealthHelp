package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.AdviceRequestMessage;
import org.json.JSONArray;

import java.util.Optional;

public class MultiAdvicePublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "adviceRequest";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private H2dbManager h2dbManage = new H2dbManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);

    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(AdviceRequestMessage.class, message -> {
            JSONArray values = h2dbManage.getAdvices(message.getPatientId(), Optional.of(message.getStart()), Optional.of(message.getEnd()));
            publisher.publishMessage(values.toString(), "patient."+message.getPatientId()+".receive.advice");
        }).build();
    }

}
