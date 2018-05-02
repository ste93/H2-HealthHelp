package datacenter.pub_sub;

import core.pub_sub.SubscriberBehaviour;
import core.pub_sub.TopicSubscriber;

/**
 * Simple message subscriber for pub/sub communication of doctor's advice.
 *
 * @author manuBottax
 */
public class AdviceReceiver extends TopicSubscriber {

    private static final String QUEUE_NAME = "advice.queue";
    private static final String EXCHANGE_NAME = "advice";
    private static final String ROUTING_KEY = "datacentre.receive.advice";

    private SubscriberBehaviour behaviour = x -> {
        System.out.println("[adviceReceiver] advice : [ " + x + " ].");
    };

    /**
     * Default constructor for the AdviceReceiver class.
     */
    public AdviceReceiver() {
        super(QUEUE_NAME,EXCHANGE_NAME, ROUTING_KEY);
        this.setBehaviour(behaviour);
    }
}