package core.dbmanager;

import core.SensorType;
import core.UserRole;
import core.dbmanager.H2dbManager;
import core.dbmanager.H2dbManagerImpl;
import core.dbmanager.User;
import core.dbmanager.UserBuilder;
import org.json.JSONArray;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** Testing of function's H2dbManager
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerTest {
    private H2dbManager H2manager = new H2dbManagerImpl();

    @Test
    public void RegistrationTest() {
        User patientUser = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = "chiara.lucchi";
                    userBuilder.name = "Chiara";
                    userBuilder.surname = "Lucchi";
                    userBuilder.cf = "LCCGLI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="lucchigiulia@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.PATIENT.getRole();
                })
                .createUser();
        /* registration of patient*/
        assertTrue(H2manager.registration(patientUser));
        /* doesn't register the new patient, because already inserted*/
        assertFalse(H2manager.registration(patientUser));

        User doctorUser = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = "Federico.Fedeli";
                    userBuilder.name = "Federico";
                    userBuilder.surname = "Fedeli";
                    userBuilder.cf = "LCCGLI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="lfgsjdghsj@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.DOCTOR.getRole();
                })
                .createUser();
        /* registration of doctor*/
        assertTrue(H2manager.registration(doctorUser));
        /* doesn't register the new doctor, because already inserted*/
        assertFalse(H2manager.registration(doctorUser));
    }

    @Test
    public void LoginTest() {
        /* Patient's login*/
        assertTrue(H2manager.login("giulia.lucchi","ciao",UserRole.PATIENT));
        /* Doctor's login*/
        assertTrue(H2manager.login("chiara.lucchi","ciao",UserRole.DOCTOR));
        /* user's login not authorized*/
        assertTrue(H2manager.login("federico.lucchi","ciao",UserRole.PATIENT));
    }

    @Test
    public void SensorTypesTest() throws Exception {
        /* Check the array of sensor types*/
        JSONArray array = new JSONArray().put("glycemia").put("temperature").put("pressure");
        assertEquals(array.get(0) ,H2manager.getSensorsType("giulia.lucchi").get(0));
        assertEquals(array.get(1) ,H2manager.getSensorsType("giulia.lucchi").get(1));

        /* Add new sensor type*/
        assertTrue(H2manager.addNewSensorType("giulia.lucchi", SensorType.PRESSURE));
        /* Check the adding of new sensor type*/
        assertEquals(array.get(2) ,H2manager.getSensorsType("giulia.lucchi").get(2));

    }
}
