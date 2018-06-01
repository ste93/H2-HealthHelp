package core.dbmanager;

import core.SensorType;
import core.UserRole;
import core.dbmanager.h2application.H2dbManager;
import core.dbmanager.h2application.H2dbManagerImpl;
import core.dbmanager.h2application.User;
import core.dbmanager.h2application.UserBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/** Testing of function's H2dbManager
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerTest {
    private static H2dbManager H2manager = new H2dbManagerImpl();

    @BeforeClass
    public static void basicRegistration(){
        User patientUser = new UserBuilder()
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

        User doctorUser = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = "mario.rossi";
                    userBuilder.name = "Mario";
                    userBuilder.surname = "Rossi";
                    userBuilder.cf = "RSSMRI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="mariorossi@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.DOCTOR.getRole();
                })
                .createUser();
        H2manager.registration(patientUser);
        H2manager.registration(doctorUser);

    }
     /**
     * Create a valid patient's account and login with it and get the patient's information.
     *
     */
    @Test
    public void PatientTest(){
        /* To create a idCode unique */
        String idCode = "giulia.lucchi"+(0+(int)(Math.random()*200));

        /* -----------------  PATIENT REGISTRATION  ----------------- */
        User patientUser = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = idCode;
                    userBuilder.name = "Giulia";
                    userBuilder.surname = "Lucchi";
                    userBuilder.cf = "LCCGLI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="lucchigiulia@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.PATIENT.getRole();
                })
                .createUser();
        /* -------------------  PATIENT LOGIN  -------------------- */

        // valid registration of patient
        assertTrue(H2manager.registration(patientUser));
        // doesn't register the new patient, because already inserted
        assertFalse(H2manager.registration(patientUser));

        /* -----------------  PATIENT INFORMATION  ----------------- */
    }

    /**
     * Create a valid doctor's account and login with it and get the doctor's information.
     */
    @Test
    public void DoctorTest(){
        /* To create a idCode unique */
        String idCode = "mario.lucchi"+(0+(int)(Math.random()*200));

        /* -----------------  DOCTOR REGISTRATION  ----------------- */
        User doctorUser = new UserBuilder()
                .with(userBuilder -> {
                    userBuilder.idCode = idCode;
                    userBuilder.name = "Mario";
                    userBuilder.surname = "Rossi";
                    userBuilder.cf = "RSSMRI45C64S579I";
                    userBuilder.password = "ciao";
                    userBuilder.mail="mariorossi@gmail.com";
                    userBuilder.phones="05476143 3246543123";
                    userBuilder.role = UserRole.DOCTOR.getRole();
                })
                .createUser();

        /* -----------------  DOCTOR LOGIN  ----------------- */

        // valid registration of doctor
        assertTrue(H2manager.registration(doctorUser));
        // doesn't register the new doctor, because already inserted
        assertFalse(H2manager.registration(doctorUser));

        /* -----------------  DOCTOR INFORMATION  ----------------- */
    }

    /**
     * Tests get, put of sensors' types.
     */
    @Test
    public void SensoTypeAndValueTest(){

       /* -----------------  ADD A SENSORS TYPE  ----------------- */

       // Add new sensor type
       assertTrue(H2manager.addNewSensorType("giulia.lucchi", SensorType.GLYCEMIA));
       assertTrue(H2manager.addNewSensorType("giulia.lucchi", SensorType.TEMPERATURE));
       assertTrue(H2manager.addNewSensorType("giulia.lucchi", SensorType.PRESSURE));

       //Check insert of sensor type and get() operation
       JSONArray expectedArray = new JSONArray().put("glycemia").put("temperature").put("pressure");
        try {
            assertEquals(expectedArray.get(0) ,H2manager.getSensorsType("giulia.lucchi").get(0));
            assertEquals(expectedArray.get(1) ,H2manager.getSensorsType("giulia.lucchi").get(1));
            assertEquals(expectedArray.get(2) ,H2manager.getSensorsType("giulia.lucchi").get(2));
        } catch (Exception e) {
            assertEquals("500",e.getMessage());
        }

    }

    /**
     * Tests post, get and delete sensors' type values.
     */
    @Test
    public void SensorTypeValueTest() throws JSONException {

        /* -----------------  ADD A SENSOR VALUE  ----------------- */
        String temperatureMessage = "\"patientId\": \"giulia.lucchi\",\n" +
                "   \"value\": 37,\n" +
                "   \"unit\": \"gradi\",\n" +
                "   \"timestamp\": \"2018-01-01 09:00\",\n" +
                "   \"output\": \n" +
                "       \"level\": 1,\n" +
                "       \"description\": \"temperatura più alta del solito\"";

        String temperatureMessage1 = "\"patientId\": \"giulia.lucchi\",\n" +
                "   \"value\": 37,\n" +
                "   \"unit\": \"gradi\",\n" +
                "   \"timestamp\": \"1994-01-01 09:00\",\n" +
                "   \"output\": \n" +
                "       \"level\": 1,\n" +
                "       \"description\": \"temperatura più alta del solito\"";

        String gyclemiaMessage= "\"patientId\": \"giulia.lucchi\",\n" +
                "   \"value\": 130,\n" +
                "   \"unit\": \"mg/dl\",\n" +
                "   \"timestamp\": \"2018-01-01 09:00\",\n" +
                "   \"output\": \n" +
                "       \"level\": 2,\n" +
                "       \"description\": \"glicemia alta\"";

        assertTrue(H2manager.addSensorValue("giulia.lucchi", SensorType.GLYCEMIA, gyclemiaMessage));
        assertTrue(H2manager.addSensorValue("giulia.lucchi", SensorType.TEMPERATURE, temperatureMessage));
        assertTrue(H2manager.addSensorValue("giulia.lucchi", SensorType.TEMPERATURE, temperatureMessage1));
        // add a sensor type value to a not existing patient
        assertFalse(H2manager.addSensorValue("federico.fedeli", SensorType.TEMPERATURE, temperatureMessage));

        /* -----------------  DELETE AND GET SENSOR VALUE  ----------------- */
        //delete all values
        assertTrue(H2manager.deleteValues("giulia.lucchi", SensorType.GLYCEMIA, Optional.empty(), Optional.empty()));
        //delete on range
        assertTrue(H2manager.deleteValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1993-01-01 09:00"), Optional.of("1996-01-01 11:00")));

        //checks removing with a get sensor values
        JSONArray sensors = null;
        try {
            sensors= H2manager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1993-01-01 09:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
            assertEquals("500",e.getMessage());
        }

        assertEquals("[]", sensors.toString());

        //check remaining value in sensors
        JSONArray remainingfrom2000 = null;
        try {
            sensors= H2manager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.empty(), Optional.empty());
            remainingfrom2000 = H2manager.getValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("2000-01-01 00:00"), Optional.empty());
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        }
    }

    @Test
    public void DrugTest() throws JSONException {
        //accept json without brackets
        String message = "\"doctorId\":\"mario.rossi\", \"drugName\":\"okii\", \"timestamp\":\"1994-01-01 11:00\"" ;
        String message1 = "\"doctorId\":\"mario.rossi\", \"drugName\":\"okii\", \"timestamp\":\"2018-01-01 11:00\"" ;

        /* -----------------  ADD DRUGS  ----------------- */
        assertTrue(H2manager.addDrug("giulia.lucchi", message));
        assertTrue(H2manager.addDrug("giulia.lucchi", message1));

        /* -----------------  GET DRUGS  ----------------- */
        //check adding and get all drugs
        try {
            assertTrue(H2manager.getDrugs("giulia.lucchi", Optional.empty(), Optional.empty()).length() >= 2);
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        }

        //check on range
        JSONArray drugs = null;
        try {
            drugs = H2manager.getDrugs("giulia.lucchi",Optional.of("1993-01-01 11:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        };
        String expectedMessage = "{\"doctorId\":\"mario.rossi\",\"drugName\":\"okii\",\"timestamp\":\"1994-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage, drugs.get(0).toString());

        //from year 2000 to today year
        try {
            drugs = H2manager.getDrugs("giulia.lucchi",Optional.of("2000-01-01 11:00"), Optional.empty());
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        }
        String expectedMessage1 = "{\"doctorId\":\"mario.rossi\",\"drugName\":\"okii\",\"timestamp\":\"2018-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage1, drugs.get(0).toString());

    }

    @Test
    public void AdviceTest() throws JSONException {
        //accept json without brackets
        String message ="\"patientId\":\"giulia.lucchi\",\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"1994-01-01 11:00\"";
        String message1 ="\"patientId\":\"giulia.lucchi\",\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"2018-01-01 11:00\"";
        /* -----------------  ADD ADVICE  ----------------- */
        assertTrue(H2manager.addAdvice(message));
        assertTrue(H2manager.addAdvice(message1));

        /* -----------------  GET ADVICE  ----------------- */
        //check adding and get all advices
        try {
            assertTrue(H2manager.getAdvices("giulia.lucchi", Optional.empty(), Optional.empty()).length() >= 2);
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        }

        //check on range
        JSONArray advices = null;
        try {
            advices = H2manager.getAdvices("giulia.lucchi",Optional.of("1993-01-01 11:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
           assertEquals("500", e.getMessage());
        };
        String expectedMessage = "{\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"1994-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage, advices.get(0).toString());

        //from year 2000 to today year
        try {
            advices = H2manager.getAdvices("giulia.lucchi",Optional.of("2000-01-01 11:00"), Optional.empty());
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        }
        String expectedMessage1 = "{\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"2018-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage1, advices.get(0).toString());

    }


}
