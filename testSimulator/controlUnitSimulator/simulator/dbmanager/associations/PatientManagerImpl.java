package controlUnitSimulator.simulator.dbmanager.associations;

import core.dbmanager.URIrequest;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class PatientManagerImpl extends UserManagerImpl implements PatientManager {

    private static Client CLIENT =ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.ASSOCIATIONS_ROUTE.getPath());

    private static WebTarget PATIENT_TARGET = TARGET.path("/patients");
    private static WebTarget RELATIONSHIP_TARGET = TARGET.path("/relationship");

    /**
     * Creates an instance of AssociationsManagerImpl with a specific web target related to the user.
     */
    public PatientManagerImpl() {
        super(PATIENT_TARGET);
    }

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     * @return all user's associations.
     */
    @Override
    public JSONArray getPatientAssociations(String userId) throws Exception {
        return super.getUserAssociations(userId, "idPatient", RELATIONSHIP_TARGET);
    }

}

