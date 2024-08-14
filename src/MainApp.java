import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
    	// Initialize ManagementSystem
        ManagementSystem managementSystem = new ManagementSystem();

        // Create main GUI controller
        MainController mainController = new MainController(managementSystem);
        

        mainController.showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
