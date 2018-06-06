package core.dbmanager.associations;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

public class UserManagerImpl implements UserManager {

    private final WebTarget target;

    /**
     * Creates an instance of AssociationsManagerImpl with a specific web target related to the user.
     *
     * @param target - user's web target
     */
    public UserManagerImpl(final WebTarget target) {
        this.target = target;
    }

    /**
     * Creates a new user with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id      - user's univocal ID
     * @param name    - user's name
     * @param surname - user's surname
     * @param cf      - user's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    @Override
    public boolean createNewUser(final String id, final String name, final String surname, final String cf) {
        int response = target
                .queryParam("_id", id)
                .queryParam("name", name)
                .queryParam("surname", surname)
                .queryParam("cf", cf)
                .request()
                .post(Entity.json(""))
                .getStatus();

        return response == 200;
    }

    /**
     * Returns the data related to the specific user.
     *
     * @param id - user's univocal ID
     *
     * @return a JSON object describing the user
     *
     * @throws Exception
     */
    @Override
    public JSONObject getUserData(final String id) throws Exception {
        JSONObject json;
        int response = 0;

        try {
            response = makeRequestAndGetResponse("_id", id, "get");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response == 200) {
            String userData = getBuilder("_id", id).get(String.class);
            json = new JSONObject(userData);
        } else {
            throw new Exception("" + response);
        }

        return json;
    }

    /**
     * Deletes a specific user. It returns true if the operation is successful, false otherwise.
     *
     * @param id - user's univocal ID
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean deleteUser(final String id) {
        int response = 0;
        try {
            response = makeRequestAndGetResponse("_id", id, "delete");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response == 200;
    }

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     * @return all user's associations.
     */
    @Override
    public JSONArray getUserAssociations(final String userId, final String idRole, final WebTarget associationsTarget) throws Exception {
        JSONArray json;

        Invocation.Builder builder = associationsTarget
                .queryParam(idRole, userId)
                .request();

        int response = builder.get().getStatus();

        if (response == 200) {
            String userData = builder.get(String.class);
            json = new JSONArray(userData);
        } else {
            throw new Exception("" + response);
        }

        return json;
    }

    /**
     * Returns the Builder of the specific user's ID.
     *
     * @param key - key of the query
     * @param value - value of the query
     *
     * @return the specific Builder
     */
    private Invocation.Builder getBuilder(final String key, final String value) {
        return target.queryParam(key, value).request();
    }

    /**
     * Makes the request with specific ID and method and returns the response of the request itself.
     *
     * @param key - key of the query
     * @param value - value of the query
     * @param method - the method to perform
     *
     * @return the response of the request
     *
     * @throws Exception when the method is not define
     */
    private int makeRequestAndGetResponse(final String key, final String value, final String method) throws Exception {
        Invocation.Builder builder = getBuilder(key, value);
        int response = -1;

        switch (method) {
            case "get":    response = builder.get().getStatus();
                break;
            case "delete": response = builder.delete().getStatus();
                break;
            default:       throw new Exception("method " + method + " is not define");
        }

        return response;
    }

}
