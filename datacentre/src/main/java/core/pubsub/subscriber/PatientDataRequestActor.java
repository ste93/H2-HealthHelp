package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pubsub.core.TopicSubscriber;
import core.pubsub.core.TopicSubscriberImpl;
import core.pubsub.message.HistoryMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static core.pubsub.IpAndPort.HOST_IP_AND_PORT;

/**
 * Receive request to visualized personal sensor values by patient and
 * sends them to history publisher.
 *
 * @author Giulia Lucchi
 */
public class PatientDataRequestActor extends AbstractActor {

    private static final String QUEUE_NAME= "history.queue";
    private static final String EXCHANGE_NAME = "historyRequest";
    private static final List<String> ROUTING_KEY_HISTORY = Arrays.asList("datacentre.request.history");
;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        TopicSubscriber subscribe = new TopicSubscriberImpl(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_HISTORY, HOST_IP_AND_PORT.getIp(),HOST_IP_AND_PORT.getPort());

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                JSONObject json;
                try {

                    json = new JSONObject(message);
                    String patientId = json.getString("patientId");
                    String type = json.getString("type");
                    String start = json.getString("start");
                    String end =  json.getString("end");

                    String requesterRole =  json.getString("requesterRole");
                    String requesterId = json.getString("requesterId");

                    HistoryMessage historyMessage = new HistoryMessage(patientId, type, start, end, requesterRole, requesterId);
                    getContext().actorSelection("/user/app/historyPublisherActor").tell(historyMessage, ActorRef.noSender());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        subscribe.setConsumer(consumer);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
