package core.dbmanager.h2application;

import core.SensorType;
import core.UserRole;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

/** Manages the client access to RESTful API to access the H2 database.
 *
 * @author Giulia Lucchi
 */
public interface H2dbManager {

    /**
     * Manages patient or doctor registration.
     *
     * @param user user object that encapsulates the parameters required to save the user.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean registration(final User user);

    /**
     * Manages patient's or doctor's login.
     *
     * @param idCode patient or doctor identifier
     * @param password patient or doctor password
     * @param role user's role, rapresented in Enum UserRole.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean login(final String idCode, final String password, final UserRole role);

    /**
     * Gets patient or doctor information.
     *
     * @param role user's role, rapresented in Enum UserRole
     * @param idCode patient or doctor identifier
     *
     * @return JSONObject includes the information saved in the db application.
     */
    JSONObject getUserInformation(final String role, final UserRole idCode);

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
    JSONArray getValues(final String idPatient, final SensorType sensorType, final Optional<String> start, final Optional<String> end) throws JSONException, Exception;

    /**
     * Returns all advices related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of advices' set, represented by JSONObject
     */
    JSONArray getAdvices(String idPatient, Optional<String> start, Optional<String> end) throws Exception;

    /**
     * Adds an advice related to a particular patient.
     *
     * @param message a JSONObject containing advice, patient and doctor id
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addAdvice(final String message);

    /**
     * Returns all described drugs related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of drugs' set, represented by JSONObject
     */
    JSONArray getDrugs(final String idPatient, final Optional<String> start, final Optional<String> end) throws JSONException, Exception; //oggetto con classe drug da creare

    /**
     * Adds a drug related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param message  containing a drug's name and doctor's id
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addDrug(final String idPatient, final String message);


}
