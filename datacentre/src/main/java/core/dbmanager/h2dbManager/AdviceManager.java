package core.dbmanager.h2dbManager;

import org.json.JSONArray;

import java.util.Optional;

/**
 * Maps the RESTful API's functionality related to the management of advices in Java interface.
 *
 * @author Giulia Lucchi
 */
public interface AdviceManager {
    /**
     * Returns all advices related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of advices' set, represented by JSONObject
     */
    JSONArray getAdvices(String idPatient, Optional<String> start, Optional<String> end) throws Exception;

    /**
     * Adds an advice related to a particular patient.
     *
     * @param message a JSONObject containing advice, patient and doctor id
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addAdvice(final String message);

}
