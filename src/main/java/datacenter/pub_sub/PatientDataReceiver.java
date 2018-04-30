package datacenter.pub_sub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import core.pub_sub.MqttTopicSubscriber;

import java.io.IOException;

public class PatientDataReceiver extends MqttTopicSubscriber {

    public PatientDataReceiver() {
        super("patientData", "datacentre.receive.patientdata");
    }


    @Override
    public void setConsumer() {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [PatientDataReceiver] Received a new Data from a patient ! message -> " + envelope.getRoutingKey() + " : ' " + message + " '");
                saveData();
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            System.err.println("Error during starting operation");
            e.printStackTrace();
        }
    }

    private void saveData(){
        System.out.println("[PatientDataReceiver] Do something with my data");
    }
}
