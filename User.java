package application;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

class User {
	
    private String userName;
    private char[] password;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredFirstName;
    private Set<String> roles; 
    public LocalDateTime deadLine; // Password reset deadline
    private boolean accountSetUp;
    private boolean reset;
    private String OTC;
    private boolean isAdmin;
    private boolean isStudent;
    private boolean isInstructor;
    private String[] levels = {"beginner", "intermediate", "advanced", "expert"};
    private String level;

    // Constructor
    public User(String userName, char[] password) {
        this.userName = userName;
        this.password = Arrays.copyOf(password, password.length); // Copy password to avoid exposing the original array
        roles = new HashSet<>(); // Initialize roles as a HashSet to prevent duplicate roles
        accountSetUp = false;
        reset = false;
        level = levels[1];
    }

    // Getters and Setters for each field
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = Arrays.copyOf(password, password.length); 
        System.out.println(this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPreferredFirstName() {
        return preferredFirstName;
    }

    public void setPreferredFirstName(String preferredFirstName) {
        this.preferredFirstName = preferredFirstName;
    }
    
    public void addRole(String role) {
        roles.add(role); 
    }

    public void removeRole(String role) {
        roles.remove(role); 
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public Set<String> getRoles() {
        return roles;
    }

    // Set multiple roles (in case of setting roles directly)
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public boolean getAccountSetUp() {
        return accountSetUp;
    }  
    
    public void finishAccountSetup() {
        accountSetUp = true;
    }

    public boolean getReset() {
    	return reset;
    }
    
    public void doReset() {
        reset = true;
    }
    
    public void undoReset() {
        reset = false;
    }    

    public void DeadlineGone() {
        deadLine = null;
    }
    
    public String getOTC() {
    	return OTC;
    }
    
    public void setOTC() {
    	// one time code will be sent to email
    	// made it 123 for explaining how to use
    	OTC = "123";
    }
    
    public boolean getIsAdmin() {
    	return isAdmin;
    }
    
    public void setIsAdmin() {
    	isAdmin = true;
    }
    
    public boolean getIsStudent() {
    	return isStudent;
    }
    
    public void setIsStudent() {
    	isStudent = true;
    }
    
    public boolean getIsInstructor() {
    	return isInstructor;
    }
    
    public void setIsInstructor() {
    	isInstructor = true;
    }
    
    public void setLevel(int levelIndex)
    {
        level = levels[levelIndex];
    }
    public String getLevel() {
        return level;
    }
        
}
