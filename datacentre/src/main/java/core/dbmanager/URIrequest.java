package core.dbmanager;

/**Includes all path to do a request on dbs' RESTful API.
 *
 * @author Giulia Lucchi
 */
public enum URIrequest {
    PROTOCOL("http://"),

    H2_ROUTE("database/application/"),

    H2_REGISTRATION("registration");


    private String path;

    URIrequest(final String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
