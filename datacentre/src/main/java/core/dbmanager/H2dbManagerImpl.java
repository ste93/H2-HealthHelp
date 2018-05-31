package core.dbmanager;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import core.SensorType;
import core.UserRole;
import org.glassfish.jersey.client.ClientResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.Json;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.security.URIParameter;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

/** Implements H2dbManager interface
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerImpl implements H2dbManager {

    private static Client CLIENT =ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.H2_ROUTE.getPath());

    private static WebTarget H2_REGISTRATION = TARGET.path("/registration");
    private static WebTarget H2_LOGIN = TARGET.path("/login");
    private static WebTarget SENSOR_TYPE = TARGET.path("/sensors");
    private static WebTarget SENSOR_VALUES = SENSOR_TYPE.path("/values");
    private static WebTarget H2_ADVICE = TARGET.path("/advices");
    private static WebTarget H2_DRUGS = TARGET.path("/drugs");

    /**
     * Manages user registration.
     *
     * @param user user object that encapsulates the parameters required to save the user.
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean registration(final User user) {
        Response response = H2_REGISTRATION.queryParam("idCode",user.idCode)
                                            .queryParam("name", user.name)
                                            .queryParam("surname", user.surname)
                                            .queryParam("password", user.password)
                                            .queryParam("cf", user.cf)
                                            .queryParam("phone", user.phones)
                                            .queryParam("mail", user.mail)
                                            .queryParam("role", user.role)
                                            .request()
                                            .post(Entity.json(""));

       return response.getStatus()==200;
    }

    /** Manages patient's or doctor's login.
     *
     * @param idCode patient or doctor identifier
     * @param password patient or doctor password
     * @param role user's role, rapresented in Enum UserRole.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    @Override
    public boolean login(final String idCode, final String password, UserRole role) {
        Response response = H2_LOGIN.queryParam("idCode", idCode)
                                        .queryParam("password", password)
                                        .queryParam("role", role.getRole())
                                        .request()
                                        .get();

        return response.getStatus()==200;
    }

    /**
     * Returns the list of sensor's types.
     *
     * @param idPatient patient's identifier
     *
     * @throws Exception json containing error code
     * @return JSONArray of sensor types' string
     *         otherwise a JSONObject including the error code
     */
    @Override
    public JSONArray getSensorsType(final String idPatient)throws Exception {
        JSONObject json;

        Invocation.Builder request = SENSOR_TYPE.queryParam("idCode", idPatient)
                                        .request();
        Integer  responseCode = request.get().getStatus();

        if (responseCode== 200) {
            json = new JSONObject(request.get(String.class));
        } else {
            json = new JSONObject();
            json.put("error ", responseCode);
            throw new Exception("" + responseCode);
        }

        return json.getJSONArray("sensors") ;
    }

    /**
     * Add a sensor type related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type added
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    @Override
    public boolean addNewSensorType(final String idPatient, final SensorType sensorType) {
        Response response = SENSOR_TYPE.queryParam("idCode", idPatient)
                                        .queryParam("type", sensorType.getType() )
                                        .request()
                                        .put(Entity.json(""));


        return  response.getStatus()==200;
    }

    /**
     * Adds a value of a particular sensor type.
     *
     * @param idPatient  patient's identifier
     * @param sensorType sensor type related to a value
     * @param message    sensor type's value to add
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addSensorValue(String idPatient, SensorType sensorType, JSONObject message) {
        Response response = SENSOR_VALUES.queryParam("idCode", idPatient)
                                            .queryParam("type", sensorType.getType())
                                            .queryParam("message", message.toString())
                                            .request()
                                            .post(Entity.json(null));

        return response.getStatus()==200;
    }

    /**
     * Delete a particular value or a values' set on a particular range of date.
     *
     * @param idPatient  patient's identifier
     * @param sensorType sensor type related to a value or values' set
     * @param start      an optional start date to search on the range of date
     * @param end        an optional end date to search on the range of date
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean deleteValues(String idPatient, SensorType sensorType, Optional<String> start, Optional<String> end) {
        Response response;
        if(start.isPresent() && end.isPresent()){
            response = SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .queryParam("start", start.get())
                    .queryParam("end", end.get())
                    .request()
                    .delete();

        }else if(start.isPresent() && !(end.isPresent())){
            response =  SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .queryParam("start", start.get())
                    .request()
                    .delete();
        }else{
            response =  SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .request()
                    .delete();
        }

        return response.getStatus()==200;
    }

    /**
     * Returns a particular value or a values' set on a particular range of date.
     *
     * @param idPatient  patient's identifier
     * @param sensorType sensor type related to a value or values' set
     * @param start      an optional start date to search on the range of date
     * @param end        an optional end date to search on the range of date
     * @return JSONArray of a value or an values' set, represented by JSONObject
     */
    @Override
    public JSONArray getValues(String idPatient, SensorType sensorType, Optional<String> start, Optional<String> end) throws Exception {
        Invocation.Builder request;
        if(start.isPresent() && end.isPresent()){
            request = SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .queryParam("start", start.get())
                    .queryParam("end", end.get())
                    .request();

        }else if(start.isPresent() && !(end.isPresent())){
            request =  SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .queryParam("start", start.get())
                    .request();
        }else{
            request =  SENSOR_VALUES.queryParam("idCode", idPatient)
                    .queryParam("type", sensorType.getType())
                    .request();
        }

        Integer  responseCode = request.get().getStatus();

        JSONArray json;
        if (responseCode== 200) {
            json = new JSONArray(request.get(String.class));
        } else {
            json = new JSONArray();
            json.put(responseCode);
            throw new Exception("" + responseCode);
        }

        return json;
    }

    /**
     * Returns all advices related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @return JSONArray of advices' set, represented by JSONObject
     */
    @Override
    public JSONArray getAdvices(String idPatient) throws Exception {
        Invocation.Builder request = H2_ADVICE.queryParam("idCode", idPatient)
                                                .request();
        Integer  responseCode = request.get().getStatus();

        JSONArray json;
        if (responseCode== 200) {
            json = new JSONArray(request.get(String.class));
        } else {
            json = new JSONArray();
            json.put(responseCode);
            throw new Exception("" + responseCode);
        }

        return json;
    }

    /**
     * Adds an advice related to a particular patient.
     *
     * @param message a JSONObject containing advice, patient and doctor id
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addAdvice(String message) {
       // System.out.println(new JSONObject("{patientId:giulia.lucchi,doctorId:mario.rossi,advice:giulia,timestamp:2018-02-22 09:10, }"));

        Response response = H2_ADVICE
                .queryParam("message", message)
                .request().buildPost(null).invoke();

        return response.getStatus()==200;
    }

    /**
     * Returns all described drugs related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @return JSONArray of drugs' set, represented by JSONObject
     */
    @Override
    public JSONArray getDrugs(final String idPatient, final Optional<String> start, final Optional<String> end) throws Exception {
        Invocation.Builder request;
        if(start.isPresent() && end.isPresent()){
            request = H2_DRUGS.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .queryParam("end", end.get())
                    .request();

        }else if(start.isPresent() && !(end.isPresent())){
            request = H2_DRUGS.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .request();
        }else{
            request = H2_DRUGS.queryParam("idCode", idPatient)
                    .request();
      }

        Integer  responseCode = request.get().getStatus();

        JSONArray json;
        if (responseCode== 200) {
            json = new JSONArray(request.get(String.class));
        } else {
            json = new JSONArray();
            json.put(responseCode);
            throw new Exception("" + responseCode);
        }

        return json;
    }

    /**
     * Adds a drug related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param message   JSONObject containing a drug's name and doctor's id
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addDrug(String idPatient, Json message) {
        return false;
    }


}
