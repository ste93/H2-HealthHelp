package core.pubsub.subscriber;

import akka.actor.*;
import core.SensorType;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.EmergencyActor;
import core.pubsub.EmergencyMessage;
import core.pubsub.MessagesUtils;
import core.pubsub.core.AbstractTopicSubscriber;
import core.pubsub.core.SubscriberBehaviour;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Simple message subscriber for pub/sub communication for patient data.
 *
 * @author manuBottax
 *          modify by Giulia Lucchi
 */
public class PatientDataReceiver extends AbstractTopicSubscriber {

    private static final String QUEUE_NAME= "patientData.queue";
    private static final String EXCHANGE_NAME = "patientData";
    private static final String ROUTING_KEY_DATA = "datacentre.receive.patientdata";
    private static final String ROUTING_KEY_SENSOR = "datacentre.receive.sensor";
    private static final List<String> ROUTING_KEYS = Arrays.asList(ROUTING_KEY_DATA, ROUTING_KEY_SENSOR);

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private final H2dbManager H2manager = new H2dbManagerImpl();
    private final MessagesUtils utils = new MessagesUtils();
    private final ActorRef emergencyActor = ActorSystem.apply("datacentre").actorOf(Props.create(EmergencyActor.class), "emergencyActor");

    /**
     * Default constructor for the PatientDataReceiver class.
     */
    public PatientDataReceiver() {
        super(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEYS, HOST_IP, PORT);
        this.setBehaviour(behaviour);
    }

    private SubscriberBehaviour behaviour = (String message) -> {
        JSONObject json = null;
        try{
            if(message.contains(ROUTING_KEY_DATA)){
                String body = utils.getBody(message, ROUTING_KEY_DATA);

                json = new JSONObject(body);

                String type = (String) json.get("type");

                JSONObject value = (JSONObject) json.get("message");

                JSONObject output = (JSONObject) value.get("output");
                int level = output.getInt("level");
                if(level == 2 || level == 3){
                    emergencyActor.tell(new EmergencyMessage(1), emergencyActor);
                }

                String idPatient = (String)value.get("patientId");
                String messageToInsert = convertToFormatApi(value.toString());

                H2manager.addSensorValue(idPatient, SensorType.valueOf(type),messageToInsert);

            }else if(message.contains(ROUTING_KEY_SENSOR)){
                String body = message.split(ROUTING_KEY_SENSOR)[1];

                json = new JSONObject(body);
                String patientId = (String) json.get("patientId");
                String type = (String) json.get("type");

                H2manager.addNewSensorType(patientId,SensorType.valueOf(type));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    };

    /**
     *
     * @param value body of message.
     *
     * @return string in accepted format to H2 db API.
     */
    private String convertToFormatApi(String value){
        String messageFormat = value.substring(1,value.length()-1)
                                .replace("}","")
                                    .replace("{","");

        return messageFormat;
    }

}