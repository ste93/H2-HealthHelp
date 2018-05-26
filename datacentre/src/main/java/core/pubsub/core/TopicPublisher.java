package core.pubsub.core;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Base subscriber class for publish/subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 */
public class TopicPublisher {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    /**
     * Default constructor for class TopicPublisher.
     * It use localhost as host for the message broker server.
     *
     * @param exchangeName - the name of the folder to publish in (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     */
    public TopicPublisher(String exchangeName) {
        this.exchangeName = exchangeName;
        mqttSetup();
        this.factory.setHost("localhost");
    }

    /**
     * Default constructor for class TopicPublisher.
     *
     * @param exchangeName - the name of the folder to publish in (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param hostIP - the IP String of the host of the message broker server.
     */
    public TopicPublisher(String exchangeName, String hostIP) {
        this.exchangeName = exchangeName;
        mqttSetup();
        this.factory.setHost(hostIP);
    }

    /**
     * method to publish a message.
     * @param message - the message to be sent.
     * @param routingKey - the specific topic in which publish the message.
     */
    public void publishMessage(String message, String routingKey ){
        try {
            channel.basicPublish(this.exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        } catch (IOException e) {
            System.err.println("Error during message publishing");
            e.printStackTrace();
        }
    }

    /**
     * method for closing the connection to the RabbitMQ server.
     * After invoking this the class cannot publish more message on the topic.
     */
    public void close() {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            System.err.println("Error during closing operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during closing operation");
            e.printStackTrace();
        }
    }

    private void mqttSetup() {
        this.factory = new ConnectionFactory();
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
