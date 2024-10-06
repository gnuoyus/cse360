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

public class Admin_Interface extends Application {
    private Map<String, User> userDatabase = new HashMap<>();
    private Admin admin;
    
    public Admin_Interface(Map<String, User> userDatabase, User user) {
        this.userDatabase = userDatabase;
        if (!user.getIsAdmin()) {
        	Admin admin = new Admin(user.getUserName(),user.getPassword(),userDatabase);
        	this.admin = admin;
        } else {
        	this.admin = (Admin)user;
        }
    }

	@Override
    public void start(Stage primaryStage) {
        showAdminPanel(primaryStage);
    }

    private void showAdminPanel(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label adminLabel = new Label("Admin Panel");
        Button inviteButton = new Button("Invite User");
        Button resetButton = new Button("Reset User Password");
        Button deleteButton = new Button("Delete User");
        Button listButton = new Button("List Users");
        Button addRoleButton = new Button("Add/Remove Role");
        Button logoutButton = new Button("Logout");
        Label messageLabel = new Label();

        grid.add(adminLabel, 0, 0);
        grid.add(inviteButton, 0, 1);
        grid.add(resetButton, 0, 2);
        grid.add(deleteButton, 0, 3);
        grid.add(listButton, 0, 4);
        grid.add(addRoleButton, 0, 5);
        grid.add(logoutButton, 0, 6);
        grid.add(messageLabel, 0, 7);

        inviteButton.setOnAction(event -> inviteUser(primaryStage, messageLabel));
        resetButton.setOnAction(event -> resetUserPassword(primaryStage, messageLabel));
        deleteButton.setOnAction(event -> deleteUser(primaryStage, messageLabel));
        listButton.setOnAction(event -> listUsers(primaryStage, messageLabel));
        addRoleButton.setOnAction(event -> modifyUserRoles(primaryStage, messageLabel));
        logoutButton.setOnAction(event -> logout(primaryStage));

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void inviteUser(Stage primaryStage, Label messageLabel) {
        Stage inviteStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("New Username:");
        TextField usernameField = new TextField();

        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();
        Button inviteButton = new Button("Invite");
        Label inviteMessage = new Label();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(roleLabel, 0, 2);
        grid.add(roleField, 1, 2);
        grid.add(inviteButton, 1, 3);
        grid.add(inviteMessage, 1, 4);

        inviteButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();

            if (!username.isEmpty() && !role.isEmpty()) {
                char[] password = "temporary".toCharArray();
                User newUser = new User(username, password);
                if (role.equals("Student")) {
                    Student student = new Student(username, password, userDatabase);
                    newUser = student;
                    newUser.setIsStudent();
                } if (role.equals("Instructor")) {
                    Instructor instructor = new Instructor(username, password, userDatabase);
                    newUser = instructor;
                    newUser.setIsInstructor();                  
                } 
                newUser.addRole(role);
                newUser.setOTC();
                userDatabase.put(username, newUser);
                inviteMessage.setText("One Time Code sent to " + username);
            } else {
                inviteMessage.setText("Please fill in all fields.");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        inviteStage.setScene(scene);
        inviteStage.setTitle("Invite User");
        inviteStage.show();
    }

    private void resetUserPassword(Stage primaryStage, Label messageLabel) {
        Stage resetStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username to Reset:");
        TextField usernameField = new TextField();
        Button resetButton = new Button("Reset Password");
        Label resetMessage = new Label();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(resetButton, 1, 2);
        grid.add(resetMessage, 1, 3);

        resetButton.setOnAction(event -> {
            String username = usernameField.getText();

            if (!username.isEmpty()) {
                admin.resetUserPassword(username, userDatabase);
                resetMessage.setText("Email sent to user.");
            } else {
                resetMessage.setText("Please fill in all fields.");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        resetStage.setScene(scene);
        resetStage.setTitle("Reset User Password");
        resetStage.show();
    }

    private void deleteUser(Stage primaryStage, Label messageLabel) {
        Stage deleteStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username to Delete:");
        TextField usernameField = new TextField();
        Button deleteButton = new Button("Delete");
        Label deleteMessage = new Label();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(deleteButton, 1, 1);
        grid.add(deleteMessage, 1, 2);

        deleteButton.setOnAction(event -> {
            String username = usernameField.getText();
            if (userDatabase.containsKey(username)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + username + "?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        userDatabase.remove(username);
                        deleteMessage.setText(username + " has been deleted.");
                    }
                });
            } else {
                deleteMessage.setText("User not found.");
            }
        });

        Scene scene = new Scene(grid, 300, 150);
        deleteStage.setScene(scene);
        deleteStage.setTitle("Delete User");
        deleteStage.show();
    }

    private void listUsers(Stage primaryStage, Label messageLabel) {
        StringBuilder userList = new StringBuilder("Users:\n");

        for (User user : userDatabase.values()) {
            userList.append("Username: ").append(user.getUserName())
                    .append(", Name: ").append(user.getFirstName()).append(" ").append(user.getLastName())
                    .append(", Roles: ").append(user.getRoles()).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, userList.toString());
        alert.setTitle("User List");
        alert.show();
    }

    private void modifyUserRoles(Stage primaryStage, Label messageLabel) {
        Stage modifyStage = new Stage();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label roleLabel = new Label("Role:");
        TextField roleField = new TextField();
        Button addButton = new Button("Add Role");
        Button removeButton = new Button("Remove Role");
        Label modifyMessage = new Label();

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(roleLabel, 0, 1);
        grid.add(roleField, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(removeButton, 2, 2);
        grid.add(modifyMessage, 1, 3);

        addButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            User user = userDatabase.get(username);
            if (!username.isEmpty() && !role.isEmpty()) {
                String message = admin.addRoleToUser(user, role);  
                modifyMessage.setText(message);
            } else {
                modifyMessage.setText("Please provide username and role.");
            }
        });

        removeButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            User user = userDatabase.get(username);
            if (!username.isEmpty() && !role.isEmpty()) {
                admin.removeRoleFromUser(user, role);  
                modifyMessage.setText("Role " + role + " removed from " + username);
            } else {
                modifyMessage.setText("Please provide username and role.");
            }
        });

        Scene scene = new Scene(grid, 400, 200);
        modifyStage.setScene(scene);
        modifyStage.setTitle("Modify User Roles");
        modifyStage.show();
    }

    private void logout(Stage primaryStage) {
    	System.out.println("Logging out.");
    	Label loggingOutLabel = new Label("Logging out...");
        GridPane grid = new GridPane();
        grid.add(loggingOutLabel, 0, 0);
        
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> redirectToLogin(primaryStage));
        pause.play();
    }

    private void redirectToLogin(Stage primaryStage) {
    	LoginScreen loginPage = new LoginScreen(userDatabase);
        loginPage.start(primaryStage);
    }

}