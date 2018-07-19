package core.pubsub.message;

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

    /**
     *
     * @param value body of message.
     *
     * @return string in accepted format to H2 db API.
     */
    public String convertToFormatApi(String value){
        String messageFormat = value.substring(1,value.length()-1)
                .replace("}","")
                .replace("{","");

        return messageFormat;
    }
}
