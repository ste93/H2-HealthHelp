package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pubsub.core.TopicSubscriber;
import core.pubsub.core.TopicSubscriberImpl;
import core.pubsub.message.DrugRequestMessage;
import core.pubsub.message.MessagesUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class  DrugRequestActor extends AbstractActor {

    private static final String QUEUE_NAME= "drugRequest.queue";
    private static final String EXCHANGE_NAME = "drugRequest";
    private static final List<String> ROUTING_KEY_ADVICE = Arrays.asList("datacentre.request.drug");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final MessagesUtils utils = new MessagesUtils();

    @Override
    public void preStart() throws Exception {
        super.preStart();
        TopicSubscriber subscribe = new TopicSubscriberImpl(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_ADVICE, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                JSONObject json;
                try {
                    json = new JSONObject(message);
                    String patientId = json.getString("patientId");
                    String start = json.getString("start");
                    String end =  json.getString("end");
                    DrugRequestMessage drugRequest = new DrugRequestMessage(patientId, start, end);
                    getContext().actorSelection("/user/app/multiDrugPublisherActor").tell(drugRequest, ActorRef.noSender() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        subscribe.setConsumer(consumer);
        System.out.println(" -----> DrugReceiver STARTED.");
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().build();
    }

}
