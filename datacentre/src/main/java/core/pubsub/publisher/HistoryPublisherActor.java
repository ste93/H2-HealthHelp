package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.SensorType;
import core.dbmanager.h2dbManager.DataSensorManager;
import core.dbmanager.h2dbManager.DataSensorManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.HistoryMessage;
import org.json.JSONArray;

import java.util.Optional;

/**
 * Sends the patient's clinical history to the requester
 *
 * @author Giulia Lucchi
 */
public class HistoryPublisherActor extends AbstractActor{

    private static final String EXCHANGE_NAME = "historyRequest";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private DataSensorManager dataSensorManager = new DataSensorManagerImpl();
    TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(HistoryMessage.class, message -> {

            JSONArray values = dataSensorManager.getSensorValues(message.getPatientId(), SensorType.valueOf(message.getType().toUpperCase()), Optional.of(message.getStart()), Optional.of(message.getEnd()));

            publisher.publishMessage(values.toString(), message.getRequesterRole()+"."+message.getRequesterId()+".receive.history");
        }).build();
    }
}
