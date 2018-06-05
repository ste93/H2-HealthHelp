package core.pubsub.message;

import core.UserRole;
import org.json.JSONArray;

/**
 * Created by lucch on 05/06/2018.
 */
public class HistoryMessage {
    private String patientId;
    private String type;
    private String start;
    private String end;
    private UserRole requesterRole;
    private String requesterId;

    public HistoryMessage(final String patientId, final String type, final String start, final String end,
                                                        final  UserRole requesterRole, final String requesterId) {
        this.patientId = patientId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.requesterId = requesterId;
        this.requesterRole = requesterRole;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getType() {
        return type;
    }

    public UserRole getRequesterRole() {
        return requesterRole;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getStart() {

        return start;
    }

    public String getEnd() {
        return end;
    }
}
