package core.pub_sub;

import com.rabbitmq.client.*;

import java.io.IOException;
public class BaseSubscriber  extends MqttTopicSubscriber{


    public BaseSubscriber(String exchangeName, String topicKey) {
        super(exchangeName, topicKey);
        this.setConsumer();
    }

    public BaseSubscriber(String exchangeName, String topicKey, String hostIP) {
        super(exchangeName, topicKey,hostIP);
        this.setConsumer();
    }

    @Override
    public void setConsumer (){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received from a Different Receiver ! message -> " + envelope.getRoutingKey() + " : ' " + message + " '");
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }

}
