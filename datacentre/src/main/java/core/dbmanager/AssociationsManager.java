package core.dbmanager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by margherita on 26/05/2018.
 */
public interface AssociationsManager {

    /**
     *
     * Returns the data related to the specific patient.
     *
     * @param id - patient's univocal ID
     *
     * @return a JSON object describing the patient
     *
     * @throws Exception
     */
    JSONObject getPatientData(String id) throws Exception;

    /**
     * Returns the data related to the specific doctor.
     *
     * @param id - doctor's univocal ID
     *
     * @return a JSON object describing the doctor
     *
     * @throws Exception
     */
    JSONObject getDoctorData(String id) throws Exception;

    /**
     * Creates a new patient with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id - patient's univocal ID
     * @param name - patient's name
     * @param surname - patient's surname
     * @param cf - patient's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    boolean createNewPatient(String id, String name, String surname, String cf);

    /**
     * Creates a new doctor with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id - doctor's univocal ID
     * @param name - doctor's name
     * @param surname - doctor's surname
     * @param cf - doctor's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    boolean createNewDoctor(String id, String name, String surname, String cf);

    /**
     * Deletes a specific patient. It returns true if the operation is successful, false otherwise.
     *
     * @param id - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean deletePatient(String id);

    /**
     * Deletes a specific patient. It returns true if the operation is successful, false otherwise.
     *
     * @param id - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    boolean deleteDoctor(String id);

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
    JSONObject getAssociation(String idPatient, String idDoctor);

    /**
     * Returns all the associations related to the specific patient.
     *
     * @param idPatient - patient's univocal ID
     *
     * @return all patient's associations.
     */
    JSONArray getPatientAssociations(String idPatient);

    /**
     * Returns all the associations related to the specific doctor.
     *
     * @param idDoctor - doctor's univocal ID
     *
     * @return all doctor's associations.
     */
    JSONArray getDoctorAssociations(String idDoctor);

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
