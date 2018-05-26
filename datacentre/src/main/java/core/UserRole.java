package core;

/**
 * Created by lucch on 26/05/2018.
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
