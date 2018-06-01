package core.dbmanager;

import org.json.JSONArray;
import org.json.JSONObject;

public interface AssociationsManager {

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
     * Creates a new association between a specific patient and a specific doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean createNewAssociation(String idPatient, String idDoctor);

    /**
     * Returns the association between a specific patient and a specific doctor.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor - doctor's univocal ID
     *
     * @return the association between patient and doctor.
     */
    JSONObject getAssociation(String idPatient, String idDoctor) throws Exception;

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     *
     * @return all user's associations.
     */
    JSONArray getUserAssociations(String userId) throws Exception;

    /**
     * Deletes the specific association between patient and doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteAssociation(String idPatient, String idDoctor);

}
