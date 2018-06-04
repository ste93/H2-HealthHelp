package core.pubsub;

import akka.actor.AbstractActor;
import core.pubsub.EmergencyMessage;

/**
 * Actor that manages the emergency, derived from sensor data values.
 *
 * @author Giulia Lucchi
 */
public class EmergencyActor extends AbstractActor{
    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(EmergencyMessage.class, message ->{
                    System.out.println("ARRIVATO!!   "+ message );
                })
                .build();
    }
}
