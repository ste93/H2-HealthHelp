package core.pubsub;

/**
 * Includes the operation over messages.
 *
 * @author Giulia Lucchi
 */
public class MessagesUtils {

    /**
     *
     * @param message message in susbriber's queue
     * @param bindKey the binding key of topic model
     *
     * @return the body of message.
     */
    public String getBody(String message, String bindKey){
        return message.split(bindKey)[1];
    }
}
