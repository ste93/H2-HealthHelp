package core.pubsub.subscriber;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import core.pubsub.message.AdviceMessage;
import core.pubsub.message.MessagesUtils;
import core.pubsub.publisher.AdvicePublisherActor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by lucch on 08/06/2018.
 */
public class InfoReceiver extends AbstractTopicSubscriber {

    private static final String QUEUE_NAME= "info.queue";
    private static final String EXCHANGE_NAME = "info";
    private static final String ROUTING_KEY_ADVICE = "datacentre.request.info";

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final MessagesUtils utils = new MessagesUtils();
    private final H2dbManager h2dbManager = new H2dbManagerImpl();

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public InfoReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, Arrays.asList(ROUTING_KEY_ADVICE), HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

    private SubscriberBehaviour behaviour = (String message) -> {
        try {
            String body = utils.getBody(message, ROUTING_KEY_ADVICE);
            System.out.println(body);
            JSONObject json = new JSONObject(body);
            System.out.println("arriato  "+ json);
            String patientId = json.getString("role");
            String doctorId = json.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
}
