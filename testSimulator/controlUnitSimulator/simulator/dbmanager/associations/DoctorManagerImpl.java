package controlUnitSimulator.simulator.dbmanager.associations;

import core.dbmanager.URIrequest;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class DoctorManagerImpl extends UserManagerImpl implements DoctorManager {

    private static Client CLIENT =ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.ASSOCIATIONS_ROUTE.getPath());

    private static WebTarget DOCTOR_TARGET = TARGET.path("/doctors");
    private static WebTarget RELATIONSHIP_TARGET = TARGET.path("/relationship");

    /**
     * Creates an instance of AssociationsManagerImpl with a specific web target related to the user.
     */
    public DoctorManagerImpl() {
        super(DOCTOR_TARGET );
    }

    /**
     * Returns all the associations related to the specific user.
     *
     * @param userId - user's univocal ID
     * @return all user's associations.
     */
    @Override
    public JSONArray getDoctorAssociations(String userId) throws Exception {
        return super.getUserAssociations(userId, "idDoctor", RELATIONSHIP_TARGET);
    }
}
