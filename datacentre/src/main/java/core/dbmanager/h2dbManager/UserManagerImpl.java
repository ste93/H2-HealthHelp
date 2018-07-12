package core.dbmanager.h2dbManager;

import core.UserRole;
import core.dbmanager.URIrequest;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Implemetation of UserManager.
 *
 * @author Giulia Lucchi
 */
public class UserManagerImpl implements UserManager {
    private static Client CLIENT = ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.H2_ROUTE.getPath());

    private static WebTarget H2_REGISTRATION = TARGET.path("/registration");
    private static WebTarget H2_LOGIN = TARGET.path("/login");
    private static WebTarget H2_DOCTORS = TARGET.path("/doctors");
    private static WebTarget H2_PATIENTS = TARGET.path("/patients");

    private H2dbManagerUtils utils = new H2dbManagerUtils();

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
     * Gets patient or doctor information.
     *
     * @param role   user's role, rapresented in Enum UserRole
     * @param idCode patient or doctor identifier
     * @return JSONObject includes the information saved in the db application.
     */
    @Override
    public JSONObject getUserInformation(UserRole role, String idCode) throws Exception {

        JSONObject json;
        Invocation.Builder request;

        if(role.equals(UserRole.DOCTOR)) {
            request = H2_DOCTORS.queryParam("idCode", idCode)
                    .request();
        }else{
            request = H2_PATIENTS.queryParam("idCode", idCode)
                    .request();
        }
        int responseCode = request.get().getStatus();

        return  utils.responseToJSONObject(responseCode, request);
    }

    /**
     * Deletes user from the H2 Application database
     *
     * @param idCode patient or doctor identifier
     * @param role user's role, rapresented in Enum UserRole.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    @Override
    public boolean deleteUser(final String idCode, final UserRole role){
        int response;

        if(role.equals(UserRole.DOCTOR)) {
            response = H2_DOCTORS.queryParam("idCode", idCode)
                    .request()
                    .delete()
                    .getStatus();

        }else{
            response = H2_PATIENTS.queryParam("idCode", idCode)
                    .request()
                    .delete()
                    .getStatus();
        }

        return response==200;
    }

}
