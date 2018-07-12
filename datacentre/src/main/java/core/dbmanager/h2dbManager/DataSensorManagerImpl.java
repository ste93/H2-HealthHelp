package core.dbmanager.h2dbManager;

import core.SensorType;
import core.dbmanager.URIrequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

/** Implements DataSensorManager interface.
 *
 * @author Giulia Lucchi
 */
public class DataSensorManagerImpl implements DataSensorManager {

    private static Client CLIENT =ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.H2_ROUTE.getPath());

    private static WebTarget SENSOR_TYPE = TARGET.path("/sensors");
    private static WebTarget SENSOR_VALUES = SENSOR_TYPE.path("/values");

    private H2dbManagerUtils utils = new H2dbManagerUtils();

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

        int  responseCode = request.get().getStatus();

        return utils.responseToJSONArray(responseCode, request);
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
    public JSONArray getSensorValues(String idPatient, SensorType sensorType, Optional<String> start, Optional<String> end) throws Exception {
        Invocation.Builder request = this.sensorValueOnRange(idPatient,start,end,sensorType);

        int responseCode = request.get().getStatus();
        return utils.responseToJSONArray(responseCode, request);
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

}
