package core.pubsub.core;

public interface TopicPublisher {

    void publishMessage(String message, String routingKey);

}
