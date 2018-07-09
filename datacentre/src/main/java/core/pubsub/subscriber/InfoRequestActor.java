package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.UserRole;
import core.dbmanager.h2dbManager.DataSensorManager;
import core.dbmanager.h2dbManager.DataSensorManagerImpl;
import core.dbmanager.h2dbManager.UserManager;
import core.dbmanager.h2dbManager.UserManagerImpl;
import core.pubsub.core.TopicSubscriber;
import core.pubsub.core.TopicSubscriberImpl;
import core.pubsub.message.UserMessage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Receives request to visualized personal information related to a requester and
 *  sends them to info publisher.
 *
 * @author Giulia Lucchi
 */
public class InfoRequestActor extends AbstractActor {

    private static final String QUEUE_NAME= "info.queue";
    private static final String EXCHANGE_NAME = "info";
    private static final List<String> ROUTING_KEY_INFO = Arrays.asList("datacentre.request.info");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private static final UserManager userManager = new UserManagerImpl();

    @Override
    public void preStart() throws Exception {
        super.preStart();
        TopicSubscriber subscribe = new TopicSubscriberImpl(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_INFO, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (envelope.getRoutingKey().equals(ROUTING_KEY_INFO.get(0))) {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");

                    JSONObject json;
                    try {
                        json = new JSONObject(message);
                        UserRole role = UserRole.valueOf(json.getString("role").toUpperCase());
                        String idCode = json.getString("id");

                        JSONObject response = userManager.getUserInformation(role, idCode);
                        String id = response.getString("idCode");
                        String name = response.getString("name");
                        String surname = response.getString("surname");
                        String cf = response.getString("cf");
                        String mail = response.getString("mail");
                        String phones = response.getString("phone");

                        UserMessage info = new UserMessage(id, name, surname, cf, phones, mail);
                        getContext().actorSelection("/user/app/infoPublisherActor").tell(info, ActorRef.noSender());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
