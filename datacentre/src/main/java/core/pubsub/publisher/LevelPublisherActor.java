package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.associations.PatientManager;
import core.dbmanager.associations.PatientManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.message.ValueMessage;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.stream.IntStream;

/**
 * Actor that manages the emergency, derived from sensor data values.emergency
 *
 * @author Giulia Lucchi
 */
public class LevelPublisherActor extends AbstractActor{

    private static final String EXCHANGE_NAME = "level";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private PatientManager patientManager = new PatientManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisher(EXCHANGE_NAME,HOST_IP,PORT);

    }

    @Override
    public Receive createReceive() {

        return receiveBuilder().match(ValueMessage.class, message -> {
            //JSONArray doctors = patientManager.getPatientAssociations(message.getPatientId());
               // IntStream.range(0, doctors.length()).forEach(x -> {
                    try {
                        if(message.getLevel() == 2){
                            this.publisher.publishMessage(message.getValue(), "doctor."+/*doctors.get(x)*/"mario.rossi.receive.alert");
                        }else{
                            //this.publisher.publishMessage(message.getValue(), "doctor."+doctors.get(x)+".receive.emergency");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                //}
                    //);



        }).build();
    }
}
