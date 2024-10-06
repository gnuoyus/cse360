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
    private Map<String, User> userDatabase = new HashMap<>();
    private Student student;
    
    public Student_Interface(Map<String, User> userDatabase, User user) {
        this.userDatabase = userDatabase;
        if (!user.getIsStudent()) {
        	Student student = new Student(user.getUserName(),user.getPassword(),userDatabase);
        	this.student = student;
        } else {
        	this.student = (Student)user;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        showStudentPanel(primaryStage);
    }

    private void showStudentPanel(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label studentLabel = new Label("Student Panel");
        Button logoutButton = new Button("Logout");
        Label messageLabel = new Label();

        grid.add(studentLabel, 0, 0);
        grid.add(logoutButton, 0, 6);
        grid.add(messageLabel, 0, 7);

        logoutButton.setOnAction(event -> logout(primaryStage));

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Student Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void logout(Stage primaryStage) {
    	System.out.println("Logging out.");
    	Label loggingOutLabel = new Label("Logging out...");
        GridPane grid = new GridPane();
        grid.add(loggingOutLabel, 0, 0);
        
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        
        // Pause for 2 seconds before redirecting to login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> redirectToLogin(primaryStage));
        pause.play();
    }

    private void redirectToLogin(Stage primaryStage) {
    	LoginScreen loginPage = new LoginScreen(userDatabase);
        loginPage.start(primaryStage);
    }
}
