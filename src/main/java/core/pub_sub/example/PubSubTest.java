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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + sub1.getReceivedMessage());
        System.out.println(" sub2 : " + sub2.getReceivedMessage());
        System.out.println();


        pub.publishMessage("test2 (solo sub 1)", "mypatient.warning");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + sub1.getReceivedMessage());
        System.out.println(" sub2 : " + sub2.getReceivedMessage());
        System.out.println();


        pub.publishMessage("test3 (sub1 sub 2)", "advice.warning");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + sub1.getReceivedMessage());
        System.out.println(" sub2 : " + sub2.getReceivedMessage());
        System.out.println();


        pub.publishMessage("test4 (nessuno)", "mypatient.advice.abcd");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + sub1.getReceivedMessage());
        System.out.println(" sub2 : " + sub2.getReceivedMessage());
        System.out.println();


        pub.publishMessage("test5 (nessuno)", "mypatient.abcd");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + sub1.getReceivedMessage());
        System.out.println(" sub2 : " + sub2.getReceivedMessage());
        System.out.println();

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
