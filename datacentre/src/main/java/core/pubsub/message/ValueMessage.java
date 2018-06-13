package core.pubsub.message;

import org.json.JSONObject;

/**
 * Message to emergency.
 *
 * @author Giulia Lucchi
 */
public class ValueMessage {

    private JSONObject value;
    private int level;
    private String patientId;

    public ValueMessage(final int level, final JSONObject value, final String patientId){
        this.level = level;
        this.patientId = patientId;
        this.value = value;
    }

    public String getPatientId() {
        return patientId;
    }

    public int getLevel() {
        return level;
    }

    public String getValue() {
        return value.toString();
    }
}
