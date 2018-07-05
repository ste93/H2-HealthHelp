package core.pubsub.core;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

public interface TopicSubscriber {

    Channel getChannel();

    void setConsumer(Consumer consumer);

}
