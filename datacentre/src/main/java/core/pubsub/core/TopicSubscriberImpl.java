package core.pubsub.core;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Base subscriber class for subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 *
 * @author RabbitMQ documentation
 *          modify by   Giulia Lucchi
 *                      Margherita Pecorelli
 */
public class TopicSubscriberImpl implements TopicSubscriber {

    private String exchangeName;
    private String queueName;
    private List<String> topicBindKey;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Consumer consumer;

    /**
     * Default constructor for class TopicPublisherImpl
     *
     * @param exchangeName - the name of the folder to subscribe in (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param queueName - the name of queue in witch the message are enqueue
     * @param topicBindKey - routing key in the topic model
     * @param hostIP - IP address of subscriber
     * @param port - port of subscriber
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public TopicSubscriberImpl(String exchangeName, String queueName, List<String> topicBindKey, String hostIP, int port) throws IOException, TimeoutException {
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

    @Override
    public Channel getChannel() {
        return channel;
    }

    /**
     * Set the consumer of the subscriber.
     *
     * @param consumer - element to process the messages.
     */
    @Override
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
        try {
            this.setUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configuration of exchange, channel and queue.
     *
     * @throws IOException
     */
    private void setUp() throws IOException {
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
