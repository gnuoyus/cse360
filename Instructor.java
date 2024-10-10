package application;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Instructor Class</p>
 *
 * <p>Description: The Instructor class represents a user with instructor privileges.
 * It extends the base User class and includes attributes and methods specific to instructors.</p>
 *
 * <p>Author: Group Tu64</p>
 *
 * <p>Version: 1.00 2024-10-02</p>
 */
public class Instructor extends User {

    // A map representing the user database where the key is the username and the value is a User object.
    private Map<String, User> userDatabase = new HashMap<>();

    /**
     * Constructor for the Instructor class.
     * It initializes the username, password, and a reference to the user database.
     *
     * @param username     The username of the instructor.
     * @param password     The password of the instructor.
     * @param userDatabase The map containing all user data.
     */
    public Instructor(String username, char[] password, Map<String, User> userDatabase) {
        super(username, password); // Calling the constructor of the superclass (User)
        this.userDatabase = userDatabase; // Assigning the user database to the instructor's local database
    }
}
