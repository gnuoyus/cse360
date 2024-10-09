package application;

import java.time.LocalDateTime;
import java.util.*;

public class Admin extends User {

    private Map<String, User> userDatabase = new HashMap<>();
    
    public Admin(String username, char[] password, Map<String, User> userDatabase) {
        super(username, password);  
        this.userDatabase = userDatabase;
    }

    public void deleteUser(String userName, Map<String, User> userDatabase) {
        if (userDatabase.remove(userName) != null) {
            System.out.println("User " + userName + " has been deleted.");
        } else {
            System.out.println("User " + userName + " not found.");
        }
    }

    public String addRoleToUser(User user, String role) {
    	String messageLabel = "";
        if (user != null) {
        	if (role.equals("Admin") || role.equals("Student") || role.equals("Instructor")) {
            user.addRole(role);
        	messageLabel = "Role " + role + " added to " + user.getUserName();
        	} else {
            	messageLabel = "Invalid role.";
            }
        } else {
        	messageLabel = "User not found.";
        }
        return messageLabel;
    }

    public void removeRoleFromUser(User user, String role) {
        if (user != null) {
            user.removeRole(role);
            System.out.println("Removed role " + role);
        } else {
            System.out.println("User not found.");
        }
    }

    static char[] generate_OTP() 
    { 
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
        String numbers = "0123456789"; 
                String symbols = "!@#$%^&*=+-/.?<>)"; 

        String values = Capital_chars + Small_chars + numbers + symbols; 

        Random rand_method = new Random(); 

        int length = (int) ((Math.random() * (-4)) + 8);
        char[] oneTimePassword = new char[length]; 

        for (int i = 0; i < length; i++) { 
            oneTimePassword[i] = values.charAt(rand_method.nextInt(values.length())); 
        } 
        return oneTimePassword; 
    } 

    public void resetUserPassword(String userName, Map<String, User> userDatabase) {
        User user = userDatabase.get(userName);
        user.doReset();
        if (user != null) {
            char[] oneTimePassword = generate_OTP();
            user.setPassword("temporary".toCharArray());
            user.deadLine =  LocalDateTime.now().plusDays(1);
            System.out.print(user.deadLine);
            System.out.println("Password reset for " + userName + " with one-time password." + oneTimePassword.toString());
        }
    }

    public void listUsers() {
        for (User user : userDatabase.values()) {
            System.out.println("Username: " + user.getUserName() + ", Roles: " + user.getRoles());
        }
    }

    public void backupUserData(Map<String, User> userDatabaseBackup) {
        userDatabaseBackup.clear();
        for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
            User backupUser = new User(entry.getValue().getUserName(), entry.getValue().getPassword());
            backupUser.setEmail(entry.getValue().getEmail());
            backupUser.setFirstName(entry.getValue().getFirstName());
            backupUser.setLastName(entry.getValue().getLastName());
            backupUser.setRoles(entry.getValue().getRoles());
            userDatabaseBackup.put(entry.getKey(), backupUser);
        }
        System.out.println("User data has been backed up.");
    }

    public void restoreUserData(Map<String, User> userDatabaseBackup) {
        userDatabase.clear();
        userDatabase.putAll(userDatabaseBackup);
        System.out.println("User data has been restored from backup.");
    }
}
