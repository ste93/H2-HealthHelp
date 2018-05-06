package core.pub_sub.example;

import core.pub_sub.AbstractTopicSubscriber;
import core.pub_sub.SubscriberBehaviour;

public class ClassTopicSubscriber extends AbstractTopicSubscriber {

    private AnotherClass c = new AnotherClass();
    private String testString = "[ myClass ] ";


    private class myBehaviour implements SubscriberBehaviour {

        public void handleMessage(String message) {
            System.out.print(testString);
            c.doSomethingWithData(message);
        }
    }


    public ClassTopicSubscriber(String queueName, String exchangeName, String topicKey) {
        super(queueName, exchangeName, topicKey);
        this.setBehaviour(new myBehaviour());
    }

    public ClassTopicSubscriber(String queueName, String exchangeName, String topicKey, String hostIP) {
        super(queueName, exchangeName, topicKey, hostIP);
        this.setBehaviour(new myBehaviour());
    }
}
