package core.pubsub.subscriber;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.UserRole;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import core.pubsub.message.HistoryMessage;
import core.pubsub.message.MessagesUtils;
import core.pubsub.publisher.HistoryPublisherActor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 *
 */
public class RequestReceiver extends AbstractTopicSubscriber {

    private static final String QUEUE_NAME= "history.queue";
    private static final String EXCHANGE_NAME = "historyRequest";
    private static final String ROUTING_KEY_HISTORY = "datacentre.request.history";

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;


    private final MessagesUtils utils = new MessagesUtils();
    private final ActorRef historyActor = ActorSystem.apply("datacentre").actorOf(Props.create(HistoryPublisherActor.class), "historyActor");

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public RequestReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, Arrays.asList(ROUTING_KEY_HISTORY), HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

    private SubscriberBehaviour behaviour = (String message) -> {
        try {
            String body = utils.getBody(message, ROUTING_KEY_HISTORY);
            System.out.println(body);
            JSONObject json = new JSONObject(body);
            System.out.println(json);
            String patientId = json.getString("patientId");
            String type = json.getString("type");
            String start = json.getString("start");
            String end =  json.getString("end");
            String requesterRole =  json.getString("requesterRole");
            String requesterId = json.getString("requesterId");

            HistoryMessage historyMessage = new HistoryMessage(patientId, type, start, end, requesterRole, requesterId);
           System.out.println(" message creato");
            historyActor.tell(historyMessage, historyActor);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
}
