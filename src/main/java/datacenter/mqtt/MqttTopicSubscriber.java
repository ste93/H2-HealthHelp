package datacenter.mqtt;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MqttTopicSubscriber {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String queueName;

    public MqttTopicSubscriber(String name) {
        this.exchangeName = name;
        mqttSetup();
        this.factory.setHost("localhost");
    }

    public MqttTopicSubscriber(String name, String hostIP) {
        this.exchangeName = name;
        mqttSetup();
        this.factory.setHost(hostIP);
    }

    public void bindTopic (String bindKey){
        try {
            channel.queueBind(queueName, this.exchangeName, bindKey);
        } catch (IOException e) {
            System.err.println("Error during binding operation");
            e.printStackTrace();
        }

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
            this.queueName = channel.queueDeclare().getQueue(); // il nome si può specificare volendo, insieme alle caratteristiche, tipo se è persistente o no
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
    }
}
