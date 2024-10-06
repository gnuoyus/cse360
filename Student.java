package application;

import java.util.HashMap;
import java.util.Map;

public class Student extends User {

    private Map<String, User> userDatabase = new HashMap<>();

    public Student(String username, char[] password, Map<String, User> userDatabase) {
        super(username, password);
        this.userDatabase = userDatabase;
    }
}