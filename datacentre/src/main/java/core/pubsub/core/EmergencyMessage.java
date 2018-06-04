package core.pubsub.core;

/**
 * Message to emergency.
 *
 * @author Giulia Lucchi
 */
public class EmergencyMessage {

    private int level;

    public EmergencyMessage(final int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
