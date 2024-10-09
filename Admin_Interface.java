package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * The Admin_Interface class provides a graphical user interface (GUI) for admins to manage users.
 * This interface includes functionality to invite new users, reset passwords, delete users, 
 * modify roles, and view a list of users.
 */
public class Admin_Interface extends Application {

    // A map that stores the user database where the key is the username and the value is a User object.
    private Map<String, User> userDatabase = new HashMap<>();

    // The Admin object that handles admin-specific operations.
    private Admin admin;

    /**
     * Constructor for the Admin_Interface class.
     * Initializes the interface with the user database and the user acting as an admin.
     *
     * @param userDatabase The map containing all user data.
     * @param user         The user who is using the admin interface (must have admin privileges).
     */
    public Admin_Interface(Map<String, User> userDatabase, User user) {
        this.userDatabase = userDatabase;

        // If the user is not already an admin, make them one.
        if (!user.getIsAdmin()) {
            Admin admin = new Admin(user.getUserName(), user.getPassword(), userDatabase);
            this.admin = admin;
        } else {
            // Cast the existing user to Admin.
            this.admin = (Admin) user;
        }
    }

    /**
     * The main entry point for the JavaFX application.
     * It shows the admin panel when the application starts.
     *
     * @param primaryStage The primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        showAdminPanel(primaryStage); // Display the admin panel.
    }

    /**
     * Displays the admin panel with various buttons to manage users (invite, reset password, delete, list, modify roles, etc.).
     *
     * @param primaryStage The stage on which the admin panel is displayed.
     */
    private void showAdminPanel(Stage primaryStage) {
        // Create a grid layout with spacing for controls
        GridPane grid = new GridPane();
        grid.setHgap(10); // Horizontal gap between elements
        grid.setVgap(10); // Vertical gap between elements

        // Creating labels and buttons for the admin interface
        Label adminLabel = new Label("Admin Panel");
        Button inviteButton = new Button("Invite User");
        Button resetButton = new Button("Reset User Password");
        Button deleteButton = new Button("Delete User");
        Button listButton = new Button("List Users");
        Button addRoleButton = new Button("Add/Remove Role");
        Button logoutButton = new Button("Logout");
        Label messageLabel = new Label();

        // Adding the buttons and labels to the grid layout
        grid.add(adminLabel, 0, 0);
        grid.add(inviteButton, 0, 1);
        grid.add(resetButton, 0, 2);
        grid.add(deleteButton, 0, 3);
        grid.add(listButton, 0, 4);
        grid.add(addRoleButton, 0, 5);
        grid.add(logoutButton, 0, 6);
        grid.add(messageLabel, 0, 7);

        // Setting event listeners for each button to trigger the respective actions
        inviteButton.setOnAction(event -> inviteUser(primaryStage, messageLabel));
        resetButton.setOnAction(event -> resetUserPassword(primaryStage, messageLabel));
        deleteButton.setOnAction(event -> deleteUser(primaryStage, messageLabel));
        listButton.setOnAction(event -> listUsers(primaryStage, messageLabel));
        addRoleButton.setOnAction(event -> modifyUserRoles(primaryStage, messageLabel));
        logoutButton.setOnAction(event -> logout(primaryStage));

        // Creating the scene and setting it to the primary stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens the invitation window to invite a new user to the system.
     * The admin can specify a username and a role for the new user.
     *
     * @param primaryStage   The stage for the invitation window.
     * @param messageLabel   The label used to display messages about the invitation process.
     */
    private void inviteUser(Stage primaryStage, Label messageLabel) {
        // Creating a new stage for inviting users
        Stage inviteStage = new Stage();
        GridPane grid = new GridPane(); // Grid layout for the invite window
        grid.setHgap(10);
        grid.setVgap(10);

        // Labels and input fields for the invite window
        Label usernameLabel = new Label("New Username:");
        TextField usernameField = new TextField();
        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();
        Button inviteButton = new Button("Invite");
        Label inviteMessage = new Label();

        // Adding elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(roleLabel, 0, 2);
        grid.add(roleField, 1, 2);
        grid.add(inviteButton, 1, 3);
        grid.add(inviteMessage, 1, 4);

        // Event handling for inviting a user
        inviteButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();

            // Check if both username and role are filled
            if (!username.isEmpty() && !role.isEmpty()) {
                char[] password = "temporary".toCharArray(); // Temporary password
                User newUser = new User(username, password);

                // Creating a new Student or Instructor based on the role entered
                if (role.equals("Student")) {
                    Student student = new Student(username, password, userDatabase);
                    newUser = student;
                    newUser.setIsStudent(); // Setting the user as a Student
                } else if (role.equals("Instructor")) {
                    Instructor instructor = new Instructor(username, password, userDatabase);
                    newUser = instructor;
                    newUser.setIsInstructor(); // Setting the user as an Instructor
                }

                // Adding the role and OTC (One-Time Code) to the new user
                newUser.addRole(role);
                newUser.setOTC();
                userDatabase.put(username, newUser); // Adding the new user to the database
                inviteMessage.setText("One Time Code sent to " + username);
            } else {
                inviteMessage.setText("Please fill in all fields."); // Error message if fields are empty
            }
        });

