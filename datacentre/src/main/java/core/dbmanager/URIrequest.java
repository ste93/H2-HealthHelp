package core.dbmanager;

/**Includes basic path to do a request on dbs' RESTful API.
 *
 * @author Giulia Lucchi
 */
public enum URIrequest {
    BASIC_ROUTE("http://localhost:3000/"),

    H2_ROUTE(BASIC_ROUTE.getPath()+"database/application"),
    ASSOCIATIONS_ROUTE(BASIC_ROUTE.getPath()+"database/associations");

    private String path;

    URIrequest(final String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
