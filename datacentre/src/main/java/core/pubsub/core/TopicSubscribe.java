package core.pubsub.core;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class TopicSubscribe {

    private String exchangeName;
    private String queueName;
    private List<String> topicBindKey;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Consumer consumer;

    public TopicSubscribe(String exchangeName, String queueName, List<String> topicBindKey, String hostIP, int port) throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost(hostIP);
        factory.setUsername("admin");
        factory.setPassword("exchange");
        factory.setPort(port);
        connection = factory.newConnection();
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.topicBindKey = topicBindKey;
        channel = connection.createChannel();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
        try {
            this.seUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seUp() throws IOException {
        channel.exchangeDeclare(exchangeName, "topic");
        channel.queueDeclare(queueName, true, false, false, null);

        topicBindKey.forEach(bindKey -> {
            try {
                channel.queueBind(queueName, exchangeName, bindKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        channel.basicConsume(queueName, true, consumer);
    }
}
