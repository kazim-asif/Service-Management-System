import java.util.Date;

// Appointment class representing scheduled appointments
public class Appointment {
	private int code;
    private Date date;
    private String time;
    private int duration;
    private String staffMember;
    private Service service;
    private String customer;

    public Appointment() {
    	
    }
    
    public Appointment(Date date, String time, String staffMember, Service service, String customer, int duration) {
        this.date = date;
        this.time = time;
        this.staffMember = staffMember;
        this.service = service;
        this.customer=customer;
        this.duration=duration;
    }
    
    // Getters
    
    public Date getDate() {
        return date;
    }

    public int getCode() {
		return code;
	}

	public String getTime() {
        return time;
    }

    public String getStaffMember() {
        return staffMember;
    }

    public Service getService() {
        return service;
    }
    
    public String getCustomer() {
        return customer;
    }

	public int getDuration() {
		return duration;
	}

	//setters
	public void setCode(int code) {
		this.code = code;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setStaffMember(String staffMember) {
		this.staffMember = staffMember;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
    
}