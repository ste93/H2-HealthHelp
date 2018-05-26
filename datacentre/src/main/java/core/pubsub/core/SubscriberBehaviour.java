package core.pubsub.core;

/**
 * Functional interface for specify costum behaviour in subscriber.
 *
 * @author manuBottax
 */
public interface SubscriberBehaviour {
    
    /**
     * the method that define the behaviour to apply to a message received.
     * @param message - the message received
     */
    void handleMessage(String message);
}
