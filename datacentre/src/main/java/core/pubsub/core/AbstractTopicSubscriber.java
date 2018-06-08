package core.pubsub.core;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Base subscriber class for publish/subscribe communication.
 * It handle the basic configuration for RabbitMQ client and offer a method for specify behaviours when message is received.
 *
 * @author manuBottax
 *         modify by Giulia Lucchi
 */
public abstract class AbstractTopicSubscriber {

    private String exchangeName;
    private String queueName;
    private List<String> topicBindKey;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;


    /**
     * Default constructor for class AbstractTopicSubscriber.
     * It use localhost as host for the message broker server.
     *
     * @param queueName - the name of the queue in which save data.
     *                  N.B. it has to be unique for every instance of the publisher     *
     * @param exchangeName - the name of the folder to subscribe (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param topicKey - the specific topic (filter rule) for the message to be received ( e.g. "advice.*"). "#" allow to receive every message.
     */
    public AbstractTopicSubscriber(String queueName, String exchangeName, List<String> topicKey) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.topicBindKey = topicKey;
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        this.mqttSetup();
    }

    /**
     * Default constructor for class AbstractTopicSubscriber.
     *  @param queueName - the name of the queue in which save data.
     *                  N.B. it has to be unique for every instance of the publisher
     * @param exchangeName - the name of the folder to subscribe (e.g. "advice").
     *                     N.B. It has to be the same in both publisher and subscriber.
     * @param topicKey - the specific topic (filter rule) for the message to be received ( e.g. "advice.*"). "#" allow to receive every message.
     * @param hostIP - the IP String of the host of the message broker server.
     */
    public AbstractTopicSubscriber(String queueName, String exchangeName, List<String> topicKey, String hostIP, int port) {
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.topicBindKey = topicKey;
        this.factory = new ConnectionFactory();
        this.factory.setHost(hostIP);
        this.factory.setPort(port);
        this.factory.setUsername("admin");
        this.factory.setPassword("exchange");
        this.mqttSetup();
    }

    /**
     * Define the behaviour of the subscriber when receive a message.
     *
     * @param behaviour - the behaviour of the subscriber when a message is received.
     */
    public void setBehaviour(SubscriberBehaviour behaviour){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
           //   behaviour.handleMessage(message, envelope.getRoutingKey());

            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }

    /**
     * Method for closing the connection to the RabbitMQ server.
     * After invoking this the class cannot receive more message from folder.
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

    /**
     * Method that return the total number of message received by this object.
     * Useful for testing purpose.
     *
     * @return the number of received message.
     */
   /* public int getReceivedMessage (){
        return this.receivedMessage;
    }*/

    private void mqttSetup() {
        try {
            this.connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
            //channel.basicQos(1);
            channel.queueDeclare(this.queueName, true, false, false, null);
            this.topicBindKey.forEach(bindKey ->{
                try {
                    channel.queueBind(this.queueName, this.exchangeName, bindKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
    }


}
