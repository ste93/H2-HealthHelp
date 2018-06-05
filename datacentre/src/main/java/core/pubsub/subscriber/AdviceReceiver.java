package core.pubsub.subscriber;

import core.pubsub.message.AdviceMessage;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by lucch on 04/06/2018.
 */
public class AdviceReceiver extends AbstractTopicSubscriber {

    private static final String QUEUE_NAME= "advice.queue";
    private static final String EXCHANGE_NAME = "advice";
    private static final String ROUTING_KEY_ADVICE = "datacentre.receive.advice";

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;



    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public AdviceReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, Arrays.asList(ROUTING_KEY_ADVICE), HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

    private SubscriberBehaviour behaviour = (String message) -> {
        try {
            JSONObject json = new JSONObject(message);

            String patientId = json.getString("patientId");
            String doctorId = json.getString("doctorId");
            String advice = json.getString("advice");
            String timestamp =  json.getString("timestamp");


            AdviceMessage adviceMessage = new AdviceMessage(patientId, doctorId, advice, timestamp);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    };
}
