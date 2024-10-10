package application;

import java.util.HashMap;
import java.util.Map;

/**
 * The Student class represents a user with student privileges.
 * It extends the base User class and contains a reference to the user database.
 */
public class Student extends User {

    private Map<String, User> userDatabase = new HashMap<>(); // Database to store user information

    /**
     * Constructor for the Student class.
     *
     * @param username The username for the student
     * @param password The password for the student
     * @param userDatabase The database of users
     */
    public Student(String username, char[] password, Map<String, User> userDatabase) {
        super(username, password); // Call the parent constructor to initialize username and password
        this.userDatabase = userDatabase; // Initialize the user database
    }

}
