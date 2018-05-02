package datacenter.pub_sub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pub_sub.SubscriberBehaviour;
import core.pub_sub.TopicSubscriber;

import java.io.IOException;

/**
 * Simple message subscriber for pub/sub communication for history request.
 *
 * @author manuBottax
 */
public class RequestReceiver extends TopicSubscriber {

    private static final String QUEUE_NAME = "historyRequest.queue";
    private static final String EXCHANGE_NAME = "historyRequest";
    private static final String ROUTING_KEY = "datacentre.request.history";

    private SubscriberBehaviour behaviour = x -> {
        System.out.println("[History Request Receiver] Do something with my history request : [ " + x + " ].");
    };

    /**
     * Default constructor for the RequestReceiver class.
     */
    public RequestReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        this.setBehaviour(behaviour);
    }
}
