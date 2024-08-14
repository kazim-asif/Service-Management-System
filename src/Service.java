
// Service class representing various services offered by the company
public class Service {
	private int serviceID;
    private String name;
    private String description;
    private double cost;

    public Service(){
    	
    }
    public Service(String name, String description, double cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	// Getters
	public int getServiceID() {
		return serviceID;
	}
	
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }
}