import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerGUI {

	private Stage stage;
	private MainController mainController;

	public CustomerGUI(MainController mainController) {
		this.stage = new Stage();
		stage.setTitle("Customer Panel");
		this.mainController = mainController;
	}

	public void showHomeScreen() {
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));

		Button showServiceButton = new Button("Show Services");
		showServiceButton.setOnAction(e -> {
			// Open the service creation form
			showAvailableServices(mainController.getAvailableServices());
		});
		
		Button viewAppointmentsButton = new Button("Appointments");
		viewAppointmentsButton.setOnAction(e -> {
			// Open the service creation form
			showAppointments(mainController.showAppointments());
		});
		
		Hyperlink loginPage = new Hyperlink("Back to Login");
        loginPage.setOnAction(e -> {
        	mainController.showLogin();
        	stage.close();
        });

		root.getChildren().addAll(showServiceButton, viewAppointmentsButton, loginPage);

		Scene scene = new Scene(root, 300, 200);
		stage.setScene(scene);
		stage.show();
	}

	private void showAppointments(List<Appointment> appointments) {
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));

		Hyperlink homePage = new Hyperlink("Back to Home");
		homePage.setOnAction(e -> {
        	mainController.showCustomerInterfaceAfterLogin();
        	stage.close();
        });

		Label titleLabel = new Label("All Appointments:");
		root.getChildren().add(titleLabel);
		
		int i=0;
		for (Appointment app : appointments) {
			i++;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(app.getDate());
            
			Label appLabel = new Label(i+"- "+"Date: " + formattedDate + ", Time: " + app.getTime() +
                    ", Duration: " + app.getDuration() + " minutes, Service: " + app.getService().getName());

			Calendar currentDate = Calendar.getInstance();
            Calendar appointmentDate = Calendar.getInstance();
            appointmentDate.setTime(app.getDate());
            
            Button updateButton =  new Button();
            
            if (currentDate.before(appointmentDate)) {
			
            	updateButton= new Button("Update");
				int finalI = i; // To use inside lambda expression
				updateButton.setOnAction(e -> updateAppointment(finalI, appointments));
            }
			
			VBox appointmentBox = new VBox(5);
			appointmentBox.getChildren().addAll(appLabel, updateButton);
            root.getChildren().add(appointmentBox);
			
		}

		root.getChildren().add(homePage);
		
		Scene scene = new Scene(root, 530, 250);
		stage.setScene(scene);
		stage.show();
	}
	
	private void updateAppointment(int index, List<Appointment> appointments) {
        Appointment selectedAppointment = appointments.get(index - 1); // Adjusting index as it starts from 1
        VBox updateFields = new VBox(10);
        updateFields.setPadding(new Insets(20));

        TextField durationField = new TextField(String.valueOf(selectedAppointment.getDuration()));
        TextField timeField = new TextField(selectedAppointment.getTime());
        DatePicker datePicker = new DatePicker();
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
        	
        	LocalDate localDate = datePicker.getValue();
        	String dateString = localDate.toString();
        	// Convert String to Date
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException ex) {
            	showErrorAlert("Invalid date format. It should be yyyy-MM-dd.");
            }

			// Validate input
			if (durationField.getText().isEmpty() || date == null || timeField.getText().isEmpty()) {
				showErrorAlert("Please fill in all fields.");
				return;
			}
        	
        	try {
				// Parse duration and time
				int duration = Integer.parseInt(durationField.getText());
				LocalTime time = LocalTime.parse(timeField.getText());
				
	            // Update the appointment with new values
	            selectedAppointment.setDate(date);
	            selectedAppointment.setTime(time.toString());
	            selectedAppointment.setDuration(duration);
	            
	            showAppointments(appointments);
				
			} catch (NumberFormatException ex) {
				showErrorAlert("Invalid duration.");
			} catch (DateTimeParseException ex) {
				showErrorAlert("Invalid time format. Please use HH:mm.");
			}
        	
        });

        updateFields.getChildren().addAll(new Label("Date:"), datePicker,
                                           new Label("Time:"), timeField,
                                           new Label("Duration:"), durationField,
                                           saveButton);

        Scene updateScene = new Scene(updateFields, 300, 300);
        Stage updateStage = new Stage();
        updateStage.setScene(updateScene);
        updateStage.setTitle("Update Appointment");
        updateStage.show();
    }
	
	
	public void showAvailableServices(List<Service> availableServices) {
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));

		Hyperlink homePage = new Hyperlink("Back to Home");
		homePage.setOnAction(e -> {
        	mainController.showCustomerInterfaceAfterLogin();
        	stage.close();
        });

		Label titleLabel = new Label("Available Services:");
		root.getChildren().add(titleLabel);
		
		for (Service service : availableServices) {
			// Display each service as a button
			Button serviceButton = new Button(service.getName());
			serviceButton.setOnAction(e -> requestQuote(service)); // On button click, request a quote for the service
			root.getChildren().add(serviceButton);
		}

		root.getChildren().add(homePage);
		
		Scene scene = new Scene(root, 300, 200);
		stage.setScene(scene);
		stage.show();
	}

	public void requestQuote(Service service) {
		VBox root = new VBox(10);
		root.setPadding(new Insets(20));

		Hyperlink homePage = new Hyperlink("Back to Home");
		homePage.setOnAction(e -> {
        	mainController.showCustomerInterfaceAfterLogin();
        	stage.close();
        });
        
        root.getChildren().add(homePage);
		
		// Input fields for duration, date, and time
		TextField durationField = new TextField();
		durationField.setPromptText("Duration (in minutes)");

		DatePicker datePicker = new DatePicker();
		datePicker.setPromptText("Select Date");

		TextField timeField = new TextField();
		timeField.setPromptText("Time (HH:mm)");

		Button submitButton = new Button("Request Quote");
		submitButton.setOnAction(e -> {
			// Get input values
			String durationStr = durationField.getText();
			LocalDate localDate = datePicker.getValue();
			String timeStr = timeField.getText();
			
            String dateString = localDate.toString();

            // Convert String to Date
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException ex) {
                ex.printStackTrace(); // Handle parse exception
            }

			// Validate input
			if (durationStr.isEmpty() || date == null || timeStr.isEmpty()) {
				showErrorAlert("Please fill in all fields.");
				return;
			}

			try {
				// Parse duration and time
				int duration = Integer.parseInt(durationStr);
				LocalTime time = LocalTime.parse(timeStr);

				// Create appointment with empty StaffID as quote
				mainController.requestQuote(service, duration, date, time);

				showAvailableServices(mainController.getAvailableServices());
				
			} catch (NumberFormatException ex) {
				showErrorAlert("Invalid duration.");
			} catch (DateTimeParseException ex) {
				showErrorAlert("Invalid time format. Please use HH:mm.");
			}
		});

		root.getChildren().addAll(
				new Label("Duration (in minutes):"), durationField,
				new Label("Date:"), datePicker,
				new Label("Time (HH:mm):"), timeField,
				submitButton
				);

		Scene scene = new Scene(root, 420, 300);
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
