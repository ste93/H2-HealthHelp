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

import static core.pubsub.IpAndPort.HOST_IP_AND_PORT;

/**
 * Sends the patient's clinical history to the requester
 *
 * @author Giulia Lucchi
 */
public class HistoryPublisherActor extends AbstractActor{

    private static final String EXCHANGE_NAME = "historyRequest";

    private DataSensorManager dataSensorManager = new DataSensorManagerImpl();
    TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP_AND_PORT.getIp(),HOST_IP_AND_PORT.getPort());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(HistoryMessage.class, message -> {

            JSONArray values = dataSensorManager.getSensorValues(message.getPatientId(), SensorType.valueOf(message.getType().toUpperCase()), Optional.of(message.getStart()), Optional.of(message.getEnd()));

            publisher.publishMessage(values.toString(), message.getRequesterRole()+"."+message.getRequesterId()+".receive.history");
        }).build();
    }
}
