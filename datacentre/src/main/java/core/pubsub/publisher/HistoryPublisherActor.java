package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.SensorType;
import core.UserRole;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.message.HistoryMessage;
import org.json.JSONArray;

import java.util.Optional;

/**
 * Created by lucch on 05/06/2018.
 */
public class HistoryPublisherActor extends AbstractActor{

    private static final String EXCHANGE_NAME = "historyRequest";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private H2dbManager h2dbManage = new H2dbManagerImpl();
    TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisher(EXCHANGE_NAME,HOST_IP,PORT);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(HistoryMessage.class, message -> {
                    JSONArray values = h2dbManage.getValues(message.getPatientId(), SensorType.valueOf(message.getType().toUpperCase()), Optional.of(message.getStart()), Optional.of(message.getEnd()));
                    //System.out.println("1 hist  "+ message.getRequesterRole()+"."+message.getRequesterId()+".receive.history");
                    publisher.publishMessage(values.toString(), message.getRequesterRole()+"."+message.getRequesterId()+".receive.history");
                    //System.out.println("2 hist  "+  values.toString());
                })
                .build();
    }
}
