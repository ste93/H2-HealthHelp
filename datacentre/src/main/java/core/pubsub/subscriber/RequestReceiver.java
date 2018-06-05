package core.pubsub.subscriber;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.UserRole;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import core.pubsub.message.HistoryMessage;
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
            JSONObject json = new JSONObject(message);

            String patientId = json.get("patientId").toString();
            String type = json.getString("type").toString();
            String start = json.get("start").toString();
            String end =  json.get("end").toString();
            UserRole requesterRole= UserRole.valueOf(json.get("requestRole").toString());
            String requesterId = json.get("requesterId").toString();

            HistoryMessage historyMessage = new HistoryMessage(patientId, type, start, end, requesterRole, requesterId);
            historyActor.tell(historyMessage, historyActor);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
}
