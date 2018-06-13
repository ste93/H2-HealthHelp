package core.pubsub.subscriber;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.UserRole;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.pubsub.core.TopicSubscribe;
import core.pubsub.message.UserMessage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lucch on 08/06/2018.
 */
public class InfoReceiverActor extends AbstractActor {

    private static final String QUEUE_NAME= "info.queue";
    private static final String EXCHANGE_NAME = "info";
    private static final List<String> ROUTING_KEY_INFO = Arrays.asList("datacentre.request.info");

    private static final String HOST_IP = "213.209.230.94";
    private static final int PORT = 8088;

    private static final H2dbManager H2manager = new H2dbManagerImpl();



    @Override
    public void preStart() throws Exception {
        super.preStart();



        TopicSubscribe subscribe = new TopicSubscribe(EXCHANGE_NAME, QUEUE_NAME, ROUTING_KEY_INFO, HOST_IP, PORT);

        Consumer consumer = new DefaultConsumer(subscribe.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (envelope.getRoutingKey().equals(ROUTING_KEY_INFO.get(0))) {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                    JSONObject json;
                    try {

                        System.out.println(message);
                        json = new JSONObject(message);

                        UserRole role = UserRole.valueOf(json.getString("role").toUpperCase());
                        String idCode = json.getString("id");

                        JSONObject response = H2manager.getUserInformation(role, idCode);

                        System.out.println("CAZZO" + response);
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

        System.out.println(" -----> InfoReceierActor STARTED");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
