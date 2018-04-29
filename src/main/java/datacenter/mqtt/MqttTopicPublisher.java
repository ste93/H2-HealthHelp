package datacenter.mqtt;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MqttTopicPublisher {

    private String exchangeName;
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public MqttTopicPublisher(String name) {
        this.exchangeName = name;
        mqttSetup();
        this.factory.setHost("localhost");
    }

    public MqttTopicPublisher(String name, String hostIP) {
        this.exchangeName = name;
        mqttSetup();
        this.factory.setHost(hostIP);
    }

    public void publishMessage(String message, String routingKey ){
        try {
            channel.basicPublish(this.exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        } catch (IOException e) {
            System.err.println("Error during message publishing");
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
        } catch (IOException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Error during setup operation");
            e.printStackTrace();
        }
    }
}
