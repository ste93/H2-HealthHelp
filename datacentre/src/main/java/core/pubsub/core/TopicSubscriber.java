package core.pubsub.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

/**
 * Base subscriber class for subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 *
 * @author RabbitMQ documentation
 *          modify by   Giulia Lucchi
 *                      Margherita Pecorelli
 */
public interface TopicSubscriber {

    Channel getChannel();

    void setConsumer(Consumer consumer);

}
