package core.pubsub.core;

/**
 * Base subscriber class for publish/subscribe communication.
 * It handle the basic configuration for RabbitMQ client and define a default behaviour.
 *
 * @author RabbitMQ documentation
 *          modify by Manuel Bottazzi
 */
public interface TopicPublisher {

    void publishMessage(String message, String routingKey);

}
