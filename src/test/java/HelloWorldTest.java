import org.junit.Test;

import static org.junit.Assert.*;


/**
 * +-------------------------------------------------------------------------------------------------------+
 * ¦                                       Feature                            ¦   Junit 4    ¦   Junit 5   ¦
 * ¦--------------------------------------------------------------------------+--------------+-------------¦
 * ¦ Execute before all test methods of the class are executed.               ¦ @BeforeClass ¦ @BeforeAll  ¦
 * ¦ Used with static method.                                                 ¦              ¦             ¦
 * ¦ For example, This method could contain some initialization code          ¦              ¦             ¦
 * ¦-------------------------------------------------------------------------------------------------------¦
 * ¦ Execute after all test methods in the current class.                     ¦ @AfterClass  ¦ @AfterAll   ¦
 * ¦ Used with static method.                                                 ¦              ¦             ¦
 * ¦ For example, This method could contain some cleanup code.                ¦              ¦             ¦
 * ¦-------------------------------------------------------------------------------------------------------¦
 * ¦ Execute before each test method.                                         ¦ @Before      ¦ @BeforeEach ¦
 * ¦ Used with non-static method.                                             ¦              ¦             ¦
 * ¦ For example, to reinitialize some class attributes used by the methods.  ¦              ¦             ¦
 * ¦-------------------------------------------------------------------------------------------------------¦
 * ¦ Execute after each test method.                                          ¦ @After       ¦ @AfterEach  ¦
 * ¦ Used with non-static method.                                             ¦              ¦             ¦
 * ¦ For example, to roll back database modifications.                        ¦              ¦             ¦
 * +-------------------------------------------------------------------------------------------------------+
 */
public class HelloWorldTest {

    @Test
    public void test1() {
        assertTrue(1 == 1);
    }

}