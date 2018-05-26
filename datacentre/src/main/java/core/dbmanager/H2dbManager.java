package core.dbmanager;

import core.SensorType;
import core.UserRole;

import javax.json.Json;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/** Manages the client access to RESTful API to access the H2 database.
 *
 * @author Giulia Lucchi
 */
public interface H2dbManager {

    /** Manages user registration.
     *
     * @param user user object that encapsulates the parameters required to save the user.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean registration(final User user);

    boolean login(final String idCode, final String password, final UserRole role);


    ArrayList<String> getSensorsType(final String idPatient);

    boolean addNewSensorType(final String idPatient, final SensorType sensorType);

    boolean addSensorValue(final String idPatient, final SensorType sensorType, final Json message);

    boolean deleteValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);

    boolean getValues(final String idPatient, final SensorType sensorType, final Optional<Date> start, final Optional<Date> end);


    ArrayList<Json> getAdvices(final String idPatient); // oggetto con classe advice da creare
    boolean addAdvice(final Json message);


    ArrayList<Json> getDrugs(final String idPatient); //oggetto con classe drug da creare
    boolean addDrug(final String idPatient, final Json message);


}
