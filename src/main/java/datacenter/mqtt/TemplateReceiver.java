package datacenter.mqtt;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 *  A class used to test rabbitmq basic functionalities and as a template for it.
 *
 * @author manuBottax
 */
public class TemplateReceiver {
    // the name has to be changed every time you change the exchange type
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ///// initial configuration //////
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /////////////////// direct receiving ( no routing ) //////////////
        //   --  [Persistence 1 ] true state that queue is saved on disk, so it remain even if the mqtt server restart
        //   --  N.B. if the queue not exist it create it, otherwise it connect to it.
        //   channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        //   System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //
        //   channel.basicQos(1); // accept only one unack-ed message at a time ( parameter is the number of msg accepted )
        //
        //   final Consumer consumer = new DefaultConsumer(channel) {
        //    @Override
        //    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        //        String message = new String(body, "UTF-8");
        //        System.out.println(" [x] Received '" + message + "'");
        //        try {
        //            doSomething(message);
        //        }
        //        catch (InterruptedException ex ) {}
        //        finally {
        //            System.out.println(" [x] Done");
        //            //If the worker die before complete the message handling the message is send to another one.
        //            channel.basicAck(envelope.getDeliveryTag(), false);
        //        }
        //    }
        //  };
        //
        //  boolean autoAck = false;
        //  channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        //////////////////////////////////////////////////////////////////////////////////

        //// Simple Broadcast ////////////////////////////////////
        //  channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //  --  generate a temporary queue and return the (random) name -> every receiver has his own queue
        //  String queueName = channel.queueDeclare().getQueue();
        //  --  bind the queue with every exchange I want to listen
        //  channel.queueBind(queueName, EXCHANGE_NAME, "");
        //  System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //
        //  Consumer consumer = new DefaultConsumer(channel) {
        //    @Override
        //    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        //        String message = new String(body, "UTF-8");
        //        System.out.println(" [x] Received '" + message + "'");
        //    }
        //  };
        //  channel.basicConsume(queueName, true, consumer);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //// Filter Routing ( Direct exchange ) /////////////////
        //   channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //   String queueName = channel.queueDeclare().getQueue();

        /* console acquisition
        if (argv.length < 1){
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        for(String severity : argv){
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        */
        /*
        // it will receive both info and warning because the two different binds
        channel.queueBind(queueName, EXCHANGE_NAME, "info");
        channel.queueBind(queueName, EXCHANGE_NAME, "warning");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);

        */


        ////  Topic Exchange //////////////////////////////
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        String bindingKey = "*.warning";
        channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received -> " + envelope.getRoutingKey() + " : ' " + message + " '");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}