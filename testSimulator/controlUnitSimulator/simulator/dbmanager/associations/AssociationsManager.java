package controlUnitSimulator.simulator.dbmanager.associations;

import org.json.JSONObject;

public interface AssociationsManager {

    /**
     * Creates a new association between a specific patient and a specific doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean createNewAssociation(String idPatient, String idDoctor) throws Exception;

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
     * Deletes the specific association between patient and doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteAssociation(String idPatient, String idDoctor) throws Exception;

}
