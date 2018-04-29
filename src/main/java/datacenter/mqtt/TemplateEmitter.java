package datacenter.mqtt;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 *  A class used to test rabbitmq basic functionalities and as a template for it.
 *
 * @author manuBottax
 */
public class TemplateEmitter {

    // the name has to be changed every time you change the exchange type
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ///// initial configuration //////
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /////////////////// direct sending ( no routing ) //////////////
        //   --  [Persistence 1 ] true state that queue is saved on disk, so it remain even if the mqtt server restart
        //   --  N.B. if the queue not exist it create it, otherwise it connect to it.
        // channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // String message = "Hello World ( " + i +" ) ..";//getMessage(argv);
        //   --  [Persistence 2 ] MessageProperties.... state that single message are saved on disk, so are resistant to server failure.
        //   --  (it can be lost if writing is not completed at the moment of failure, further persistence option are available )
        //channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        //////////////////////////////////////////////////////////////////////////////////


        //// Simple Broadcast ////////////////////////////////////
        // channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // String message = "Hello World ( " + i + " )";//getMessage(argv);
        //
        //    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        //    System.out.println(" [x] Sent '" + message + "'");
        /////////////////////////////////////////////////////////////////////////////

        //// Filter Routing ( Direct exchange ) /////////////////
        //  channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        //  String message = "Hello World ( " + i + " )";//getMessage(argv);
        //  --  severity is the routing key
        //  String severity = "info";
        //  channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        //    severity = "warning";
        //    channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        //////////////////////////////////////////////////////////////////////////////////

        ////  Topic Exchange //////////////////////////////

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String routingKey = "advice.info";

        for(int i = 0; i < 15; i ++) {
            String message = "info : Hello World ( " + i + " )";//getMessage(argv);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }
        routingKey = "advice.warning";
        for(int i = 0; i < 15; i ++) {
            String message = "warning : Hello World ( " + i + " )";//getMessage(argv);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }

        routingKey = "sensor.warning";
        for(int i = 0; i < 15; i ++) {
            String message = "sensor warning : Hello World ( " + i + " )";//getMessage(argv);
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        }

        channel.close();
        connection.close();


    }

    //Utilities method for console message handling
    private static String getMessage(String[] strings){
        if (strings.length < 1)
            return "info: Hello World!";
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
