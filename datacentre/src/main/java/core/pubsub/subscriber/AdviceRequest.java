package core.pubsub.subscriber;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import core.pubsub.message.AdviceRequestMessage;
import core.pubsub.publisher.MultiAdvicePublisherActor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AdviceRequest extends AbstractTopicSubscriber {

    private static final String QUEUE_NAME= "adviceRequest.queue";
    private static final String EXCHANGE_NAME = "adviceRequest";
    private static final String ROUTING_KEY_ADVICE = "datacentre.request.advice";

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final ActorRef multiAdvicePublisherActor = ActorSystem.apply("datacentre").actorOf(Props.create(MultiAdvicePublisherActor.class), "adviceActor");

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public AdviceRequest() {
        super(QUEUE_NAME, EXCHANGE_NAME, Arrays.asList(ROUTING_KEY_ADVICE), HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

    private SubscriberBehaviour behaviour = (String message) -> {
        try {
            JSONObject json = new JSONObject(message);

            String patientId = json.getString("patientId");
            String start = json.getString("start");
            String end =  json.getString("end");


            AdviceRequestMessage adviceRequest = new AdviceRequestMessage(patientId, start, end);

            multiAdvicePublisherActor .tell(adviceRequest, multiAdvicePublisherActor );
        } catch (JSONException e) {
            e.printStackTrace();
        }

    };

}
