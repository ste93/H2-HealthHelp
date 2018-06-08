package core.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by lucch on 08/06/2018.
 */
public class basicTest {
    private static final String EXCHANGE_NAME = "patientData";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("213.209.230.94");
        factory.setUsername("admin");
        factory.setPassword("exchange");
        factory.setPort(8088);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        ArrayList<String> array = new ArrayList<>();
        array.add("datacentre.receive.patientdata");
        array.add("datacentre.receive.sensor");

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare("patientData.queue", true, false, false, null);

        if (array.size() < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
           // System.exit(1);
        }

        for (String bindingKey : array) {
            channel.queueBind("patientData.queue", EXCHANGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume("patientData.queue", true, consumer);
    }
}