package core.pubsub.core;

/**
 * Created by lucch on 08/06/2018.
 */
public class SubscriberBehaviorImpl implements SubscriberBehaviour {
    /**
     * the method that define the behaviour to apply to a message received.
     *
     * @param message - the message received
     * @param key
     */
    @Override
    public String handleMessage(String message, String key) {
        return key+message;
    }
}
