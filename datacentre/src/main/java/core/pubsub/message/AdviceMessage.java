package core.pubsub.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages the advice to send from the doctor.
 *
 * @author Giulia Lucchi
 */
public class AdviceMessage {

    private String patientId;
    private String doctorId;
    private String advice;
    private String timestamp;

    public AdviceMessage(String patientId, String doctorId, String advice, String timestamp) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.advice = advice;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getAdvice() {
        return advice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Get of all information in the message to JSONOjbet format.
     *
     * @return JSONObject completed advice.
     */
    public String getMessage() throws JSONException {
        return new JSONObject()
                    .put("patientId", patientId)
                    .put("doctorId", doctorId)
                    .put("advice", advice)
                    .put("timestamp", timestamp).toString();
    }
}
