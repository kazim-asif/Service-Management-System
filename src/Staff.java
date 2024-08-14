// Staff class implementing the User interface
public class Staff extends User {
    
	private int staffID;
    private String role;
    
    public Staff() {
    }
	public Staff(String firstName, String lastName, String username, String password, String email, String phoneNumber,
			String role) {
		super(firstName, lastName, username, password, email, phoneNumber);
		this.role = role;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
    
    
}