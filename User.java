package application;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

class User {

    private String userName; // Username of the user
    private char[] password; // Password of the user
    private String email; // Email address of the user
    private String firstName; // First name of the user
    private String middleName; // Middle name of the user
    private String lastName; // Last name of the user
    private String preferredFirstName; // Preferred first name of the user
    private Set<String> roles; // Roles assigned to the user
    public LocalDateTime deadLine; // Password reset deadline
    private boolean accountSetUp; // Indicates if the account is set up
    private boolean reset; // Indicates if the password reset has been initiated
    private String OTC; // One-time code for password reset
    private boolean isAdmin; // Indicates if the user is an admin
    private boolean isStudent; // Indicates if the user is a student
    private boolean isInstructor; // Indicates if the user is an instructor
    private String[] levels = {"beginner", "intermediate", "advanced", "expert"}; // Skill levels
    private String level; // Current skill level of the user

    /**
     * Constructor for the User class.
     *
     * @param userName The username for the user
     * @param password The password for the user
     */
    public User(String userName, char[] password) {
        this.userName = userName;
        this.password = Arrays.copyOf(password, password.length); // Copy password to avoid exposing the original array
        roles = new HashSet<>(); // Initialize roles as a HashSet to prevent duplicate roles
        accountSetUp = false;
        reset = false;
        level = levels[1]; // Default level set to "intermediate"
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
        this.password = Arrays.copyOf(password, password.length); // Copy new password to avoid exposing the original array
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

    /**
     * Adds a role to the user.
     *
     * @param role The role to add
     */
    public void addRole(String role) {
        roles.add(role); 
    }

    /**
     * Removes a role from the user.
     *
     * @param role The role to remove
     */
    public void removeRole(String role) {
        roles.remove(role); 
    }

    /**
     * Checks if the user has a specific role.
     *
     * @param role The role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    /**
     * Gets the set of roles assigned to the user.
     *
     * @return A Set of roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Sets multiple roles for the user.
     *
     * @param roles The set of roles to assign
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * Checks if the user's account is set up.
     *
     * @return true if the account is set up, false otherwise
     */
    public boolean getAccountSetUp() {
        return accountSetUp;
    }

    /**
     * Marks the account as set up.
     */
    public void finishAccountSetup() {
        accountSetUp = true;
    }

    /**
     * Checks if the password reset has been initiated.
     *
     * @return true if reset is initiated, false otherwise
     */
    public boolean getReset() {
        return reset;
    }

    /**
     * Initiates the password reset process.
     */
    public void doReset() {
        reset = true;
    }

    /**
     * Undoes the password reset process.
     */
    public void undoReset() {
        reset = false;
    }

    /**
     * Clears the password reset deadline.
     */
    public void DeadlineGone() {
        deadLine = null;
    }

    /**
     * Gets the one-time code for password reset.
     *
     * @return The one-time code
     */
    public String getOTC() {
        return OTC;
    }

    /**
     * Sets the one-time code for password reset.
     */
    public void setOTC() {
        // one-time code will be sent to email
        // made it 123 for explaining how to use
        OTC = "123"; // Placeholder value
    }

    /**
     * Checks if the user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Marks the user as an admin.
     */
    public void setIsAdmin() {
        isAdmin = true;
    }

    /**
     * Checks if the user is a student.
     *
     * @return true if the user is a student, false otherwise
     */
    public boolean getIsStudent() {
        return isStudent;
    }

    /**
     * Marks the user as a student.
     */
    public void setIsStudent() {
        isStudent = true;
    }

    /**
     * Checks if the user is an instructor.
     *
     * @return true if the user is an instructor, false otherwise
     */
    public boolean getIsInstructor() {
        return isInstructor;
    }

    /**
     * Marks the user as an instructor.
     */
    public void setIsInstructor() {
        isInstructor = true;
    }

    /**
     * Sets the user's skill level based on the index.
     *
     * @param levelIndex The index of the skill level to set
     */
    public void setLevel(int levelIndex) {
        level = levels[levelIndex];
    }

    /**
     * Gets the user's current skill level.
     *
     * @return The current skill level
     */
    public String getLevel() {
        return level;
    }
}
