package core.dbmanager.h2dbManager;

import core.SensorType;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Optional;

/**
 * Maps the RESTful API's functionality related to the management of sensors and sensor data in Java interface.
 *
 * @author Giulia Lucchi
 */
public interface DataSensorManager {

    /**
     * Returns the list of sensor's types.
     *
     * @param idPatient patient's identifier
     *
     * @throws Exception json containing error code
     * @return JSONArray of sensor types' string
     *         otherwise a JSONObject including the error code
     */
    JSONArray getSensorsType(final String idPatient) throws Exception;

    /**
     * Adds a sensor type related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type added
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addNewSensorType(final String idPatient, final SensorType sensorType);

    /**
     * Adds a value of a particular sensor type.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type related to a value
     * @param message sensor type's value to add
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addSensorValue(final String idPatient, final SensorType sensorType, final String message) ;

    /**
     * Delete a particular value or a values' set on a particular range of date.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type related to a value or values' set
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean deleteValues(final String idPatient, final SensorType sensorType, final Optional<String> start, final Optional<String> end);

    /**
     * Returns a particular value or a values' set on a particular range of date.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type related to a value or values' set
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of a value or an values' set, represented by JSONObject
     */
    JSONArray getSensorValues(final String idPatient, final SensorType sensorType, final Optional<String> start, final Optional<String> end) throws JSONException, Exception;

}
