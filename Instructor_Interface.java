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
 * <p>Instructor_Interface Class</p>
 *
 * <p>Description: Instructor_Interface is the GUI class for instructors.
 * This class displays the instructor panel and handles user interactions like logging out.</p>
 *
 * <p>Author: Group Tu64</p>
 *
 * <p>Version: 1.00 2024-10-02</p>
 */
public class Instructor_Interface extends Application {
    private Map<String, User> userDatabase = new HashMap<>(); // A map to store all users
    private Instructor instructor; // The current instructor using the interface
    
    /**
     * Constructor for the Instructor_Interface class.
     * It initializes the instructor by checking if the user is already an instructor or needs promotion.
     *
     * @param userDatabase The map containing all user data.
     * @param user         The current user who logged in.
     */
    public Instructor_Interface(Map<String, User> userDatabase, User user) {
        this.userDatabase = userDatabase;
        if (!user.getIsInstructor()) { 
            // Promote user to instructor if they are not one already
            Instructor instructor = new Instructor(user.getUserName(), user.getPassword(), userDatabase);
            this.instructor = instructor;
        } else { 
            // User is already an instructor, just assign them
            this.instructor = (Instructor) user;
        }
    }

    /**
     * The JavaFX start method to initiate the GUI.
     * It shows the instructor panel.
     *
     * @param primaryStage The main window of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        showStudentPanel(primaryStage); // Show the panel for instructor interactions
    }

    /**
     * Displays the instructor panel where actions like logging out are provided.
     * 
     * @param primaryStage The main window where the panel is shown.
     */
    private void showStudentPanel(Stage primaryStage) {
        GridPane grid = new GridPane(); // Creating a grid layout
        grid.setHgap(10); // Set horizontal gap between components
        grid.setVgap(10); // Set vertical gap between components

        // Create UI elements
        Label studentLabel = new Label("Instructor Panel");
        Button logoutButton = new Button("Logout");
        Label messageLabel = new Label();

        // Add elements to the grid
        grid.add(studentLabel, 0, 0);
        grid.add(logoutButton, 0, 6);
        grid.add(messageLabel, 0, 7);

        // Set the logout button action
        logoutButton.setOnAction(event -> logout(primaryStage));

        // Create a new scene and set it on the primary stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Instructor Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Handles the logout process. Displays a message and waits for 2 seconds before redirecting.
     * 
     * @param primaryStage The main window to display the logout message.
     */
    private void logout(Stage primaryStage) {
        System.out.println("Logging out."); // Log the action in the console

        // Display a logout message
        Label loggingOutLabel = new Label("Logging out...");
        GridPane grid = new GridPane();
        grid.add(loggingOutLabel, 0, 0);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        // Pause for 2 seconds before moving to the login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> redirectToLogin(primaryStage)); // Redirect after the pause
        pause.play();
    }

    /**
     * Redirects the user to the login screen after logout.
     * 
     * @param primaryStage The main window where the login screen will be displayed.
     */
    private void redirectToLogin(Stage primaryStage) {
        LoginScreen loginPage = new LoginScreen(userDatabase); // Create a new login screen
        loginPage.start(primaryStage); // Start the login screen
    }
}
