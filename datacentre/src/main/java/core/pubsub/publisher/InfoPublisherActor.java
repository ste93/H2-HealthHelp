package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.UserMessage;

/**
 * Sends the personal information to the requester.
 *
 * @author Giulia Lucchi
 */
public class InfoPublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "receive.info";
    private static final String HOST_IP = "213.209.230.94";
    private static final String ROUTING_KEY = "datacentre.receive.info";
    private static final int PORT = 8088;

    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(UserMessage.class, userMessage -> {
            publisher.publishMessage(userMessage.toJson(), ROUTING_KEY);
        }).build();
    }
}
