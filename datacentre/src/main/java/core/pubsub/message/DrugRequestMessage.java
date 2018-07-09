package core.pubsub.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages the request to get all drugs of a specific patient.
 *
 * @author Giulia Lucchi
 */
public class DrugRequestMessage {

    private String patientId;
    private String start;
    private String end;

    public DrugRequestMessage(final String patientId, final String start, final String end) {
        this.patientId = patientId;
        this.start = start;
        this.end = end;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    /**
     * Get of all information in the message to String format.
     *
     * @return String completed drug.
     */
    public String getMessage() throws JSONException {
        return new JSONObject()
                .put("patientId", patientId)
                .put("start", start)
                .put("end", end).toString();
    }

}
