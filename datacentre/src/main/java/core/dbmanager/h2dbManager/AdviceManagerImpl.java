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
 * Implementation of AdviceManager interface.
 *
 * @author Giulia Lucchi
 */
public class AdviceManagerImpl implements AdviceManager {


    private static Client CLIENT = ClientBuilder.newClient();
    private static WebTarget TARGET = CLIENT.target(URIrequest.H2_ROUTE.getPath());

    private static WebTarget H2_ADVICE = TARGET.path("/advices");

    private H2dbManagerUtils utils = new H2dbManagerUtils();
    /**
     * Returns all advices related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of advices' set, represented by JSONObject
     */
    @Override
    public JSONArray getAdvices(String idPatient, Optional<String> start, Optional<String> end) throws Exception {
        Invocation.Builder request = utils.drugsOrAdviceOnRange(H2_ADVICE, idPatient, start, end);

        int responseCode = request.get().getStatus();
        return utils.responseToJSONArray(responseCode, request);
    }

    /**
     * Adds an advice related to a particular patient.
     *
     * @param message containing advice, patient and doctor id (format: "KEY":"VALUE" without initial and final brackets)
     *
     * @return boolean true if the request was successful
     * false otherwise
     */
    @Override
    public boolean addAdvice(String message) {

        Response response = H2_ADVICE
                .queryParam("message", message)
                .request()
                .post(null);

        return response.getStatus()==200;
    }
}
