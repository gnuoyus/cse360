package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Role_Interface extends Application {

    private Map<String, User> userDatabase = new HashMap<>();
    private User user;
    
    public Role_Interface(Map<String, User> userDatabase, User user) {
    	this.userDatabase = userDatabase;
    	this.user = user;
    }
    
    public void printData() {
    	for (Map.Entry<String, User> entry : userDatabase.entrySet()) {
    	    String username = entry.getKey();
    	    User user = entry.getValue();
    	    System.out.print("Username: " + username);
    	    Set<String> roles = user.getRoles();
            Iterator<String> iterator = roles.iterator();
            if (iterator.hasNext()) {
                String firstElement = iterator.next();
                System.out.println(" User's First Role: " + firstElement);
            }
    	}
    }

    @Override
    public void start(Stage primaryStage) {
    	showRolePanel(primaryStage);
    }

    public void showRolePanel(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        ListView<String> roleListView = new ListView<>();

        if (user != null) {
            roleListView.getItems().addAll(user.getRoles());
        } else {
            System.out.println("User not found.");
        }

        Label selectedRoleLabel = new Label("Selected Role: None");

        Button selectButton = new Button("Select Role");
        selectButton.setOnAction(event -> {
            String selectedRole = roleListView.getSelectionModel().getSelectedItem();
            if (selectedRole != null) {
                selectedRoleLabel.setText("Selected Role: " + selectedRole);
                if (selectedRole.equals("Admin")) {
                	Admin_Interface adminPage = new Admin_Interface(userDatabase, user);
                    adminPage.start(primaryStage);
                } else if (selectedRole.equals("Student"))  {
                    Student_Interface studentPage = new Student_Interface(userDatabase, user);
                    studentPage.start(primaryStage);           	
                } else if (selectedRole.equals("Instructor")) {
                	Instructor_Interface instructorPage = new Instructor_Interface(userDatabase, user);
                    instructorPage.start(primaryStage);                 	
                }
            } else {
                selectedRoleLabel.setText("Please select a role.");
            }
        });

        gridPane.add(new Label("Available Roles:"), 0, 0);
        gridPane.add(roleListView, 0, 1);
        gridPane.add(selectButton, 0, 2);
        gridPane.add(selectedRoleLabel, 0, 3);

        Scene scene = new Scene(gridPane, 300, 250);
        primaryStage.setTitle("Role Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
