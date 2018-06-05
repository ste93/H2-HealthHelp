package core.pubsub.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucch on 05/06/2018.
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

    public String getMessage() throws JSONException {
        return new JSONObject()
                    .put("patientId", patientId)
                    .put("doctorId", doctorId)
                    .put("advice", advice)
                    .put("timestamp", timestamp).toString();
    }
}
