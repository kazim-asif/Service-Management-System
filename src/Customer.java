
public class Customer extends User {
	private int customerID;
	private String address;
	private String paymentDetails;
	
	public Customer() {
		
	}
	public Customer(String firstName, String lastName, String username, String password, String email,
			String phoneNumber, String address, String paymentDetails) {
		super(firstName, lastName, username, password, email, phoneNumber);
		this.address = address;
		this.paymentDetails = paymentDetails;
	}

	
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
}
