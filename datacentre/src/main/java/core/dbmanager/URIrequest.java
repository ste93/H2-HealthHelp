package core.dbmanager;

/**Includes basic path to do a request on dbs' RESTful API.
 *
 * @author Giulia Lucchi
 */
public enum URIrequest {
    PORT_ASSOCIATION("5225/"),
    PORT_H2_ROUTE("3000/"),
    BASIC_ROUTE("http://localhost:"),
    H2_ROUTE(BASIC_ROUTE.getPath()+PORT_H2_ROUTE.getPath()+"database/application"),
    ASSOCIATIONS_ROUTE(BASIC_ROUTE.getPath()+PORT_ASSOCIATION.getPath()+"database/associations");

    private String path;

    URIrequest(final String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
