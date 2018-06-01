package core;

/**
 * Includes the unique role of H2 application.
 *
 * @author Giulia Lucchi
 */
public enum UserRole {
    PATIENT("patient"),
    DOCTOR("doctor");

    private String role;

    UserRole(final String path){
        this.role = path;
    }

    public String getRole() {
        return role;
    }
}
