import core.pub_sub.TopicPublisher;
import core.pub_sub.TopicSubscriber;
import core.pub_sub.example.ClassTopicSubscriber;
import core.pub_sub.example.LambdaTopicSubsciber;
import org.junit.*;

import static org.junit.Assert.assertTrue;

/**
 * Odio i test in JUnit .....
 *
 */
public class PubSubTest {

    private static TopicPublisher publisher;
    private static TopicSubscriber subscriber1;
    private static TopicSubscriber subscriber2;

    @BeforeClass
    public static void init() {
        publisher = new TopicPublisher("myTest");
        subscriber1 = new LambdaTopicSubsciber("myTest.queue.1","myTest", "*.warning");
        subscriber2 = new ClassTopicSubscriber("myTest.queue.2", "myTest", "advice.#");
    }

    @Test
    public void testSinglePubSub(){

        publisher.publishMessage("test1 (solo sub 1)", "mypatient.warning");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + subscriber1.getReceivedMessage());
        System.out.println(" sub2 : " + subscriber2.getReceivedMessage());
        assertTrue(subscriber1.getReceivedMessage() == 1 && subscriber2.getReceivedMessage() == 0);
    }


    @Test
    public void testSinglePubSub2(){

        publisher.publishMessage("test2 (solo sub 2)", "advice.mypatient.abcd");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + subscriber1.getReceivedMessage());
        System.out.println(" sub2 : " + subscriber2.getReceivedMessage());
        assertTrue(subscriber1.getReceivedMessage() == 1 && subscriber2.getReceivedMessage() == 1);
    }


    @Test
    public void testMultiplePubSub(){
        publisher.publishMessage("test3 (sub1 sub 2)", "advice.warning");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" sub1 : " + subscriber1.getReceivedMessage());
        System.out.println(" sub2 : " + subscriber2.getReceivedMessage());
        assertTrue(subscriber1.getReceivedMessage() == 2 && subscriber2.getReceivedMessage() == 2);
    }



    @Test
    public void testBinding(){
        publisher.publishMessage("test4 (nessuno)", "mypatient.advice.abcd");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(" sub1 : " + subscriber1.getReceivedMessage());
        System.out.println(" sub2 : " + subscriber2.getReceivedMessage());
        assertTrue(subscriber1.getReceivedMessage() == 1 && subscriber2.getReceivedMessage() == 1);

        publisher.publishMessage("test5 (nessuno)", "mypatient.abcd");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" sub1 : " + subscriber1.getReceivedMessage());
        System.out.println(" sub2 : " + subscriber2.getReceivedMessage());
        assertTrue(subscriber1.getReceivedMessage() == 1 && subscriber2.getReceivedMessage() == 1);
    }

    @AfterClass
    public static void close () {
        publisher.close();
        subscriber1.close();
        subscriber2.close();
    }
}
