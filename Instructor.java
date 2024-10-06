package application;

import java.util.HashMap;
import java.util.Map;

public class Instructor extends User {

    private Map<String, User> userDatabase = new HashMap<>();

    public Instructor(String username, char[] password, Map<String, User> userDatabase) {
        super(username, password);
        this.userDatabase = userDatabase;
    }
}