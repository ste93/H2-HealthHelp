package core.pub_sub.example;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionTest {

    //private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("213.209.230.94");
        factory.setPort(8088);
        factory.setUsername("admin");
        factory.setPassword("exchange");
        try {
            Connection connection = factory.newConnection();
        }
        catch (Exception ex ) {ex.printStackTrace();}
        System.out.println("Connected");
        //Channel channel = connection.createChannel();

        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //String message = "Hello World!";
        //channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        //System.out.println(" [x] Sent '" + message + "'");

        //channel.close();
        //connection.close();
    }
}
