package application;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Student Class</p>
 *
 * <p>Description: The Student class represents a user with student privileges.
 * It extends the base User class and includes attributes and methods specific to students, such as managing course enrollment or tracking academic progress.</p>
 *
 * <p>Author: Group Tu64</p>
 *
 * <p>Version: 1.00 2024-10-02</p>
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
