package core.pubsub.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucch on 11/06/2018.
 */
public class UserMessage {
    private String idCode;
    private String name;
    private String surname;
    private String cf;
    private String phones;
    private String mail;

    public UserMessage(String idCode, String name, String surname, String cf, String phones, String mail) {
        this.idCode = idCode;
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.phones = phones;
        this.mail = mail;
    }

    public String getIdCode() {
        return idCode;
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

    public String toJson() throws JSONException {
        return new JSONObject().put("idCode", idCode)
                                .put("name", name)
                                .put("surname", surname)
                                .put("cf", cf)
                                .put("phones",phones)
                                .put("mail", mail)
                                .toString();
    }

}
