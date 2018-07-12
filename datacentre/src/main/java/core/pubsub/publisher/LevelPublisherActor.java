package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.associations.PatientManager;
import core.dbmanager.associations.PatientManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.ValueMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.stream.IntStream;

import static core.pubsub.IpAndPort.HOST_IP_AND_PORT;

/**
 * Sends the emergency (level 3) or alert (level 2) to the patient's doctors.
 *
 * @author Giulia Lucchi
 */
public class LevelPublisherActor extends AbstractActor{

    private static final String EXCHANGE_NAME = "level";

    private PatientManager patientManager = new PatientManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP_AND_PORT.getIp(),HOST_IP_AND_PORT.getPort());
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder().match(ValueMessage.class, message -> {
            JSONArray doctors = patientManager.getPatientAssociations(message.getPatientId());

            IntStream.range(0, doctors.length()).forEach(x -> {
                try {
                    String idDoctor = ((JSONObject) doctors.get(x)).getString("idDoctor");
                    if(message.getLevel() == 2){
                        this.publisher.publishMessage(message.getValue(), "doctor."+ idDoctor + ".receive.alert");
                    }else{
                        this.publisher.publishMessage(message.getValue(), "doctor."+ idDoctor + ".receive.emergency");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }).build();
    }
}
