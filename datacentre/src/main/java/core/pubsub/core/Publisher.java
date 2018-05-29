package core.pubsub.core;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


public class Publisher {

    // the name has to be changed every time you change the exchange type
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ///// initial configuration //////
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        ////  Topic Exchange //////////////////////////////
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String routingKey = "datacentre.receive.patientdata";

        for(int i = 0; i < 15; i ++) {
            String message = "{ \"patientId\": \"marghe.lucchi\",\n" +
                    "\"dataType\": \"temperature\",\n" +
                    "   \"value\": 123,\n" +
                    "   \"unit\": \"dk\",\n" +
                    "   \"timestamp\": \"25-12-1334 11:34\",\n" +
                    "   \"output\": {\n" +
                    "       \"level\": 0,\n" +
                    "       \"description\": \"fghjkl\"\n" +
                    "   }\n" +
                    "}";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }

        channel.close();
        connection.close();
    }

    //Utilities method for console message handling
//    private static String getMessage(String[] strings){
//        if (strings.length < 1)
//            return "info: Hello World!";
//        return joinStrings(strings, " ");
//    }
//
//    private static String joinStrings(String[] strings, String delimiter) {
//        int length = strings.length;
//        if (length == 0) return "";
//        StringBuilder words = new StringBuilder(strings[0]);
//        for (int i = 1; i < length; i++) {
//            words.append(delimiter).append(strings[i]);
//        }
//        return words.toString();
//    }

}
