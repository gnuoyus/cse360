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

public class Student_Interface extends Application {
    private Map<String, User> userDatabase = new HashMap<>(); // Database to store user information
    private Student student; // Current student user

    /**
     * Constructor for the Student_Interface class.
     *
     * @param userDatabase The database of users
     * @param user The current user
     */
    public Student_Interface(Map<String, User> userDatabase, User user) {
        this.userDatabase = userDatabase; // Initialize the user database
        if (!user.getIsStudent()) { // Check if the current user is not a student
            Student student = new Student(user.getUserName(), user.getPassword(), userDatabase);
            this.student = student; // Create a new student instance
        } else {
            this.student = (Student) user; // Cast the current user to Student
        }
    }

    @Override
    public void start(Stage primaryStage) {
        showStudentPanel(primaryStage); // Display the student panel
    }

    /**
     * Displays the main panel for student interactions.
     *
     * @param primaryStage The primary stage for the application
     */
    private void showStudentPanel(Stage primaryStage) {
        GridPane grid = new GridPane(); // Create a layout for the panel
        grid.setHgap(10);
        grid.setVgap(10);

        Label studentLabel = new Label("Student Panel"); // Title for the panel
        Button logoutButton = new Button("Logout"); // Button to log out
        Label messageLabel = new Label(); // Label for messages

        grid.add(studentLabel, 0, 0); // Add the title to the grid
        grid.add(logoutButton, 0, 6); // Add the logout button
        grid.add(messageLabel, 0, 7); // Add the message label

        logoutButton.setOnAction(event -> logout(primaryStage)); // Handle logout button click

        Scene scene = new Scene(grid, 400, 300); // Create a scene with the grid layout
        primaryStage.setTitle("Student Panel"); // Set the title for the stage
        primaryStage.setScene(scene); // Set the scene
        primaryStage.show(); // Display the stage
    }

    /**
     * Handles the logout process.
     *
     * @param primaryStage The primary stage for the application
     */
    private void logout(Stage primaryStage) {
        System.out.println("Logging out."); // Log message for debugging
        Label loggingOutLabel = new Label("Logging out..."); // Label indicating logout process
        GridPane grid = new GridPane(); // Create a new grid layout
        grid.add(loggingOutLabel, 0, 0); // Add the logout label to the grid
        
        Scene scene = new Scene(grid, 400, 300); // Create a scene with the grid layout
        primaryStage.setScene(scene); // Set the scene for the primary stage
        
        // Pause for 2 seconds before redirecting to the login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> redirectToLogin(primaryStage)); // Redirect after pause
        pause.play(); // Start the pause
    }

    /**
     * Redirects the user to the login screen.
     *
     * @param primaryStage The primary stage for the application
     */
    private void redirectToLogin(Stage primaryStage) {
        LoginScreen loginPage = new LoginScreen(userDatabase); // Create a new login screen
        loginPage.start(primaryStage); // Start the login screen
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
