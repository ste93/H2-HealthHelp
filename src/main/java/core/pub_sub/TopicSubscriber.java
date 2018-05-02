package core.pub_sub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Base subscriber class for publish/subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 *
 * The method setBehaviour() has to be overridden in order to define a custom behaviour.
 *
 * @author manuBottax
 */
public abstract class TopicSubscriber {

    private String exchangeName;
    protected String queueName;
    private String topicBindKey;
    private ConnectionFactory factory;
    private Connection connection;
    protected Channel channel;

    //private SubscriberBehaviour defaultBehaviour = x -> System.out.println(" [x] Received -> ' " + x  + " '");

    /**
     * Default constructor for class TopicSubscriber.
     * It use localhost as host for the message broker server.
     *
     * @param exchangeName - the name of the folder to subscribe (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param topicKey - the specific topic (filter rule) for the message to be received ( e.g. "advice.*"). "#" allow to receive every message.
     */
    public TopicSubscriber(String exchangeName, String topicKey) {
        this.exchangeName = exchangeName;
        this.topicBindKey = topicKey;
        this.mqttSetup();
        this.factory.setHost("localhost");
        //this.setBehaviour(defaultBehaviour);
    }

    /**
     * Default constructor for class TopicSubscriber.
     *
     * @param exchangeName - the name of the folder to subscribe (e.g. "advice").
     *                     It has to be the same in both publisher and subscriber.
     * @param topicKey - the specific topic (filter rule) for the message to be received ( e.g. "advice.*"). "#" allow to receive every message.
     * @param hostIP - the IP String of the host of the message broker server.
     */
    public TopicSubscriber(String exchangeName, String topicKey, String hostIP) {
        this.exchangeName = exchangeName;
        this.topicBindKey = topicKey;
        this.mqttSetup();
        this.factory.setHost(hostIP);
        //this.setBehaviour(defaultBehaviour);
    }

    /**
     * Define the behaviour of the subscriber when receive a message.
     * By default it print the message received.
     * This method has to be redefined in order to achieve a custom behaviour.
     */
    public void setBehaviour(SubscriberBehaviour behaviour){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    behaviour.handleMessage(message);
                }
                finally {
                    //If the worker die before complete the message handling the message is send to another one.
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        try {
            channel.basicConsume(queueName, false, consumer);

        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }

    /**
     * method for closing the connection to the RabbitMQ server.
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

    private void mqttSetup() {

        this.factory = new ConnectionFactory();
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
            channel.basicQos(1);
            this.queueName = this.channel.queueDeclare().getQueue(); // il nome si può specificare volendo, insieme alle caratteristiche, tipo se è persistente o no
            this.channel.queueBind(this.queueName, this.exchangeName, this.topicBindKey);
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
        /*
        this.factory = new ConnectionFactory();
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
            // define the number of message that can handle at the same time.
            channel.basicQos(1);
            // define a persistent queue that is saved on disk in order to avoid loos even of the server restart.
            this.queueName = this.channel.queueDeclare(this.exchangeName + ".queue", true, false, false, null).getQueue(); // il nome si può specificare volendo, insieme alle caratteristiche, tipo se è persistente o no
            this.channel.queueBind(this.queueName, this.exchangeName, this.topicBindKey);
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }*/
    }
}
