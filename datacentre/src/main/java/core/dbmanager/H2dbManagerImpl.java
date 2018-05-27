package core.dbmanager;

import core.SensorType;
import core.UserRole;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    private static  WebTarget SENSOR_VALUES = SENSOR_TYPE.path("/values");

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

       return response.getStatus()==200 ?  true :  false;
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
        return response.getStatus()==200 ? true : false;
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


        return  response.getStatus()==200 ? true : false;
    }

    @Override
    public boolean addSensorValue(String idPatient, SensorType sensorType, Json message) {
        return false;
    }

    @Override
    public boolean deleteValues(String idPatient, SensorType sensorType, Optional<Date> start, Optional<Date> end) {
        return false;
    }

    @Override
    public boolean getValues(String idPatient, SensorType sensorType, Optional<Date> start, Optional<Date> end) {
        return false;
    }

    @Override
    public ArrayList<Json> getAdvices(String idPatient) {
        return null;
    }

    @Override
    public boolean addAdvice(Json message) {
        return false;
    }

    @Override
    public ArrayList<Json> getDrugs(String idPatient) {
        return null;
    }

    @Override
    public boolean addDrug(String idPatient, Json message) {
        return false;
    }
}
