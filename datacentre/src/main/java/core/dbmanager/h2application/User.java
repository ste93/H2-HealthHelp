package core.dbmanager.h2application;

import core.UserRole;
import java.util.ArrayList;

/** Represents the user of H2 application.
 *
 * @author Giulia Lucchi
 */
public class User {
    private String idCode;
    private String password;
    private String name;
    private String surname;
    private String cf;
    private String phones;
    private String mail;
    private String role;

    /** User's constructor
     *
     * @param idCode patient or doctor identifier
     * @param password patient or doctor password
     * @param name patient or doctor name
     * @param surname patient or doctor surnamle
     * @param cf patient of doctor cf
     * @param phones patient or doctor array of phone numbers
     * @param mail patient or doctor mail
     * @param role user's role, rapresented in Enum UserRole.
     */
    public User(final String idCode, final String password, final String name, final String surname, final String cf, final String phones, final String mail, final String role){
        this.idCode = idCode;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.phones = phones;
        this.mail = mail;
        this.role = role;
    }

    public String getIdCode() {
        return idCode;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCf() {
        return cf;
    }

    public String getPhones() {
        return phones;
    }

    public String getMail() {
        return mail;
    }

    public String getRole() {
        return role;
    }
}
