package core.dbmanager;

import core.SensorType;
import core.UserRole;

import javax.json.Json;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/** Implements H2dbManager interface
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerImpl implements H2dbManager {

    private final Client client = ClientBuilder.newClient();

    /**
     * Manages user registration.
     *
     * @param user user object that encapsulates the parameters required to save the user.
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean registration(final User user) {
        return false;
    }

    @Override
    public boolean login(String idCode, String password, UserRole role) {
        return false;
    }

    @Override
    public ArrayList<String> getSensorsType(String idPatient) {
        return null;
    }

    @Override
    public boolean addNewSensorType(String idPatient, SensorType sensorType) {
        return false;
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
