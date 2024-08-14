
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

    private MainController mainController;
    private Stage stage;

    public LoginScreen(MainController mainController) {
        this.mainController = mainController;
        this.stage = new Stage();
        stage.setTitle("Login");
    }

    public void display() {
        // Set up login screen layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Staff");
        userTypeComboBox.setPromptText("Select User Type");
        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setOnAction(e -> {
        	mainController.showRegister();
        	stage.close();
        });

        Button sessionEndButton = new Button("End Session");
        sessionEndButton.setOnAction(e -> {
        	mainController.EndSession();
        });
        
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = userTypeComboBox.getValue();
            if (username.isEmpty() || password.isEmpty() || userType == null) {
                showErrorAlert("Please fill in all fields.");
            } else {
                // Check credentials and show respective screens
            	String res = mainController.login(username, password, userType);
            	if(res!=null) {
            		if(userType.equals("Customer")) {
            			mainController.showCustomerInterfaceAfterLogin();
            		}
            		else if (userType.equals("Staff")) {
            			mainController.showStaffInterfaceAfterLogin();
            		}
            		mainController.setUsername(username);
            		stage.close();
            	} else {
            		showErrorAlert("Invalid credentials.");
            	}
            }
        });

        root.getChildren().addAll(usernameField, passwordField, userTypeComboBox, loginButton, registerLink, sessionEndButton);

        // Set up login scene
        Scene scene = new Scene(root, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    // Method to show error alert
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
