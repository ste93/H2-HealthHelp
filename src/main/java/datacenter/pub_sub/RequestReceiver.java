package datacenter.pub_sub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pub_sub.MqttTopicSubscriber;

import java.io.IOException;

public class RequestReceiver extends MqttTopicSubscriber {

    public RequestReceiver() {
        super("historyRequest", "datacentre.request.history");
    }


    @Override
    public void setConsumer() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [HistoryRequestReceiver] Received a new history request ! message -> " + envelope.getRoutingKey() + " : ' " + message + " '");
                handleRequest();
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }

    private void handleRequest() {
        System.out.println("[HistoryRequestReceiver] Do something with my history request");
    }
}
