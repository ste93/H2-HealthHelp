package core.dbmanager.h2dbManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Optional;

/**
 * Maps the RESTful API's functionality related to the management of drugs in Java interface.
 *
 * @author Giulia Lucchi
 */
public interface DrugManager {

    /**
     * Returns all described drugs related to a unique patient.
     *
     * @param idPatient patient's identifier
     * @param start an optional start date to search on the range of date
     * @param end an optional end date to search on the range of date
     *
     * @return JSONArray of drugs' set, represented by JSONObject
     */
    JSONArray getDrugs(final String idPatient, final Optional<String> start, final Optional<String> end) throws JSONException, Exception; //oggetto con classe drug da creare

    /**
     * Adds a drug related to a particular patient.
     *
     * @param idPatient patient's identifier
     * @param message  containing a drug's name and doctor's id
     *
     * @return boolean true if the request was successful
     *                 false otherwise
     */
    boolean addDrug(final String idPatient, final String message);
}
