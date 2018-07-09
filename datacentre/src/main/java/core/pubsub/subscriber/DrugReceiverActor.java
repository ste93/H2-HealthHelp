package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.dbmanager.h2dbManager.DataSensorManager;
import core.dbmanager.h2dbManager.DataSensorManagerImpl;
import core.dbmanager.h2dbManager.DrugManager;
import core.dbmanager.h2dbManager.DrugManagerImpl;
import core.pubsub.core.TopicSubscriber;
import core.pubsub.core.TopicSubscriberImpl;
import core.pubsub.message.MessagesUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Receives prescribed drugs written by doctors for patients and store in application Knowledge base.
 *
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */
public class DrugReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "drug.queue";
    private static final String EXCHANGE_NAME = "drug";
    private static final List<String> ROUTING_KEY_DRUG = Arrays.asList("datacentre.receive.drug");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final MessagesUtils utils = new MessagesUtils();
    private final DrugManager drugManager = new DrugManagerImpl();

    @Override
    public void preStart() throws Exception {
        super.preStart();
        TopicSubscriber subscribe = new TopicSubscriberImpl(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_DRUG, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                JSONObject json;
                try {
                    json = new JSONObject(message);
                    String patientId = json.getString("patientId");
                    String prescribedDrug = json.getString("message");
                    String messageToInsert = utils.convertToFormatApi(prescribedDrug);

                    drugManager.addDrug(patientId, messageToInsert);
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
