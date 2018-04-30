package datacenter.pub_sub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pub_sub.TopicSubscriber;

import java.io.IOException;

/**
 * Simple message subscriber for pub/sub communication for history request.
 *
 * @author manuBottax
 */
public class RequestReceiver extends TopicSubscriber {

    /**
     * Default constructor for the RequestReceiver class.
     */
    public RequestReceiver() {
        super("historyRequest", "datacentre.request.history");
        this.setConsumer();
    }

    /**
     * Define the custom behaviour when a message is received on the subscribed folder.
     * The actual behaviour is defined in the private method 'handleAdvice()'
     */
    @Override
    public void setConsumer() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [HistoryRequestReceiver] Received a new history request ! message -> " + envelope.getRoutingKey() + " : ' " + message + " '");
                try {
                    handleRequest();
                } finally {
                    System.out.println(" [x] Done");
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

    private void handleRequest() {
        System.out.println("[HistoryRequestReceiver] Do something with my history request");
    }
}
