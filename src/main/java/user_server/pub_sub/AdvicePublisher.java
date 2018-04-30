package user_server.pub_sub;

import core.pub_sub.MqttTopicPublisher;

public class AdvicePublisher {

    private MqttTopicPublisher publisher;
    private String patientID;

    public AdvicePublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new MqttTopicPublisher("advice");
    }

    public void sendAdvice(String advice) {
        this.publisher.publishMessage(this.patientID + "@" + advice, "datacentre.receive.advice");
    }

    public void close() {
        this.publisher.close();
    }

}