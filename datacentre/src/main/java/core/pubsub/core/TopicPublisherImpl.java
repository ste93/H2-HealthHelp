package core.pubsub.core;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Base subscriber class for publish/subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 *
 * @author RabbitMQ documentation
 *          modify by Manuel Bottazzi
 */
public class TopicPublisherImpl implements TopicPublisher {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    /**
     * Default constructor for class TopicPublisherImpl.
     * It use localhost as host for the message broker server.
     *
     * @param exchangeName - the name of the folder to publish in (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     */
    public TopicPublisherImpl(String exchangeName) {
        this.exchangeName = exchangeName;
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        mqttSetup();
    }

    /**
     * Default constructor for class TopicPublisherImpl.
     *
     * @param exchangeName - the name of the folder to publish in (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param hostIP - the IP String of the host of the message broker server.
     */
    public TopicPublisherImpl(String exchangeName, String hostIP, int port) {
        this.exchangeName = exchangeName;
        this.factory = new ConnectionFactory();
        this.factory.setHost(hostIP);
        this.factory.setPort(port);
        this.factory.setUsername("guest");
        this.factory.setPassword("guest");
        mqttSetup();
    }

    /**
     * method to publish a message.
     * @param message - the message to be sent.
     * @param routingKey - the specific topic in which publish the message.
     */
    @Override
    public void publishMessage(String message, String routingKey){
        try {
            channel.basicPublish(this.exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        } catch (IOException e) {
            System.err.println("Error during message publishing");
            e.printStackTrace();
        }
    }

    private void mqttSetup() {
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
    }
}
