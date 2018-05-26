package core.dbmanager;

import org.json.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

public class AssociationsManagerImpl implements AssociationsManager {

    private static Client CLIENT =ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target("http://localhost:3000/database/associations");

    private static WebTarget PATIENT_TARGET = TARGET.path("/patients");
    private static WebTarget DOCTOR_TARGET = TARGET.path("/doctors");

    /**
     * Returns the data related to the specific patient.
     *
     * @param id - patient's univocal ID
     *
     * @return a JSON object describing the patient
     *
     * @throws Exception
     */
    @Override
    public JSONObject getPatientData(final String id) throws Exception {
        return getUserData(id, PATIENT_TARGET);
    }

    /**
     * Returns the data related to the specific doctor.
     *
     * @param id - doctor's univocal ID
     *
     * @return a JSON object describing the doctor
     *
     * @throws Exception
     */
    @Override
    public JSONObject getDoctorData(final String id) throws Exception {
        return getUserData(id, DOCTOR_TARGET);
    }

    /**
     * Private method to return the data related to the specific user.
     *
     * @param id - user's univocal ID
     * @param target  - user's role (patient or doctor)
     *
     * @return a JSON object describing the doctor
     *
     * @throws Exception
     */
    private JSONObject getUserData(final String id, final WebTarget target) throws Exception {
        JSONObject json = new JSONObject();

        Invocation.Builder builder = target.queryParam("_id", id).request(MediaType.APPLICATION_JSON);
        int response = builder.get().getStatus();

        if (response == 200) {
            String userData = builder.get(String.class);
            json = new JSONObject(userData);
        } else {
            json.put("error ", response);
            throw new Exception("" + response);
        }

        return json;
    }

    /**
     * Creates a new patient with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id      - patient's univocal ID
     * @param name    - patient's name
     * @param surname - patient's surname
     * @param cf      - patient's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    @Override
    public boolean createNewPatient(final String id, final String name, final String surname, final String cf) {
        return createUser(id, name, surname, cf, PATIENT_TARGET);
    }

    /**
     * Creates a new doctor with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id      - doctor's univocal ID
     * @param name    - doctor's name
     * @param surname - doctor's surname
     * @param cf      - doctor's CF
     *
     * @return true if the operation is successful, false otherwise
     */
    @Override
    public boolean createNewDoctor(final String id, final String name, final String surname, final String cf) {
        return createUser(id, name, surname, cf, DOCTOR_TARGET);
    }

    /**
     * Private method to create a new user with all related information. It returns true if the operation is successful, false otherwise.
     *
     * @param id      - user's univocal ID
     * @param name    - user's name
     * @param surname - user's surname
     * @param cf      - user's CF
     * @param target  - user's role (patient or doctor)
     *
     * @return true if the operation is successful, false otherwise
     */
    private boolean createUser(final String id, final String name, final String surname, final String cf, final WebTarget target) {
        Invocation.Builder builder = target
                .queryParam("_id", id)
                .queryParam("name", name)
                .queryParam("surname", surname)
                .queryParam("cf", cf)
                .request(MediaType.APPLICATION_JSON);

        int response = builder.post(Entity.json("")).getStatus();
        return response == 200;
    }


    /**
     * Deletes a specific patient. It returns true if the operation is successful, false otherwise.
     *
     * @param id - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean deletePatient(final String id) {
        return false;
    }

    /**
     * Deletes a specific patient. It returns true if the operation is successful, false otherwise.
     *
     * @param id - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean deleteDoctor(final String id) {
        return false;
    }

    /**
     * Creates a new association between a specific patient and a specific doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor  - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean createNewAssociation(final String idPatient, final String idDoctor) {
        return false;
    }

    /**
     * Returns the association between a specific patient and a specific doctor.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor  - doctor's univocal ID
     *
     * @return the association between patient and doctor.
     */
    @Override
    public JSONObject getAssociation(final String idPatient, final String idDoctor) {
        return null;
    }

    /**
     * Returns all the associations related to the specific patient.
     *
     * @param idPatient - patient's univocal ID
     *
     * @return all patient's associations.
     */
    @Override
    public JSONArray getPatientAssociations(final String idPatient) {
        return null;
    }

    /**
     * Returns all the associations related to the specific doctor.
     *
     * @param idDoctor - doctor's univocal ID
     *
     * @return all doctor's associations.
     */
    @Override
    public JSONArray getDoctorAssociations(final String idDoctor) {
        return null;
    }

    /**
     * Deletes the specific association between patient and doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor  - doctor's univocal ID
     *
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean deleteAssociation(final String idPatient, final String idDoctor) {
        return false;
    }
}
