package datacenter.pub_sub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pub_sub.TopicSubscriber;

import java.io.IOException;

/**
 * Simple message subscriber for pub/sub communication of doctor's advice.
 *
 * @author manuBottax
 */
public class AdviceReceiver extends TopicSubscriber {

    private static final String EXCHANGE_NAME = "advice";
    private static final String ROUTING_KEY = "datacentre.receive.advice";

    /**
     * Default constructor for the AdviceReceiver class.
     */
    public AdviceReceiver() {
        super(EXCHANGE_NAME, ROUTING_KEY);
        this.setConsumer();
    }

    /**
     * Define the custom behaviour when a message is received on the subscribed folder.
     * The actual behaviour is defined in the private method 'handleAdvice()'
     */
    //TODO: esiste un modo di definire solo il behaviour senza dover rifare ogni volta tutto che tanto è sempre uguale ?
    // passaggio di una funzione praticamente  -> vedremo

    @Override
    public void setConsumer() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [AdviceReceiver] Received a new advice ! message -> " + envelope.getRoutingKey() + " : ' " + message + " '");
                try {
                        handleAdvice();
                }
                //If the process die before completing the current message handling the message is send to another one.
                finally {
                    System.out.println(" [x] Done");
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

    private void handleAdvice() {
        System.out.println("[AdviceReceiverReceiver] Do something with my advice");
    }

}