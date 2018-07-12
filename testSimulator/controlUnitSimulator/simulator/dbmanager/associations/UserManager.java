package controlUnitSimulator.simulator.dbmanager.associations;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.WebTarget;

public interface UserManager {

    /**
     * Creates a new user with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id - user's univocal ID
     * @param name - user's name
     * @param surname - user's surname
     * @param cf - user's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    boolean createNewUser(String id, String name, String surname, String cf);

    /**
     *
     * Returns the data related to the specific user.
     *
     * @param id - user's univocal ID
     *
     * @return a JSON object describing the user
     *
     * @throws Exception
     */
    JSONObject getUserData(String id) throws Exception;

    /**
     * Deletes a specific user. It returns true if the operation is successful, false otherwise.
     *
     * @param id - user's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteUser(String id);

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     *
     * @return all user's associations.
     */
    JSONArray getUserAssociations(String userId, String role, WebTarget target) throws Exception;

}
