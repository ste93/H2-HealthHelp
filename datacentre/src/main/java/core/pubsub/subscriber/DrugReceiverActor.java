package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicSubscribe;
import core.pubsub.message.MessagesUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lucch on 07/06/2018.
 */
public class DrugReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "drug.queue";
    private static final String EXCHANGE_NAME = "drug";
    private static final List<String> ROUTING_KEY_DRUG = Arrays.asList("datacentre.receive.drug");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final MessagesUtils utils = new MessagesUtils();
    private final H2dbManager H2dbmanager = new H2dbManagerImpl();

    @Override
    public void preStart() throws Exception {
        super.preStart();



        TopicSubscribe subscribe = new TopicSubscribe(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_DRUG, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                JSONObject json;
                try {

                    json = new JSONObject(message);

                    System.out.println("arrivato drug   "+ json);
                    String patientId = json.getString("patientId");
                    String prescribedDrug = json.getString("message");

                    String messageToInsert = utils.convertToFormatApi(prescribedDrug);

                    H2dbmanager.addDrug(patientId, messageToInsert);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        subscribe.setConsumer(consumer);






        //DrugReceiver drugReceiver = new DrugReceiver();
        System.out.println(" -----> DrugReceierActor STARTED");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
