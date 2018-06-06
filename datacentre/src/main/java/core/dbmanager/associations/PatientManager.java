package core.dbmanager.associations;

import org.json.JSONArray;

public interface PatientManager extends UserManager{

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     *
     * @return all user's associations.
     */
    JSONArray getPatientAssociations(String userId) throws Exception;

}
