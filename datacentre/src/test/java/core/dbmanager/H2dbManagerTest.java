package core.dbmanager;

import core.SensorType;
import core.UserRole;
import core.dbmanager.h2dbManager.*;
import core.pubsub.message.MessagesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/** Testing of function's DataSensorManager
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerTest {
    private static DataSensorManager dataSensorManager = new DataSensorManagerImpl();
    private static AdviceManager adviceManager = new AdviceManagerImpl();
    private static DrugManager drugManager = new DrugManagerImpl();
    private static UserManager userManager = new UserManagerImpl();

    private static MessagesUtils utils = new MessagesUtils();

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
        userManager.registration(patientUser);
        userManager.registration(doctorUser);

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

        // valid registration of patient
        assertTrue(userManager.registration(patientUser));
        // doesn't register the new patient, because already inserted
        assertFalse(userManager.registration(patientUser));

        /* -------------------  PATIENT LOGIN  -------------------- */
        //valid login
        assertTrue(userManager.login(idCode, "ciao", UserRole.PATIENT));
        //invalid login
        assertFalse(userManager.login(idCode, "ciao12345", UserRole.PATIENT));

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

        // valid registration of doctor
        assertTrue(userManager.registration(doctorUser));
        // doesn't register the new doctor, because already inserted
        assertFalse(userManager.registration(doctorUser));

        /* -----------------  DOCTOR LOGIN  ----------------- */
        //valid login
        assertTrue(userManager.login(idCode, "ciao", UserRole.DOCTOR));
        //invalid login
        assertFalse(userManager.login(idCode, "ciao12345", UserRole.DOCTOR));
    }

    /**
     * Tests get, put of sensors' types.
     */
    @Test
    public void SensoTypeAndValueTest() throws JSONException {

       /* -----------------  ADD A SENSORS TYPE  ----------------- */
       try {
           // Add new sensor type and after check insert of sensor type and get() operation
           assertTrue(dataSensorManager.addNewSensorType("giulia.lucchi", SensorType.GLYCEMIA));
           assertTrue(dataSensorManager.addNewSensorType("giulia.lucchi", SensorType.TEMPERATURE));
           assertTrue(dataSensorManager.addNewSensorType("giulia.lucchi", SensorType.PRESSURE));

       } catch (Exception e) {
           e.getMessage();
       }
    }

    /**
     * Tests post, get and delete sensors' type values.
     */
    @Test
    public void SensorTypeValueTest() throws JSONException {

        /* -----------------  ADD A SENSOR VALUE  ----------------- */
        String temperatureMessage = new JSONObject()
                                        .put("patientId", "giulia.lucchi")
                                        .put("value", 37)
                                        .put("unit", "gradi")
                                        .put("timestamp", "2018-01-01 09:00" )
                                        .put("output", new JSONObject()
                                                            .put("level", 2)
                                                            .put("description", "temp altina"))
                                        .toString();


        String temperatureToInsert = utils.convertToFormatApi(temperatureMessage);

        String temperatureMessage1 = new JSONObject()
                .put("patientId", "giulia.lucchi")
                .put("value", 40)
                .put("unit", "gradi")
                .put("timestamp", "2018-01-01 09:00" )
                .put("output", new JSONObject()
                        .put("level", 3)
                        .put("description", "temp altina"))
                .toString();


        String temperatureToInsert1 = utils.convertToFormatApi(temperatureMessage1);

        String glycemiaMessage = new JSONObject()
                .put("patientId", "giulia.lucchi")
                .put("value", 130)
                .put("unit", "gradi")
                .put("timestamp", "2018-01-01 09:00" )
                .put("output", new JSONObject()
                        .put("level", 2)
                        .put("description", "glycemia altina"))
                .toString();


        String glycemiaToInsert = utils.convertToFormatApi(glycemiaMessage);

        assertTrue(dataSensorManager.addSensorValue("giulia.lucchi", SensorType.GLYCEMIA, glycemiaToInsert));
        assertTrue(dataSensorManager.addSensorValue("giulia.lucchi", SensorType.TEMPERATURE, temperatureToInsert));
        assertTrue(dataSensorManager.addSensorValue("giulia.lucchi", SensorType.TEMPERATURE, temperatureToInsert1));
        // add a sensor type value to a not existing patient
        assertFalse(dataSensorManager.addSensorValue("federico.fedeli", SensorType.TEMPERATURE, temperatureToInsert));

        /* -----------------  DELETE AND GET SENSOR VALUE  ----------------- */
        //delete all values
        assertTrue(dataSensorManager.deleteValues("giulia.lucchi", SensorType.GLYCEMIA, Optional.empty(), Optional.empty()));
        //delete on range
        assertTrue(dataSensorManager.deleteValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1993-01-01 09:00"), Optional.of("1996-01-01 11:00")));

        //checks removing with a get sensor values
        JSONArray sensors = null;
        try {
            sensors= dataSensorManager.getSensorValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("1993-01-01 09:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
            assertEquals("500",(new JSONArray(e.getMessage())).getString(0));
        }

        assertEquals("[]", sensors.toString());

        //check remaining value in sensors
        JSONArray remainingfrom2000 = null;
        try {
            sensors= dataSensorManager.getSensorValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.empty(), Optional.empty());
            remainingfrom2000 = dataSensorManager.getSensorValues("giulia.lucchi", SensorType.TEMPERATURE, Optional.of("2000-01-01 00:00"), Optional.empty());
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
        assertTrue(drugManager.addDrug("giulia.lucchi", message));
        assertTrue(drugManager.addDrug("giulia.lucchi", message1));

        /* -----------------  GET DRUGS  ----------------- */
        //check adding and get all drugs
        try {
            assertTrue(drugManager.getDrugs("giulia.lucchi", Optional.empty(), Optional.empty()).length() >= 2);
        } catch (Exception e) {
            assertEquals("500",(new JSONObject(e.getMessage())).getString("code"));
        }

        //check on range
        JSONArray drugs = null;
        try {
            drugs = drugManager.getDrugs("giulia.lucchi",Optional.of("1993-01-01 11:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
            assertEquals("500", e.getMessage());
        };
        String expectedMessage = "{\"doctorId\":\"mario.rossi\",\"drugName\":\"okii\",\"timestamp\":\"1994-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage, drugs.get(0).toString());

        //from year 2000 to today year
        try {
            drugs = drugManager.getDrugs("giulia.lucchi",Optional.of("2000-01-01 11:00"), Optional.empty());
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
        assertTrue(adviceManager.addAdvice(message));
        assertTrue(adviceManager.addAdvice(message1));

        /* -----------------  GET ADVICE  ----------------- */
        //check adding and get all advices
        try {
            assertTrue(adviceManager.getAdvices("giulia.lucchi", Optional.empty(), Optional.empty()).length() >= 2);
        } catch (Exception e) {
            assertEquals("500",(new JSONObject(e.getMessage())).getString("code"));
        }

        //check on range
        JSONArray advices = null;
        try {
            advices = adviceManager.getAdvices("giulia.lucchi",Optional.of("1993-01-01 11:00"), Optional.of("2000-01-01 11:00"));
        } catch (Exception e) {
            assertEquals("500",(new JSONObject(e.getMessage())).getString("code"));
        };
        String expectedMessage = "{\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"1994-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage, advices.get(0).toString());

        //from year 2000 to today year
        try {
            advices = adviceManager.getAdvices("giulia.lucchi",Optional.of("2000-01-01 11:00"), Optional.empty());
        } catch (Exception e) {
            assertEquals("500",(new JSONObject(e.getMessage())).getString("code"));
        }
        String expectedMessage1 = "{\"doctorId\":\"mario.rossi\",\"advice\":\"inizia a prendere l'OKII\",\"timestamp\":\"2018-01-01T10:00:00.000Z\"}";
        assertEquals(expectedMessage1, advices.get(0).toString());

    }
    
}
