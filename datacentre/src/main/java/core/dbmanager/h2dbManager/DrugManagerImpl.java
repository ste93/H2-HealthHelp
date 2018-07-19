package core.dbmanager.h2dbManager;

import core.dbmanager.URIrequest;
import org.json.JSONArray;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Implementation of DrugManager.
 *
 * @author Giulia Lucchi
 */
public class DrugManagerImpl implements DrugManager {

    private static Client CLIENT = ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.H2_ROUTE.getPath());

    private static WebTarget H2_DRUGS = TARGET.path("/drugs");

    private H2dbManagerUtils utils = new H2dbManagerUtils();

    /**
     * Returns all described drugs related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of drugs' set, represented by JSONObject
     */
    @Override
    public JSONArray getDrugs(final String idPatient, final Optional<String> start, final Optional<String> end) throws Exception {
        Invocation.Builder request = utils.drugsOrAdviceOnRange(H2_DRUGS, idPatient, start, end);

        int responseCode = request.get().getStatus();
        return utils.responseToJSONArray(responseCode, request);
    }

    /**
     * Adds a drug related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param message   containing a drug's name and doctor's id (format: "KEY":"VALUE" without initial and final brackets)
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addDrug(String idPatient, String message) {
        Response response = H2_DRUGS
                .queryParam("idCode", idPatient)
                .queryParam("message", message)
                .request()
                .post(null);

        return response.getStatus()==200;
    }

}
