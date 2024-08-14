import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainController {

    private ManagementSystem managementSystem;
    private CustomerGUI customerGUI;
    private StaffGUI staffGUI;
    private String loggedInUsername;
    
    public MainController(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
    }
    
    public void setUsername(String un) {
    	loggedInUsername=un;
    }
    
    // user management
    public void showLogin() {
    	// Create login screen
        LoginScreen loginScreen = new LoginScreen(this);
        loginScreen.display();
    }
    
    public void showRegister() {
    	// Create login screen
        RegisterScreen registerScreen = new RegisterScreen(this);
        registerScreen.display();
    }
    
    public String login(String username, String password, String usertype) {
		return managementSystem.login(username, password, usertype);
	}

    public boolean registerCustomer(String fn, String ln, String email, String phone, String username, String password,
			String address, String payment) {
		return managementSystem.registerCustomer(fn, ln, email, phone, username, password, address, payment);
	}
    
    public boolean registerStaff(String fn, String ln, String email, String phone, String username, String password,
			String role) {
    	return managementSystem.registerStaff(fn, ln, email, phone, username, password, role);
	}
    
	// initial screens for users after login
    public void showCustomerInterfaceAfterLogin() {
        customerGUI = new CustomerGUI(this);
        customerGUI.showHomeScreen();
    }

    public void showStaffInterfaceAfterLogin() {
        staffGUI = new StaffGUI(this);
        staffGUI.showHomeScreen();
    }

	public void addService(String name, String description, double cost) {
		managementSystem.addService(name, description, cost);
	}
	
	public List<Service> getAvailableServices() {
        // return available services from ManagementSystem
        return managementSystem.browseServices();
    }
	
	public void requestQuote(Service service, int duration, Date date, LocalTime time) {
        // Pass the quote to the ManagementSystem
        managementSystem.requestQuote(date, time, service, duration, loggedInUsername);
    }

    public List<Appointment> showRequestedQuotes() {
        // Get requested quotes from ManagementSystem and pass them to the StaffGUI
        return managementSystem.getRequestedQuotes();
    }
    
    public ArrayList<User> getStaff() {
    	return managementSystem.getUserByType("Staff");
    }

    public void acceptQuoteAndCreaeAppointment(Appointment quote, String staffMember) {
        managementSystem.acceptAndCreateAppointment(quote, staffMember);
    }

    public void rejectQuote(Appointment quote) {
        managementSystem.rejectQuote(quote);
    }

	public List<Appointment> showAppointments() {
		return managementSystem.getCustomerAppointments(loggedInUsername);
	}

	public HashMap<String, Double> generateRevenueReport(Date startDate, Date endDate) {
		
		return managementSystem.generateRevenueReport(startDate, endDate);
	}

	public void EndSession() {
		managementSystem.EndSession();
	}
    
}
