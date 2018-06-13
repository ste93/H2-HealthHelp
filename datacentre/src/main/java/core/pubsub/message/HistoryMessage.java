package core.pubsub.message;

/**
 * Created by lucch on 05/06/2018.
 */
public class HistoryMessage {
    private String patientId;
    private String type;
    private String start;
    private String end;
    private String requesterRole;
    private String requesterId;

    public HistoryMessage(final String patientId, final String type, final String start, final String end, final  String requesterRole, final String requesterId) {
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
        return type.toUpperCase();
    }

    public String getRequesterRole() {
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
