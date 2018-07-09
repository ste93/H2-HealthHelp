package core.dbmanager.h2dbManager;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.Optional;

/**
 * Includes the general and shared operation to do a RESTful API request;
 *
 * @author Giulia Lucchi
 */
public class H2dbManagerUtils {

    /**
     * Manages the delete od drugs on the range.
     *
     * @param idPatient patient's identifier
     * @param start      an optional start date to search on the range of date
     * @param end        an optional end date to search on the range of date
     *
     * @return Invocation.Builder request to a RESTful API
     */
    public Invocation.Builder drugsOrAdviceOnRange(WebTarget webTarget, String idPatient, Optional<String> start, Optional<String> end){
        Invocation.Builder request;

        if(start.isPresent() && end.isPresent()){
            request = webTarget.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .queryParam("end", end.get())
                    .request();

        }else if(start.isPresent() && !(end.isPresent())){
            request = webTarget.queryParam("idCode", idPatient)
                    .queryParam("start", start.get())
                    .request();
        }else{
            request = webTarget.queryParam("idCode", idPatient)
                    .request();
        }
        return request;
    }

    /**
     * Manages the JSONArray's response.
     *
     * @param responseCode response code of API's request
     * @param request   API's request built
     *
     * @return  JSONArray   response of request done.     *
     */
    public JSONArray responseToJSONArray(int responseCode, Invocation.Builder request) throws Exception {
        JSONArray json;
        if (responseCode== 200) {
            json = new JSONArray(request.get(String.class));
        } else {
            json = new JSONArray();
            json.put(responseCode);
        }
        return  json;
    }

    /**
     * Manages the JSONObject's response.
     *
     * @param responseCode response code of API's request
     * @param request   API's request built
     *
     * @return  JSONObject   response of request done.     *
     */
    public JSONObject responseToJSONObject(int responseCode, Invocation.Builder request) throws Exception {
        JSONObject json;
        if (responseCode== 200) {
            json = new JSONObject(request.get(String.class));
        } else {
            json = new JSONObject();
            json.put("code", responseCode);
        }
        return  json;

    }

}
