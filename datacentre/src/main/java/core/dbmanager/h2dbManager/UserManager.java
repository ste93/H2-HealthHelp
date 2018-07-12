package core.dbmanager.h2dbManager;

import core.UserRole;
import org.json.JSONObject;

/**
 * Maps the RESTful API's functionality related to the management of user operations in Java interface.
 *
 * @author Giulia Lucchi
 */
public interface UserManager {
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
    JSONObject getUserInformation(final UserRole role, final String idCode) throws Exception;

    /**
     * Deletes user from the H2 Application database
     *
     * @param idCode patient or doctor identifier
     * @param role user's role, rapresented in Enum UserRole.
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean deleteUser(final String idCode, final UserRole role);
}
