package core.pub_sub.example;

import core.pub_sub.SubscriberBehaviour;
import core.pub_sub.TopicSubscriber;

public class LambdaTopicSubsciber extends TopicSubscriber {

    private SubscriberBehaviour defaultBehaviour = x -> System.out.println(" [x] Received -> ' " + x  + " '");


    public LambdaTopicSubsciber(String queueName, String exchangeName, String topicKey) {
        super(queueName, exchangeName, topicKey);
        this.setBehaviour(defaultBehaviour);
    }

    public LambdaTopicSubsciber(String queueName, String exchangeName, String topicKey, String hostIP) {
        super(queueName, exchangeName, topicKey, hostIP);
        this.setBehaviour(defaultBehaviour);
    }
}
