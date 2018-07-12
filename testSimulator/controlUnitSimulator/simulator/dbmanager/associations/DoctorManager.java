package controlUnitSimulator.simulator.dbmanager.associations;

import org.json.JSONArray;

public interface DoctorManager extends UserManager {

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     *
     * @return all user's associations.
     */
    JSONArray getDoctorAssociations(String userId) throws Exception;

}
