package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.h2dbManager.AdviceManager;
import core.dbmanager.h2dbManager.AdviceManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.AdviceRequestMessage;
import org.json.JSONArray;

import java.util.Optional;

/**
 * Sends to the requester all advices relative to a specific patient on a time range.
 *
 * @author Margherita Pecorelli
 */
public class MultiAdvicePublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "adviceRequest";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private AdviceManager adviceManager = new AdviceManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(AdviceRequestMessage.class, message -> {

            JSONArray values = adviceManager.getAdvices(message.getPatientId(), Optional.of(message.getStart()), Optional.of(message.getEnd()));

            publisher.publishMessage(values.toString(), "patient."+message.getPatientId()+".receive.advice");

        }).build();
    }

}
