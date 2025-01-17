import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

// Management System to handle user, service, and appointment management
public class ManagementSystem {
    private List<User> users;
    private List<Service> services;
    private List<Appointment> appointments;
    private List<Appointment> quotes;
    
    public ManagementSystem() {
        this.users = new ArrayList<>();
        this.services = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.quotes = new ArrayList<>();
        LoadUsers();
        LoadServices();
        LoadAppointments();
    }
    
    private void LoadUsers() {
    	this.users = DBOperations.getAllUsers();
    }
    
    private void LoadServices() {
    	this.services = DBOperations.getAllServices();
    }
    
    private void LoadAppointments() {
    	this.appointments = DBOperations.getAllAppointments();
    }
    
    public void EndSession() {
    	DBOperations.storeData(users, services, appointments);
    }

	// User Management
    public boolean registerCustomer(String fn, String ln, String email, String phone, String username, String password,
			String address, String payment) {
    	if( isUsernameAvailable(username) ) {
    		Customer cust = new Customer(fn, fn, username, password, email, phone, address, payment);
    		cust.setCustomerID(users.size());
    		users.add(cust);
    		return true;
    	}
    	return false;
	}

	public boolean registerStaff(String fn, String ln, String email, String phone, String username, String password,
			String role) {
		if( isUsernameAvailable(username) ) {
			Staff staff = new Staff(fn, fn, username, password, email, phone, role);
			staff.setStaffID(users.size());
    		users.add(staff);
    		return true;
    	}
		return false;
	}
    
	//check login credentials
    public String login(String username, String password, String usertype) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) &&
            		user.getClass().toString().substring(6).equals(usertype)) {
                return user.getUsername();
            }
        }
        return null;
    }
	
    // Service Management
    public void addService(String name, String description, double cost) {
    	Service service = new Service(name, description, cost);
    	service.setServiceID(services.size());
        services.add(service);
    }

    public List<Service> browseServices() {
        return services;
    }

    // Appointment Management
    public void scheduleAppointment(Date date, String time, String staffMember, Service service, String customer, int duration) {
        appointments.add(new Appointment(date, time, staffMember, service, customer, duration));
    }

    public List<Appointment> viewAppointments() {
        return appointments;
    }
    
    // Reporting

    // Revenue generated by service type over a specific period
    public HashMap<String, Double> generateRevenueReport(Date startDate, Date endDate) {
    	HashMap<String, Double> revenueReport = new HashMap<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().after(startDate) && appointment.getDate().before(endDate)) {
                String serviceName = appointment.getService().getName();
                double cost = appointment.getService().getCost();
                revenueReport.put(serviceName, revenueReport.getOrDefault(serviceName, 0.0) + cost);
            }
        }
        return revenueReport;
    }

    // Workload distribution among staff members
    public HashMap<String, Integer> generateWorkloadReport() {
    	HashMap<String, Integer> workloadReport = new HashMap<>();
        for (Appointment appointment : appointments) {
            String staffMember = appointment.getStaffMember();
            workloadReport.put(staffMember, workloadReport.getOrDefault(staffMember, 0) + 1);
        }
        return workloadReport;
    }

    // Method to retrieve completed service requests and associated costs for a specific customer
    public List<Appointment> getCustomerServiceHistory(String customer) {
        List<Appointment> customerServiceHistory = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getCustomer().equals(customer)) {
                customerServiceHistory.add(appointment);
            }
        }
        return customerServiceHistory;
    }
    
    public boolean isUsernameAvailable(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
    
    // Method to get user by username
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername() == username) {
                return user;
            }
        }
        return null;
    }
    
 // Method to get user by user type
    public ArrayList<User> getUserByType(String type) {
    	ArrayList<User> usersbytype=new ArrayList<User>();
        for (int i=0; i<this.users.size(); i++) {
        	User user = this.users.get(i);
            if (user.getClass().toString().substring(6).equals(type)) {
            	usersbytype.add(user);
            }
        }
        return usersbytype;
    }

	
    public void requestQuote(Date date, LocalTime time, Service service, int duration, String username) {
    	// Create a new appointment with null staff ID
        Appointment quote = new Appointment();
        quote.setDate(date);
        quote.setTime(time.toString());
        quote.setStaffMember(null); // Staff ID is not assigned yet
        quote.setService(service);
        quote.setDuration(duration);
        quote.setCustomer(username);
        
        // Add the quote to the quotes list
        quotes.add(quote);
	}

	
    public List<Appointment> getRequestedQuotes() {
		return quotes;
	}
    
	public void acceptAndCreateAppointment(Appointment quote, String staffMember) {
		Appointment a = null;
		for(Appointment q: quotes) {
			if(q==quote) {
				a = new Appointment();
	            a.setDate(quote.getDate());
	            a.setTime(quote.getTime());
	            a.setDuration(quote.getDuration());
	            a.setStaffMember(staffMember);
	            a.setService(quote.getService());
	            a.setCustomer(q.getCustomer());
	            a.setCode(this.appointments.get(appointments.size()-1).getCode()+1);
				break;
			}
		}
		appointments.add(a);
		quotes.remove(quote);
	}

	public void rejectQuote(Appointment quote) {
		quotes.remove(quote);
	}
    
	public List<Appointment> getCustomerAppointments(String custUsername) {
		List<Appointment> allappoints = new ArrayList<>();
		for(Appointment a: appointments) {
			if(a.getCustomer().equals(custUsername)) {
				allappoints.add(a);
			}
		}
		return allappoints;
	}

}
