//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class RegisterScreen {
//
//    private MainController mainController;
//    private Stage stage;
//
//    public RegisterScreen(MainController mainController) {
//        this.mainController = mainController;
//        this.stage = new Stage();
//        stage.setTitle("Register");
//    }
//
//    public void display() {
//        // Set up register screen layout
//        VBox root = new VBox(10);
//        root.setPadding(new Insets(20));
//        TextField usernameField = new TextField();
//        usernameField.setPromptText("Username");
//        PasswordField passwordField = new PasswordField();
//        passwordField.setPromptText("Password");
//        ComboBox<String> userTypeComboBox = new ComboBox<>();
//        
//        userTypeComboBox.getItems().addAll("Customer", "Staff");
//        userTypeComboBox.setPromptText("Select User Type");
//        
//        Hyperlink loginLink = new Hyperlink("Login");
//        loginLink.setOnAction(e -> {
//        	mainController.showLogin();
//        	stage.close();
//        });
//
//        Button registerButton = new Button("Register");
//        registerButton.setOnAction(e -> {
//            String username = usernameField.getText();
//            String password = passwordField.getText();
//            String userType = userTypeComboBox.getValue();
//            if (username.isEmpty() || password.isEmpty() || userType == null) {
//                showErrorAlert("Please fill in all fields.");
//            } else {
//                // Register user
//            	boolean response = mainController.registerUser(username, password, userType.equals("Customer") ? UserType.CUSTOMER : UserType.STAFF);
//                if(response) {
//                	showSuccessAlert("User registered successfully.");
//                	// Show the login screen again
//                    LoginScreen loginScreen = new LoginScreen(mainController);
//                    loginScreen.display();
//                    stage.close();
//                }
//                else {
//                	showErrorAlert("UserName not available. Try another");
//                }
//            }
//        });
//
//        root.getChildren().addAll(usernameField, passwordField, userTypeComboBox, registerButton, loginLink);
//
//        // Set up register scene
//        Scene scene = new Scene(root, 300, 200);
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    // Method to show success alert
//    private void showSuccessAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Method to show error alert
//    private void showErrorAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen {

    private MainController mainController;
    private Stage stage;

    public RegisterScreen(MainController mainController) {
        this.mainController = mainController;
        this.stage = new Stage();
        stage.setTitle("Register");
    }

    public void display() {
        
    	// Set up register screen layout
        
    	ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Staff");
        userTypeComboBox.setPromptText("Select User Type");
    	
    	VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        // Additional fields for Customer
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField paymentDetailsField = new TextField();
        paymentDetailsField.setPromptText("Payment Details");

        // Additional fields for Staff
        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        // Set up login link
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setOnAction(e -> {
            mainController.showLogin();
            stage.close();
        });

        // Add fields based on user type
        userTypeComboBox.setOnAction(e -> {
            String userType = userTypeComboBox.getValue();
            if (userType != null) {
                if (userType.equals("Customer")) {
                    root.getChildren().addAll(addressField, paymentDetailsField);
                    root.getChildren().removeAll(roleField);
                } else {
                    root.getChildren().addAll(roleField);
                    root.getChildren().removeAll( addressField, paymentDetailsField);
                }
            }
        });
        
        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
        	String fn = firstNameField.getText();
        	String ln = lastNameField.getText();
        	String email = emailField.getText();
        	String phone = phoneNumberField.getText();
        	String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = userTypeComboBox.getValue();
            
            if (fn.isEmpty() || ln.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            		username.isEmpty() || password.isEmpty() || userType == null) {
                showErrorAlert("Please fill in all fields.");
            } else {
                // Register user
                boolean response=false;
                if (userType.equals("Customer")) {
                	String address = addressField.getText();
                	String payment = paymentDetailsField.getText();
                	if(address.isEmpty() || payment.isEmpty()) {
                		showErrorAlert("Please fill in all fields.");
                		return;
                	}
                	else {
                		response = mainController.registerCustomer(fn, ln, email, phone, username, password,
                				address, payment);
                	}
                } else {
                	String role = roleField.getText();
                	if(role.isEmpty()) {
                		showErrorAlert("Please fill in all fields.");
                		return;
                	}
                	else {
                		response = mainController.registerStaff(fn, ln, email, phone, username, password, 
                				role);
                	}
                }
                if (response) {
                    showSuccessAlert("User registered successfully.");
                    // Show the login screen again
                    LoginScreen loginScreen = new LoginScreen(mainController);
                    loginScreen.display();
                    stage.close();
                } else {
                    showErrorAlert("Username not available. Try another");
                }
            }
        });

        root.getChildren().addAll(firstNameField, lastNameField, emailField, phoneNumberField,
        		usernameField, passwordField, userTypeComboBox, registerButton, loginLink);

        // Set up register scene
        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.show();
    }

    // Method to show success alert
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
