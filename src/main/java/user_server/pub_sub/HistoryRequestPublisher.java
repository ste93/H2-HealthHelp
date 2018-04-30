package user_server.pub_sub;

import core.pub_sub.MqttTopicPublisher;
import user_server.UserServerLauncher;

public class HistoryRequestPublisher {

    private MqttTopicPublisher publisher;
    private String patientID;

    public HistoryRequestPublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new MqttTopicPublisher("historyRequest");
    }

    public void sendRequest(String request) {
        this.publisher.publishMessage(this.patientID + "@" + request, "datacentre.request.history");
    }

    public void close() {
        this.publisher.close();
    }


}
