package core.pubsub.subscriber;

import akka.actor.*;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.SensorType;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicSubscribe;
import core.pubsub.message.MessagesUtils;
import core.pubsub.message.ValueMessage;
import core.pubsub.publisher.LevelPublisherActor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Manages the communication between control unit and data centre.
 *
 * @author Giulia Lucchi
 */
public class PatientDataReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "patientData.queue";
    private static final String EXCHANGE_NAME = "patientData";
    private static final String ROUTING_KEY_DATA = "datacentre.receive.patientdata";
    private static final String ROUTING_KEY_SENSOR = "datacentre.receive.sensor";
    private static final List<String> ROUTING_KEYS = Arrays.asList(ROUTING_KEY_DATA, ROUTING_KEY_SENSOR);

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final H2dbManager H2manager = new H2dbManagerImpl();
    private final MessagesUtils utils = new MessagesUtils();
    private final ActorRef emergencyActor = ActorSystem.apply("datacentre").actorOf(Props.create(LevelPublisherActor.class), "emergencyActor");

    @Override
    public void preStart() throws Exception {
        super.preStart();
        TopicSubscribe subscribe = new TopicSubscribe(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEYS, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                JSONObject json;
                try{
                    if(envelope.getRoutingKey().equals(ROUTING_KEY_DATA)){
                        json = new JSONObject(message);
                        String type = (String) json.get("type");
                        JSONObject value = (JSONObject) json.get("message");
                        JSONObject output = (JSONObject) value.get("output");
                        String idPatient = (String) value.get("patientId");
                        int level = output.getInt("level");
                        if(level == 2 || level == 3){
                            getContext().actorSelection("/user/app/levelPublisherActor").tell(new ValueMessage(level,json, idPatient), emergencyActor);
                        }
                        String messageToInsert = utils.convertToFormatApi(value.toString());
                        H2manager.addSensorValue(idPatient, SensorType.valueOf(type),messageToInsert);
                    }else if(envelope.getRoutingKey().equals(ROUTING_KEY_SENSOR)){
                        json = new JSONObject(message);
                        String patientId = (String) json.get("patientId");
                        String type = (String) json.get("type");
                        H2manager.addNewSensorType(patientId,SensorType.valueOf(type));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        subscribe.setConsumer(consumer);
        System.out.println(" -----> PatientDataReceiverActor STARTED.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
