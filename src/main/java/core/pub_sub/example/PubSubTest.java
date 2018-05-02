package core.pub_sub.example;

import core.pub_sub.TopicPublisher;
import core.pub_sub.TopicSubscriber;

public class PubSubTest {

    // potrebbe diventare un test in JUnit ? boh...
    public static void main(String[] args) {
        TopicPublisher pub = new TopicPublisher("myTest");
        TopicSubscriber sub1 = new LambdaTopicSubsciber("myTest.queue.1","myTest", "*.warning");
        TopicSubscriber sub2 = new ClassTopicSubscriber("myTest.queue.2", "myTest", "advice.#");

        pub.publishMessage("test1 (solo sub 2)", "advice.mypatient.abcd");
        pub.publishMessage("test2 (solo sub 1)", "mypatient.warning");
        pub.publishMessage("test3 (sub1 sub 2)", "advice.warning");
        pub.publishMessage("test4 (nessuno)", "mypatient.advice.abcd");
        pub.publishMessage("test5 (nessuno)", "mypatient.abcd");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pub.close();
        sub1.close();
        sub2.close();
    }


}
