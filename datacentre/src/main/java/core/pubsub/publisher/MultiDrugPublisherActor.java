package core.pubsub.publisher;

import akka.actor.AbstractActor;
import core.dbmanager.h2dbManager.DrugManager;
import core.dbmanager.h2dbManager.DrugManagerImpl;
import core.pubsub.core.TopicPublisher;
import core.pubsub.core.TopicPublisherImpl;
import core.pubsub.message.DrugRequestMessage;
import org.json.JSONArray;


import java.util.Optional;

/**
 * Sends to the requester all drugs relative to a specific patient on a time range.
 *
 * @author Giulia Lucchi
 */
public class MultiDrugPublisherActor extends AbstractActor {

    private static final String EXCHANGE_NAME = "drugRequest";
    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private DrugManager drugManager = new DrugManagerImpl();
    private TopicPublisher publisher;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.publisher = new TopicPublisherImpl(EXCHANGE_NAME,HOST_IP,PORT);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder().match(DrugRequestMessage.class, message -> {

            JSONArray values = drugManager.getDrugs(message.getPatientId(), Optional.of(message.getStart()), Optional.of(message.getEnd()));

            publisher.publishMessage(values.toString(), "patient."+message.getPatientId()+".receive.drug");

        }).build();
    }

}
