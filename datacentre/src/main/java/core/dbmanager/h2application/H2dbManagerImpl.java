package core.dbmanager.h2application;

import core.SensorType;
import core.UserRole;
import core.dbmanager.URIrequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

/** Implements H2dbManager interface.
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
        Response response = H2_REGISTRATION.queryParam("idCode",user.getIdCode())
                                            .queryParam("name", user.getName())
                                            .queryParam("surname", user.getSurname())
                                            .queryParam("password", user.getPassword())
                                            .queryParam("cf", user.getCf())
                                            .queryParam("phone", user.getPhones())
                                            .queryParam("mail", user.getMail())
                                            .queryParam("role", user.getRole())
                                            .request()
                                            .post(null);

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
     * @param message    sensor type's value to add (format: "KEY":"VALUE" without initial and final brackets)
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addSensorValue(String idPatient, SensorType sensorType, String message) {
        Response response = SENSOR_VALUES.queryParam("idCode", idPatient)
                                            .queryParam("type", sensorType.getType())
                                            .queryParam("message", message)
                                            .request()
                                            .post(null);

        return response.getStatus() == 200;
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

       Response response = this.sensorValueOnRange(idPatient,start,end,sensorType).delete();
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
        Invocation.Builder request = this.sensorValueOnRange(idPatient,start,end,sensorType);

        Integer  responseCode = request.get().getStatus();
        return responseToJSONArray(responseCode, request);
    }

    /**
     * Returns all advices related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of advices' set, represented by JSONObject
     */
    @Override
    public JSONArray getAdvices(String idPatient, Optional<String> start, Optional<String> end) throws Exception {
        Invocation.Builder request = this.drugsOrAdviceOnRange(H2_ADVICE, idPatient, start, end);

        Integer responseCode = request.get().getStatus();
        return responseToJSONArray(responseCode, request);
    }

    /**
     * Adds an advice related to a particular patient.
     *
     * @param message containing advice, patient and doctor id (format: "KEY":"VALUE" without initial and final brackets)
     *
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addAdvice(String message) {

        Response response = H2_ADVICE
                .queryParam("message", message)
                .request()
                .post(null);

        return response.getStatus()==200;
    }

    /**
     * Returns all described drugs related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of drugs' set, represented by JSONObject
     */
    @Override
    public JSONArray getDrugs(final String idPatient, final Optional<String> start, final Optional<String> end) throws Exception {
        Invocation.Builder request = this.drugsOrAdviceOnRange(H2_DRUGS,idPatient, start,end);

        Integer  responseCode = request.get().getStatus();
        return responseToJSONArray(responseCode, request);
    }

    /**
     * Adds a drug related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param message   containing a drug's name and doctor's id (format: "KEY":"VALUE" without initial and final brackets)
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addDrug(String idPatient, String message) {
        Response response = H2_DRUGS
                .queryParam("idCode", idPatient)
                .queryParam("message", message)
                .request()
                .post(null);

        return response.getStatus()==200;
    }

    /**
     * A private class to manage get or delete of sensor's values.
     *
     * @param idPatient  patient's identifier
     * @param sensorType sensor type related to a value or values' set
     * @param start      an optional start date to search on the range of date
     * @param end        an optional end date to search on the range of date
     *
     * @return Invocation.Builder request to a RESTful API
     */
    private Invocation.Builder sensorValueOnRange(String idPatient, Optional<String> start, Optional<String> end, SensorType sensorType){
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
        return request;
    }

    /**
     * A private class to manage the delete od drugs on the range.
     *
     * @param idPatient patient's identifier
     * @param start      an optional start date to search on the range of date
     * @param end        an optional end date to search on the range of date
     *
     * @return Invocation.Builder request to a RESTful API
     */
    private Invocation.Builder drugsOrAdviceOnRange(WebTarget webTarget, String idPatient, Optional<String> start, Optional<String> end){
        Invocation.Builder request;

        if(start.isPresent() && end.isPresent()){
            request = webTarget.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .queryParam("end", end.get())
                    .request();

        }else if(start.isPresent() && !(end.isPresent())){
            request = webTarget.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .request();
        }else{
            request = webTarget.queryParam("idCode", idPatient)
                    .request();
        }
        return request;
    }

    private JSONArray responseToJSONArray(Integer responseCode, Invocation.Builder request) throws Exception {
        JSONArray json;
        if (responseCode== 200) {
            json = new JSONArray(request.get(String.class));
        } else {
            json = new JSONArray();
            json.put(responseCode);
            throw new Exception("" + responseCode);
        }
        return  json;
    }

}
