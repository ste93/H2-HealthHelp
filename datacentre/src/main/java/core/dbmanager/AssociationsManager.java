package core.dbmanager;

import javax.json.Json;
import javax.json.JsonArray;

/**
 * Created by lucch on 26/05/2018.
 */
public interface AssociationsManager {

    Json getPatientData(final String id);

    Json getDoctorData(final String id);

    boolean createNewPatient(final String id,final String name, final String surname, final String cf );

    boolean createNewDoctor(final String id,final String name, final String surname, final String cf );

    boolean deletePatient(final String id);

    boolean deleteDoctor(final String id);


    boolean createNewAssociation(final String idPatient, final String idDoctor);

    Json getAssociation(final String idPatient, final String idDoctor);

    JsonArray getPatientAssociations(final String idPatient);

    JsonArray getDoctorAssociations(final String idDoctor);

    boolean deleteAssociation(final String idPatient, final String idDoctor);
}
