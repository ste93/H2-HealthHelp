package core.dbmanager;

import core.SensorType;
import core.UserRole;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.dbmanager.h2application.User;
import core.dbmanager.h2application.UserBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.Assert.*;

/** Testing of function's H2dbManager
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerProvaTest {
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
                    userBuilder.idCode = "federico.Fedeli";
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

    @Test
    public void SensorTypeValueTest() throws JSONException {


        String message = "\"patientId\": \"chiara.lucchi\",\n" +
                "   \"value\": 37,\n" +
                "   \"unit\": \"gradi\",\n" +
                "   \"timestamp\": \"2018-01-01 09:00\",\n" +
                "   \"output\": \n" +
                "       \"level\": 1,\n" +
                "       \"description\": \"temperatura più alta del solito\"";

        /* Check an adding of a values related to a particular sensor type and patient */
        assertTrue(H2manager.addSensorValue("chiara.lucchi", SensorType.TEMPERATURE, message));
        assertTrue(H2manager.addSensorValue("chiara.lucchi", SensorType.TEMPERATURE, message));
        assertFalse(H2manager.addSensorValue("federico.fedeli", SensorType.TEMPERATURE, message));
        assertFalse(H2manager.addSensorValue("federico.fedeli", SensorType.TEMPERATURE, message));

        /* Check a removing of values related to a particular sensor type and patient */
        /* delete on dates' range */
       // assertTrue(H2manager.deleteValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1998-01-01 09:00"), Optional.of("2000-01-01 11:00")));
       // assertTrue(H2manager.deleteValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("2004-01-01 11:00"), Optional.empty()));
        /* delete all values of sensor type  */
       // assertTrue(H2manager.deleteValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.empty(), Optional.empty()));
        /* Check the error code to values' get. The JSONArray for response code 200 si already test with Postman tool */
        try {
            H2manager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("34789"), Optional.empty());
        } catch (Exception e) {
            assertEquals("500",e.getMessage());
        }
    }

    @Test
    public void AdvicesTest() throws JSONException, ParseException {
     /*   String message = new JSONObject().put("patientId","giulia.lucchi")
                                        .put("doctorId", "mario.rossi")
                                        .put("advice", "laaaa")
                                        .put("timestamp", new Date().toInstant().toString()).toString();*/

       /* String message =  new StringBuffer("\"patientId\":").append("\"cicciobello.lucchi\", ")
              .append("\"doctorId\":").append("\"mario.rossi\", ")
                .append("\"advice\":").append("\"laaaa\", ").append("\"timestamp\":")
                .append("\"2018-01-01 11:30\"").toString();*/

      String message = "\"patientId\":\"giulia.lucchi\", \"doctorId\":\"mario.lucchi\", \"advice\":\"cnncdeij\", \"timestamp\":\"2018-11-01 11:00\"";


        // = "{\"patientId\":\"giulia.lucchi\", \"doctorId\":\"mario.rossi\", \"advice\":\"laaaa\", \"timestamp\":"+new Date().toInstant().toString()+"}" ;

        assertTrue(H2manager.addAdvice(message));


    }

    @Test
    public void drugsTest(){

        String message = "\"doctorId\":\"mario.lucchi\", \"drugName\":\"okii\", \"timestamp\":\"2018-11-01 11:00\"" ;
        assertTrue(H2manager.addDrug("giulia.lucchi",message));

    }
}