        // Setting up the invite stage and displaying it
        Scene scene = new Scene(grid, 300, 200);
        inviteStage.setScene(scene);
        inviteStage.setTitle("Invite User");
        inviteStage.show();
    }

    /**
     * Opens a window that allows the admin to reset a user's password.
     *
     * @param primaryStage   The stage for the reset window.
     * @param messageLabel   The label used to display messages about the password reset process.
     */
    private void resetUserPassword(Stage primaryStage, Label messageLabel) {
        // Creating a new stage for resetting a user's password
        Stage resetStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Labels and input fields for the reset window
        Label usernameLabel = new Label("Username to Reset:");
        TextField usernameField = new TextField();
        Button resetButton = new Button("Reset Password");
        Label resetMessage = new Label();

        // Adding elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(resetButton, 1, 2);
        grid.add(resetMessage, 1, 3);

        // Event handling for resetting the password
        resetButton.setOnAction(event -> {
            String username = usernameField.getText();

            // Check if username is provided
            if (!username.isEmpty()) {
                admin.resetUserPassword(username, userDatabase); // Resetting password using the admin object
                resetMessage.setText("Email sent to user.");
            } else {
                resetMessage.setText("Please fill in all fields."); // Error message if the field is empty
            }
        });

        // Setting up the reset stage and displaying it
        Scene scene = new Scene(grid, 300, 200);
        resetStage.setScene(scene);
        resetStage.setTitle("Reset User Password");
        resetStage.show();
    }

    /**
     * Opens a window that allows the admin to delete a user from the system.
     *
     * @param primaryStage   The stage for the delete window.
     * @param messageLabel   The label used to display messages about the deletion process.
     */
    private void deleteUser(Stage primaryStage, Label messageLabel) {
        // Creating a new stage for deleting a user
        Stage deleteStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Labels and input fields for the delete window
        Label usernameLabel = new Label("Username to Delete:");
        TextField usernameField = new TextField();
        Button deleteButton = new Button("Delete User");
        Label deleteMessage = new Label();

        // Adding elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(deleteButton, 1, 2);
        grid.add(deleteMessage, 1, 3);

        // Event handling for deleting the user
        deleteButton.setOnAction(event -> {
            String username = usernameField.getText();

            // Check if the username is provided and exists in the database
            if (!username.isEmpty() && userDatabase.containsKey(username)) {
                admin.deleteUser(username); // Deleting the user
                deleteMessage.setText(username + " deleted.");
            } else {
                deleteMessage.setText("User does not exist."); // Error message if user not found
            }
        });

        // Setting up the delete stage and displaying it
        Scene scene = new Scene(grid, 300, 200);
        deleteStage.setScene(scene);
        deleteStage.setTitle("Delete User");
        deleteStage.show();
    }

    /**
     * Opens a window that shows the list of users in the system.
     *
     * @param primaryStage   The stage for displaying the user list.
     * @param messageLabel   The label used to display messages about the user list.
     */
    private void listUsers(Stage primaryStage, Label messageLabel) {
        StringBuilder userList = new StringBuilder();

        // Looping through the user database and appending usernames and roles to the list
        for (User user : userDatabase.values()) {
            userList.append("User: ").append(user.getFirstName()).append(" ").append(user.getLastName())
                    .append(", Roles: ").append(user.getRoles()).append("\n");
        }

        // Displaying the list of users in an alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION, userList.toString());
        alert.setTitle("User List");
        alert.show();
    }

    /**
     * Opens a window that allows the admin to add or remove roles from a user.
     *
     * @param primaryStage   The stage for the modify roles window.
     * @param messageLabel   The label used to display messages about modifying roles.
     */
    private void modifyUserRoles(Stage primaryStage, Label messageLabel) {
        // Creating a new stage for modifying user roles
        Stage modifyStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Labels and input fields for the modify roles window
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();
        Button addButton = new Button("Add Role");
        Button removeButton = new Button("Remove Role");
        Label modifyMessage = new Label();

        // Adding elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(roleLabel, 0, 1);
        grid.add(roleField, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(removeButton, 2, 2);
        grid.add(modifyMessage, 1, 3);

        // Event handling for adding a role
        addButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            User user = userDatabase.get(username);

            // Check if both username and role are provided
            if (!username.isEmpty() && !role.isEmpty()) {
                String message = admin.addRoleToUser(user, role); // Adding role using the admin object
                modifyMessage.setText(message);
            } else {
                modifyMessage.setText("Please provide username and role."); // Error message if fields are empty
            }
        });

        // Event handling for removing a role
        removeButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            User user = userDatabase.get(username);

            // Check if both username and role are provided
            if (!username.isEmpty() && !role.isEmpty()) {
                admin.removeRoleFromUser(user, role); // Removing role using the admin object
                modifyMessage.setText("Role " + role + " removed from " + username);
            } else {
                modifyMessage.setText("Please provide username and role."); // Error message if fields are empty
            }
        });

        // Setting up the modify stage and displaying it
        Scene scene = new Scene(grid, 400, 200);
        modifyStage.setScene(scene);
        modifyStage.setTitle("Modify User Roles");
        modifyStage.show();
    }

    /**
     * Logs the admin out and redirects to the login screen.
     * Displays a brief logout message before redirecting.
     *
     * @param primaryStage   The stage used for the logout message and redirection.
     */
    private void logout(Stage primaryStage) {
        System.out.println("Logging out."); // Logging to console
        Label loggingOutLabel = new Label("Logging out..."); // Displaying logout message
        GridPane grid = new GridPane();
        grid.add(loggingOutLabel, 0, 0);

        // Creating the scene for the logout message
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        // Adding a delay of 2 seconds before redirecting to the login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> redirectToLogin(primaryStage));
        pause.play(); // Start the pause transition
    }

    /**
     * Redirects the user to the login screen.
     *
     * @param primaryStage The stage where the login screen will be displayed.
     */
    private void redirectToLogin(Stage primaryStage) {
        // Creating a new login screen instance and starting it
        LoginScreen loginPage = new LoginScreen(userDatabase);
        loginPage.start(primaryStage);
    }
}
