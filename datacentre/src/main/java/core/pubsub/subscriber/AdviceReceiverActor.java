package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pubsub.core.TopicSubscriber;
import core.pubsub.core.TopicSubscriberImpl;
import core.pubsub.message.AdviceMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static core.pubsub.IpAndPort.HOST_IP_AND_PORT;

/**
 * Receives advices written by doctors for patients and sends them to advice publisher.
 *
 * @author Giulia Lucchi
 */
public class AdviceReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "advice.queue";
    private static final String EXCHANGE_NAME = "advice";
    private static final List<String> ROUTING_KEY_ADVICE = Arrays.asList("datacentre.receive.advice");


    @Override
    public void preStart() throws Exception {
        super.preStart();

        TopicSubscriber subscriber = new TopicSubscriberImpl(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_ADVICE, HOST_IP_AND_PORT.getIp(),HOST_IP_AND_PORT.getPort());

        Consumer consumer = new DefaultConsumer(subscriber.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                JSONObject json;
                try {

                    json = new JSONObject(message);
                    String patientId = json.getString("patientId");
                    String doctorId = json.getString("doctorId");
                    String advice = json.getString("advice");
                    String timestamp =  json.getString("timestamp");

                    AdviceMessage adviceMessage = new AdviceMessage(patientId, doctorId, advice, timestamp);
                    getContext().actorSelection("/user/app/advicePublisherActor").tell(adviceMessage, ActorRef.noSender());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        subscriber.setConsumer(consumer);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
