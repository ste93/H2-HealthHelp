package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pubsub.core.TopicSubscribe;
import core.pubsub.message.HistoryMessage;
import core.pubsub.message.MessagesUtils;
import core.pubsub.publisher.HistoryPublisherActor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lucch on 05/06/2018.
 */
public class RequestReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "history.queue";
    private static final String EXCHANGE_NAME = "historyRequest";
    private static final List<String> ROUTING_KEY_HISTORY = Arrays.asList("datacentre.request.history");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;


    private final MessagesUtils utils = new MessagesUtils();

    @Override
    public void preStart() throws Exception {
        super.preStart();

        TopicSubscribe subscribe = new TopicSubscribe(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_HISTORY, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                JSONObject json;
                try {
                    json = new JSONObject(message);

                    System.out.println(json);
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







        //RequestReceiver subscriber = new RequestReceiver();
        System.out.println(" -----> RequestReceiverActor STARTED.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
