package core.dbmanager.h2application;

import java.util.function.Consumer;

/** Manage the pattern Builder
 *
 * @author Giulia Lucchi
 */
public class UserBuilder {
    public String idCode;
    public String password;
    public String name;
    public String surname;
    public String cf;
    public String phones;
    public String mail;
    public String role;

    /** Creates a user builder.
     *
     * @param builderFunction s a functional interface provided by Java 8, which takes single argument and returns no result.
     *                        In this case it accepts an object of type user builder which is passed to accept method.
     * @return UserBuilder
     */
    public UserBuilder with(
            Consumer<UserBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    /** Creates an User object
     *
     * @return user user's object
     */
    public User createUser() {
        return new User(idCode,
                        password,
                        name,
                        surname,
                        cf,
                        phones,
                        mail,
                        role
        );
    }
}