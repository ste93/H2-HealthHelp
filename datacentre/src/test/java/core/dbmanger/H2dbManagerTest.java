package core.dbmanger;

import core.UserRole;
import core.dbmanager.H2dbManager;
import core.dbmanager.H2dbManagerImpl;
import core.dbmanager.User;
import core.dbmanager.UserBuilder;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/** Testing of function's H2dbManager
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerTest {
    private H2dbManager H2manager = new H2dbManagerImpl();

    @Test
    public void AuthenticationTest() {
        User user = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = "giulia.lucchi";
                    userBuilder.name = "Giulia";
                    userBuilder.surname = "Lucchi";
                    userBuilder.cf = "LCCGLI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="lucchigiulia@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.PATIENT.getRole();
                })
                .createUser();

        assertTrue(H2manager.registration(user));
    }
}
