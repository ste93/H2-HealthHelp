package control_unit.pub_sub;

import core.pub_sub.MqttTopicPublisher;

public class PatientDataPublisher {

    private MqttTopicPublisher publisher;
    private String patientID;

    public PatientDataPublisher(String patientID) {
        this.patientID = patientID;
        this.publisher = new MqttTopicPublisher("patientData");
    }

    public void sendData(String data) {
        this.publisher.publishMessage(this.patientID + "@" + data , "datacentre.receive.patientdata");
    }

    public void close() {
        this.publisher.close();
    }
}
