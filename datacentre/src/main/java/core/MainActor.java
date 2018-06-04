package core;

import akka.actor.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import core.pubsub.core.EmergencyMessage;

import java.net.URI;

/**
 * Starts all the actors that communicate in the data centre.
 *
 * @author Giulia Lucchi
 */
public class MainActor extends AbstractActor {


  /*  private Config config = ConfigFactory.parseString(
            " akka { \n" +
                " actor { \n" +
                " provider = remote\n" +
                "}\n" +
                " remote { \n" +
                " enabled-transports = [\"akka.remote.netty.tcp\"]\n" +
                " netty.tcp { \n" +
                    " hostname = \"" + "127.0.0.1" +"\"\n" +
                    " port = 2552\n" +
                    "}\n" +
                "}\n" +
            "}\n");*/

    private ActorSystem actorSystem = ActorSystem.create("datacentre");

    @Override
    public void preStart() throws Exception {
        super.preStart();
        final ActorRef patientDataReceiverActor = actorSystem.actorOf(Props.create(PatientDataReceiverActor.class), "patientDataReceiver");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
