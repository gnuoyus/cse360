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

public class LoginScreen extends Application {

    private Map<String, User> userDatabase = new HashMap<>();

    public LoginScreen() {
    }
    
    public LoginScreen(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }
    
    public void printData() {
        for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
            String username = entry.getKey();
            User user = entry.getValue();
            System.out.println("Username: " + username);
            Set<String> roles = user.getRoles();
            Iterator<String> iterator = roles.iterator();
            while (iterator.hasNext()) {
                String firstElement = iterator.next();
                System.out.println(" User's Roles: " + firstElement);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        showLoginPage(primaryStage);
    }

    public void showLoginPage(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label codeLabel = new Label("One-Time Code:");
        TextField codeField = new TextField();
        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        // Add a label to show username requirements
        Label usernameRequirementsLabel = new Label("Username must start with a letter, be 4-16 characters long and \n no special characters");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(codeLabel, 1, 2);
        grid.add(codeField, 1, 3);
        grid.add(usernameRequirementsLabel, 1, 4); // Display username requirements
        grid.add(loginButton, 1, 5);
        grid.add(messageLabel, 1, 6);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            char[] password = passwordField.getText().toCharArray();
            String code = codeField.getText();

            // Validate the username using UserNameRecognizer through User class
            User currentUser = userDatabase.get(username);
            if (currentUser == null) {
                String usernameValidationResult = UserNameRecognizer.checkForValidUserName(username);
                if (!usernameValidationResult.isEmpty()) {
                    messageLabel.setText(usernameValidationResult); // Show validation error
                    return; // Stop further execution if the username is invalid
                }

                if (userDatabase.isEmpty()) {
                    Admin admin = new Admin(username, password, userDatabase);
                    admin.addRole("Admin");
                    userDatabase.put(username, admin);
                    currentUser = admin;
                    currentUser.setIsAdmin();
                    showCreatePasswordPage(primaryStage, currentUser);
                } else {
                    messageLabel.setText("Enter a One-Time Code");
                }
            } else if (currentUser != null) {
                if (!currentUser.getAccountSetUp() && code.equals("123")) {
                    messageLabel.setText("Finish setting up account...");
                    showCreatePasswordPage(primaryStage, currentUser);
                } else if (Arrays.equals(currentUser.getPassword(), password)) {
                    if (currentUser.getReset()) {
                        if(currentUser.deadLine.isAfter(LocalDateTime.now()) || currentUser.deadLine.isEqual(LocalDateTime.now())) {
                            showCreatePasswordPage(primaryStage, currentUser);
                            currentUser.undoReset();
                        } else {
                            messageLabel.setText("Reset deadline has passed.");
                            currentUser.undoReset();
                        }        
                    } else {
                        messageLabel.setText("Login successful!");
                        Role_Interface rolePage = new Role_Interface(userDatabase, currentUser);
                        rolePage.start(primaryStage);                        
                    }
                } else {
                    messageLabel.setText("Invalid username or password.");
                }
            }
        });

        Scene scene = new Scene(grid, 400, 280);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();        
    }

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
        Label passwordRequirmentsLabel = new Label("Password must have: \n- At least one uppercase letter \n-"
        		+ " At least one lowercase letter \n- At least one digit"
        		+ " \n- At least one special character"
        		+ " \n- At least 8 characters.");

        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(confirmPasswordLabel, 0, 1);
        grid.add(confirmPasswordField, 1, 1);
        grid.add(submitButton, 1, 2);
        grid.add(messageLabel, 1, 3);
        grid.add(passwordRequirmentsLabel, 1, 4);

        submitButton.setOnAction(event -> {
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Call PasswordEvaluator to check password
            String evaluationResult = PasswordEvaluator.evaluatePassword(password);

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("Please enter both password fields.");
            } else if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match. Please try again.");
            } else if (!evaluationResult.isEmpty()) {
                // If password does not meet requirements, show error message
                messageLabel.setText("Password does not meet the password requirements: " + evaluationResult);
            } else {
                // If password is valid, proceed with setting it for the user
                messageLabel.setText("Password set successfully!");
                user.setPassword(password.toCharArray());

                if (!user.getAccountSetUp()) {
                    showSetupAccountPage(primaryStage, user);
                } else {
                    showLoginPage(primaryStage);
                }
            }
        });

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setTitle("Create Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSetupAccountPage(Stage primaryStage, User user) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

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

        finishButton.setOnAction(event -> {
            String email = emailField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();

            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                messageLabel.setText("Please fill in the required fields: Email, First Name, and Last Name.");
            } else {
                messageLabel.setText("Account setup completed successfully!");
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setMiddleName(middleNameField.getText());
                user.setLastName(lastName);
                user.setPreferredFirstName(preferredNameField.getText());

                emailField.clear();
                firstNameField.clear();
                middleNameField.clear();
                lastNameField.clear();
                preferredNameField.clear();

                user.finishAccountSetup();
                showLoginPage(primaryStage);
                printData();
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Finish Setting Up Your Account");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
