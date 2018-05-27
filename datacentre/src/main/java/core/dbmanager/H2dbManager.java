package core.dbmanager;

import core.SensorType;
import core.UserRole;
import org.json.JSONArray;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Date;
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
     * Add a sensor type related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param sensorType sensor type added
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addNewSensorType(final String idPatient, final SensorType sensorType);

    boolean addSensorValue(final String idPatient, final SensorType sensorType, final Json message);

    boolean deleteValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);

    boolean getValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);


    ArrayList<Json> getAdvices(final String idPatient); // oggetto con classe advice da creare
    boolean addAdvice(final Json message);


    ArrayList<Json> getDrugs(final String idPatient); //oggetto con classe drug da creare
    boolean addDrug(final String idPatient, final Json message);


}
