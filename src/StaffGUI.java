import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.management.Descriptor;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class StaffGUI {
    private Stage stage;
    MainController mainController;

    public StaffGUI(MainController mainController) {
        this.stage = new Stage();
        stage.setTitle("Staff Home");
        this.mainController = mainController;
    }

    public void showHomeScreen() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Button createServiceButton = new Button("Create Service");
        createServiceButton.setOnAction(e -> {
            // Open the service creation form
            showServiceCreationForm();
        });
        
        Button showQuotesButton = new Button("Manage Requested Quotes");
        showQuotesButton.setOnAction(e -> {
            // Open the service creation form
            showRequestedQuotes(mainController.showRequestedQuotes());
        });
        
        Button reportByServiceButton = new Button("Report By Service Revenue");
        reportByServiceButton.setOnAction(e -> {
            // Open the service creation form
        	showRevenueReportForm();
        });
        
        Hyperlink loginPage = new Hyperlink("Back to Login");
        loginPage.setOnAction(e -> {
        	mainController.showLogin();
        	stage.close();
        });

        root.getChildren().addAll(createServiceButton, showQuotesButton, reportByServiceButton, loginPage);

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showServiceCreationForm() {
    	
    	// Set up service creation form layout
        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));
    	
    	 // Create form elements
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField costField = new TextField();
        
        Hyperlink backToHome = new Hyperlink("Back to Home");
        backToHome.setOnAction(e -> {
        	mainController.showStaffInterfaceAfterLogin();
        	stage.close();
        });

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Get input values
            String name = nameField.getText();
            String description = descriptionField.getText();
            String costStr = costField.getText();

            if (name.isEmpty() || description.isEmpty() || costStr.isEmpty()) {
                showErrorAlert("Please fill in all fields.");
                return;
            }
            
            try {
            	double cost = Double.parseDouble(costStr);
            	// Create and add the service to the system
                mainController.addService(name, description, cost);
                // Close the current stage (service creation form)
                nameField.setText(null);
                descriptionField.setText(null);
                costField.setText(null);
                
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid cost.");
            }
        });
        
        formLayout.getChildren().addAll(
                new Label("Service Name:"), nameField,
                new Label("Description:"), descriptionField,
                new Label("Cost:"), costField,
                submitButton,
                backToHome
        );

        Scene scene = new Scene(formLayout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Create Service");
        stage.show();
    }

    public void showRequestedQuotes(List<Appointment> requestedQuotes) {
	    VBox root = new VBox(10);
	    root.setPadding(new Insets(20));
	
	    Label titleLabel = new Label("Requested Quotes:");
	    root.getChildren().add(titleLabel);
	    
	    Hyperlink backToHome = new Hyperlink("Back to Home");
        backToHome.setOnAction(e -> {
        	mainController.showStaffInterfaceAfterLogin();
        	stage.close();
        });
	
        int i=0; 
        
	    for (Appointment quote : requestedQuotes) {
	    	i++;
            // Display each quote with its attributes and buttons
            Label quoteLabel = new Label(i+"- "+"Date: " + quote.getDate() + ", Time: " + quote.getTime() +
                                         ", Duration: " + quote.getDuration() + " minutes, Service: " + quote.getService().getName());
            Button acceptButton = new Button("Accept");
            acceptButton.setOnAction(e -> acceptQuoteAndCreateAppointment(quote));
            Button rejectButton = new Button("Reject");
            rejectButton.setOnAction(e -> rejectQuote(quote));

            VBox quoteBox = new VBox(5);
            quoteBox.getChildren().addAll(quoteLabel, acceptButton, rejectButton);
            root.getChildren().add(quoteBox);
        }
	    
	    root.getChildren().add(backToHome);
	
	    Scene scene = new Scene(root, 520, 300);
	    stage.setScene(scene);
	    stage.show();
	}
    
    
    private void acceptQuoteAndCreateAppointment(Appointment quote) {
        List<User> staff = mainController.getStaff();
        
	    VBox root = new VBox(10);
	    root.setPadding(new Insets(20));
	    
	    Label quoteLabel = new Label("- "+"Date: " + quote.getDate() + ", Time: " + quote.getTime() +
                ", Duration: " + quote.getDuration() + " minutes, Service: " + quote.getService().getName());
	
	    Label titleLabel = new Label("Choose staff member:");
	    root.getChildren().addAll(titleLabel, quoteLabel);
	    
	    Hyperlink backToHome = new Hyperlink("Back to Home");
        backToHome.setOnAction(e -> {
        	mainController.showStaffInterfaceAfterLogin();
        	stage.close();
        });
	
        int i=0; 
        
        for (User member : staff) {
	    	i++;

	    	Label staffLabel = new Label(i+"- "+"Name: " + member.getFirstName()+" "+member.getLastName() + ", Email: " + member.getEmail() +
	                ", Phone: " + member.getPhoneNumber());

            Button acceptButton = new Button("Assign and Create");
            acceptButton.setOnAction(e -> {
            	mainController.acceptQuoteAndCreaeAppointment(quote, member.getUsername());
            	showRequestedQuotes(mainController.showRequestedQuotes());
            });

            VBox quoteBox = new VBox(5);
            quoteBox.getChildren().addAll(acceptButton );
            root.getChildren().addAll(staffLabel, quoteBox);
        }
	    
	    root.getChildren().addAll(backToHome);
	
	    Scene scene = new Scene(root, 530, 200);
	    stage.setScene(scene);
	    stage.show();
        
    }

    private void rejectQuote(Appointment quote) {
        mainController.rejectQuote(quote);
        showRequestedQuotes(mainController.showRequestedQuotes());
    }
    
    public void showRevenueReportForm() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Hyperlink homePage = new Hyperlink("Back to Home");
        homePage.setOnAction(e -> {
            mainController.showStaffInterfaceAfterLogin();
            stage.close();
        });

        Label titleLabel = new Label("Generate Revenue Report by Service:");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        Button generateButton = new Button("Generate Report");
        generateButton.setOnAction(e -> {
            Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
            Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());
            HashMap<String, Double> revenueReport = mainController.generateRevenueReport(startDate, endDate);
            showRevenueReport(revenueReport);
        });

        HBox dateBox = new HBox(10);
        dateBox.getChildren().addAll(startDatePicker, endDatePicker);

        root.getChildren().addAll(titleLabel, dateBox, generateButton, homePage);

        Scene scene = new Scene(root, 400, 150);
        stage.setScene(scene);
        stage.show();
    }
    
    public void showRevenueReport(HashMap<String, Double> revenueReport) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Hyperlink homePage = new Hyperlink("Back to Home");
        homePage.setOnAction(e -> {
            mainController.showStaffInterfaceAfterLogin();
            stage.close();
        });
        
        Label titleLabel = new Label("Revenue Report:");
        root.getChildren().addAll(titleLabel);

        int i=0;
        for (String serviceType : revenueReport.keySet()) {
        	i++;
            double revenue = revenueReport.get(serviceType);
            Label revenueLabel = new Label(i+"- "+"Service Type: " + serviceType + ", Revenue: " + revenue);
            root.getChildren().add(revenueLabel);
        }
        
        root.getChildren().add(homePage);

        Scene scene = new Scene(root, 400, 300);
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
