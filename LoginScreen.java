package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * LoginScreen is the main login page where users can log in or set up their accounts.
 * It handles login validation, password creation, and account setup for new users.
 */
public class LoginScreen extends Application {

    private Map<String, User> userDatabase = new HashMap<>(); // Stores user information in a database

    /**
     * Default constructor for LoginScreen.
     */
    public LoginScreen() {
    }
    
    /**
     * Constructor with a user database.
     * @param userDatabase The map containing user information.
     */
    public LoginScreen(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }
    
    /**
     * Prints the data of users and their roles from the user database.
     */
    public void printData() {
        for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
            String username = entry.getKey(); // Get the username
            User user = entry.getValue(); // Get the user object
            System.out.println("Username: " + username);

            Set<String> roles = user.getRoles(); // Get the user's roles
            Iterator<String> iterator = roles.iterator();
            while (iterator.hasNext()) {
                String role = iterator.next(); // Iterate through roles
                System.out.println(" User's Roles: " + role);
            }
        }
    }

    /**
     * Start method to display the login page.
     * @param primaryStage The main application window.
     */
    @Override
    public void start(Stage primaryStage) {
        showLoginPage(primaryStage); // Display the login page
    }

    /**
     * Displays the login page for the user to enter their credentials.
     * @param primaryStage The main application window.
     */
    public void showLoginPage(Stage primaryStage) {
        GridPane grid = new GridPane(); // Create a layout grid
        grid.setHgap(10); // Set horizontal spacing
        grid.setVgap(10); // Set vertical spacing

        // Create UI elements
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label codeLabel = new Label("One-Time Code:");
        TextField codeField = new TextField();
        Button loginButton = new Button("Login");
        Label messageLabel = new Label(); // To display validation or error messages

        // Label to show username format requirements
        Label usernameRequirementsLabel = new Label("Username must start with a letter, be 4-16 characters long and \n no special characters");

        // Add elements to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(codeLabel, 1, 2);
        grid.add(codeField, 1, 3);
        grid.add(usernameRequirementsLabel, 1, 4); // Username guidelines
        grid.add(loginButton, 1, 5);
        grid.add(messageLabel, 1, 6);

        // Set action for login button
        loginButton.setOnAction(event -> {
            String username = usernameField.getText(); // Get entered username
            char[] password = passwordField.getText().toCharArray(); // Get entered password
            String code = codeField.getText(); // Get one-time code (if any)

            User currentUser = userDatabase.get(username); // Fetch user from the database
            if (currentUser == null) {
                // If the user is not found, validate username
                String usernameValidationResult = UserNameRecognizer.checkForValidUserName(username);
                if (!usernameValidationResult.isEmpty()) {
                    messageLabel.setText(usernameValidationResult); // Show validation error
                    return; // Stop further execution
                }

                // If no users exist in the database, create the first Admin user
                if (userDatabase.isEmpty()) {
                    Admin admin = new Admin(username, password, userDatabase);
                    admin.addRole("Admin");
                    userDatabase.put(username, admin); // Add new admin to database
                    currentUser = admin;
                    currentUser.setIsAdmin();
                    showCreatePasswordPage(primaryStage, currentUser); // Proceed to set password
                } else {
                    messageLabel.setText("Enter a One-Time Code"); // Ask for the code if user exists
                }
            } else {
                // Existing user validation
                if (!currentUser.getAccountSetUp() && code.equals("123")) {
                    messageLabel.setText("Finish setting up account...");
                    showCreatePasswordPage(primaryStage, currentUser); // Proceed to password setup
                } else if (Arrays.equals(currentUser.getPassword(), password)) {
                    // Password match check
                    if (currentUser.getReset()) {
                        // Handle password reset within the deadline
                        if (currentUser.deadLine.isAfter(LocalDateTime.now()) || currentUser.deadLine.isEqual(LocalDateTime.now())) {
                            showCreatePasswordPage(primaryStage, currentUser); // Redirect to password creation
                            currentUser.undoReset(); // Mark reset as completed
                        } else {
                            messageLabel.setText("Reset deadline has passed."); // Deadline missed
                            currentUser.undoReset();
                        }
                    } else {
                        messageLabel.setText("Login successful!"); // Successful login
                        Role_Interface rolePage = new Role_Interface(userDatabase, currentUser); // Proceed to the role interface
                        rolePage.start(primaryStage); // Display the next screen
                    }
                } else {
                    messageLabel.setText("Invalid username or password."); // Show error for incorrect credentials
                }
            }
        });

        // Set the scene and display the login window
        Scene scene = new Scene(grid, 400, 280);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Displays the page for creating or resetting the user's password.
     * @param primaryStage The main application window.
     * @param user The current user whose password is being set or reset.
     */
    private void showCreatePasswordPage(Stage primaryStage, User user) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        Button submitButton = new Button("Submit Password");
        Label messageLabel = new Label();

        // Display password requirements
        Label passwordRequirementsLabel = new Label("Password must have: \n- At least one uppercase letter \n- At least one lowercase letter \n- At least one digit \n- At least one special character \n- At least 8 characters.");

        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(confirmPasswordLabel, 0, 1);
        grid.add(confirmPasswordField, 1, 1);
        grid.add(submitButton, 1, 2);
        grid.add(messageLabel, 1, 3);
        grid.add(passwordRequirementsLabel, 1, 4);

        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Validate password
            String evaluationResult = PasswordEvaluator.evaluatePassword(password); // Check password strength

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("Please enter both password fields.");
            } else if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match. Please try again.");
            } else if (!evaluationResult.isEmpty()) {
                messageLabel.setText("Password does not meet the requirements: " + evaluationResult); // Show password requirements not met
            } else {
                messageLabel.setText("Password set successfully!"); // Password set successfully
                user.setPassword(password.toCharArray());

                if (!user.getAccountSetUp()) {
                    showSetupAccountPage(primaryStage, user); // Redirect to account setup page if not completed
                } else {
                    showLoginPage(primaryStage); // Return to login page after setting password
                }
            }
        });

        // Set the scene and display the password creation window
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setTitle("Create Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Displays the page for setting up additional account details (email, name, etc.).
     * @param primaryStage The main application window.
     * @param user The current user setting up their account.
     */
    private void showSetupAccountPage(Stage primaryStage, User user) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // UI elements for account setup
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        Label middleNameLabel = new Label("Middle Name:");
        TextField middleNameField = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        Label preferredNameLabel = new Label("Preferred First Name (optional):");
        TextField preferredNameField = new TextField();

        Button finishButton = new Button("Finish Setup");
        Label messageLabel = new Label();

        // Add fields to the grid
        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(middleNameLabel, 0, 2);
        grid.add(middleNameField, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameField, 1, 3);
        grid.add(preferredNameLabel, 0, 4);
        grid.add(preferredNameField, 1, 4);
        grid.add(finishButton, 1, 5);
        grid.add(messageLabel, 1, 6);

        // Set action for finishing account setup
        finishButton.setOnAction(event -> {
            String email = emailField.getText(); // Get email input
            String firstName = firstNameField.getText(); // Get first name input
            String lastName = lastNameField.getText(); // Get last name input

            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                messageLabel.setText("Please fill in the required fields: Email, First Name, and Last Name.");
            } else {
                messageLabel.setText("Account setup completed successfully!"); // Confirmation message
                user.setEmail(email); // Set user details
                user.setFirstName(firstName);
                user.setMiddleName(middleNameField.getText());
                user.setLastName(lastName);
                user.setPreferredFirstName(preferredNameField.getText());

                emailField.clear(); // Clear fields after submission
                firstNameField.clear();
                middleNameField.clear();
                lastNameField.clear();
                preferredNameField.clear();

                user.finishAccountSetup(); // Mark account setup as complete
                showLoginPage(primaryStage); // Redirect to login page
                printData(); // Print user data for confirmation
            }
        });

        // Set the scene and display the account setup window
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Finish Setting Up Your Account");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
