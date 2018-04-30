package core.pub_sub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MqttTopicSubscriber {

    private String exchangeName;
    protected String queueName;
    private String topicBindKey;

    private ConnectionFactory factory;
    private Connection connection;
    protected Channel channel;


    public MqttTopicSubscriber(String exchangeName, String topicKey) {
        this.exchangeName = exchangeName;
        this.topicBindKey = topicKey;
        this.mqttSetup();
        this.factory.setHost("localhost");
        this.setConsumer();
    }

    public MqttTopicSubscriber(String exchangeName, String topicKey, String hostIP) {
        this.exchangeName = exchangeName;
        this.topicBindKey = topicKey;
        this.mqttSetup();
        this.factory.setHost(hostIP);
        this.setConsumer();
    }

    public void setConsumer (){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received -> " + envelope.getRoutingKey() + " : ' " + message + " '");
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }


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
            this.queueName = this.channel.queueDeclare(this.exchangeName + ".queue", true, false, false, null).getQueue(); // il nome si può specificare volendo, insieme alle caratteristiche, tipo se è persistente o no
            this.channel.queueBind(this.queueName, this.exchangeName, this.topicBindKey);
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
    }
}
