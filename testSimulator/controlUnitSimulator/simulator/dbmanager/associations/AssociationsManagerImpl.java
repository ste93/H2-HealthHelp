package controlUnitSimulator.simulator.dbmanager.associations;

import core.dbmanager.URIrequest;
import org.json.JSONObject;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.*;

public class AssociationsManagerImpl implements AssociationsManager {

    private static Client CLIENT = ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.ASSOCIATIONS_ROUTE.getPath());
    private static WebTarget RELATIONSHIP_TARGET = TARGET.path("/relationship");

    /**
     * Creates a new association between a specific patient and a specific doctor. It returns true if the operation is successful, false otherwise.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor  - doctor's univocal ID
     * @return true if the operation is successful, false otherwise.
     */
    @Override
    public boolean createNewAssociation(String idPatient, String idDoctor) throws Exception {
        return getResponse(idPatient, idDoctor, "post");
    }

    /**
     * Returns the association between a specific patient and a specific doctor.
     *
     * @param idPatient - patient's univocal ID
     * @param idDoctor  - doctor's univocal ID
     * @return the association between patient and doctor.
     */
    @Override
    public JSONObject getAssociation(String idPatient, String idDoctor) throws Exception {
        JSONObject json;

        Invocation.Builder builder = RELATIONSHIP_TARGET
                .queryParam("idPatient", idPatient)
                .queryParam("idDoctor", idDoctor)
                .request();

        int response = builder.get().getStatus();

        if (response == 200) {
            String userData = builder.get(String.class);
            json = new JSONObject(userData);
        } else {
            throw new Exception("" + response);
        }

        return json;
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
    public boolean deleteAssociation(String idPatient, String idDoctor) throws Exception {
        return getResponse(idPatient, idDoctor, "delete");
    }

    /**
     * Mekes the specific request and returns the response.
     *
     * @param idPatient - patient's ID for the request
     * @param idDoctor - doctor's ID for the request
     * @param method - the specific request
     *
     * @return request's response
     *
     * @throws Exception - if the specific request is not managed
     */
    private boolean getResponse(final String idPatient, final String idDoctor, final String method) throws Exception {
        Builder builder = RELATIONSHIP_TARGET
                .queryParam("idPatient", idPatient)
                .queryParam("idDoctor", idDoctor)
                .request();

        int response;

        switch (method) {
            case "delete": response = builder.delete().getStatus();
                            break;
            case "post"  : response = builder.post(Entity.json("")).getStatus();
                            break;
            default      : throw new Exception("method " + method + " is not define");
        }

        return response == 200;

    }

}
