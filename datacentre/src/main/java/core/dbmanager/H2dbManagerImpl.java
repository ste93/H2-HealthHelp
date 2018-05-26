package core.dbmanager;

import core.SensorType;
import core.UserRole;
import org.glassfish.jersey.client.ClientResponse;

import javax.json.Json;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/** Implements H2dbManager interface
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerImpl implements H2dbManager {

    private final Client client = ClientBuilder.newClient();
    private final WebTarget basicWebTarget = client.target(URIrequest.H2_ROUTE.getPath());

    /**
     * Manages user registration.
     *
     * @param user user object that encapsulates the parameters required to save the user.
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean registration(final User user) {
        Response response = basicWebTarget.path(URIrequest.H2_REGISTRATION.getPath())
                                            .queryParam("idCode",user.idCode)
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
