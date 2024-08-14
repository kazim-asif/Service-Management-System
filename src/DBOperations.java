import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DBOperations {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/servicemanagementsystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        return connection;
    }

    // Method to close the database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
 // Method to close ResultSet
    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to close Statement
    private static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
 // Method to retrieve all users (Staff and Customer) from the database
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            // Retrieve staff users
            resultSet = statement.executeQuery("SELECT * FROM Staff");
            while (resultSet.next()) {
                Staff staff = new Staff();
                staff.setStaffID(resultSet.getInt("staffID"));
                staff.setFirstName(resultSet.getString("firstName"));
                staff.setLastName(resultSet.getString("lastName"));
                staff.setUsername((resultSet.getString("userName")));
                staff.setPassword(resultSet.getString("password"));
                staff.setEmail(resultSet.getString("email"));
                staff.setPhoneNumber(resultSet.getString("phoneNumber"));
                staff.setRole(resultSet.getString("role"));
                userList.add(staff);
            }
            resultSet=null;
            // Retrieve customer users
            resultSet = statement.executeQuery("SELECT * FROM Customer");
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt("customerID"));
                customer.setFirstName(resultSet.getString("firstName"));
                customer.setLastName(resultSet.getString("lastName"));
                customer.setUsername(resultSet.getString("userName"));
                customer.setPassword(resultSet.getString("password"));
                customer.setAddress(resultSet.getString("address"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                customer.setPaymentDetails(resultSet.getString("paymentDetails"));
                userList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resultSet, statement, and connection
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }

        return userList;
    }

 // Method to retrieve all services from the database
    public static List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Services");

            while (resultSet.next()) {
                Service service = new Service();
                service.setServiceID(resultSet.getInt("serviceID"));
                service.setName(resultSet.getString("name"));
                service.setDescription(resultSet.getString("description"));
                service.setCost(resultSet.getDouble("cost"));
                serviceList.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resultSet, statement, and connection
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }

        return serviceList;
    }

 // Method to retrieve all appointments from the database
    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Booking " +
                    "JOIN Staff ON Booking.StaffstaffID = Staff.staffID " +
                    "JOIN Services ON Booking.ServicesserviceID = Services.serviceID " +
                    "JOIN Customer ON Booking.CustomercustomerID = Customer.customerID");

            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setCode(resultSet.getInt("code"));
                appointment.setDuration(resultSet.getInt("duration"));
                appointment.setDate(resultSet.getDate("date"));
                appointment.setTime(resultSet.getTime("time").toString()); // Convert Time to String
                appointment.setStaffMember(resultSet.getString("Staff.userName")); // Assuming username is stored in Staff table
                
                // Assuming Service object is created from Service table columns
                Service service = new Service();
                service.setServiceID(resultSet.getInt("serviceID"));
                service.setName(resultSet.getString("name"));
                service.setDescription(resultSet.getString("description"));
                service.setCost(resultSet.getDouble("cost"));
                appointment.setService(service);
                
                appointment.setCustomer(resultSet.getString("Customer.userName")); // Assuming username is stored in Customer table
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resultSet, statement, and connection
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }

        return appointmentList;
    }

    public static void storeData(List<User> users, List<Service> services, List<Appointment> appointments) {
    	storeUsers(users);
    	storeServices(services);
    	storeAppointments(appointments, users);
    }
    
 // Method to store users into the Staff and Customer tables
    public static void storeUsers(List<User> users) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            for (User user : users) {
                // Check if the user already exists in the database
                String checkQuery = "SELECT COUNT(*) FROM ";
                if (user instanceof Staff) {
                    checkQuery += "Staff";
                } else {
                    checkQuery += "Customer";
                }
                checkQuery += " WHERE userName = ?";
                
                preparedStatement = connection.prepareStatement(checkQuery);
                preparedStatement.setString(1, user.getUsername());
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count == 0) {
                    // User does not exist, so insert into the database
                    String insertQuery;
                    if (user instanceof Staff) {
                        insertQuery = "INSERT INTO Staff (firstName, lastName, userName, password, email, phoneNumber, role) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    } else {
                        insertQuery = "INSERT INTO Customer (firstName, lastName, userName, password, email, phoneNumber, address, paymentDetails) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    }
                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, user.getFirstName());
                    preparedStatement.setString(2, user.getLastName());
                    preparedStatement.setString(3, user.getUsername());
                    preparedStatement.setString(4, user.getPassword());
                    preparedStatement.setString(5, user.getEmail());
                    preparedStatement.setString(6, user.getPhoneNumber());

                    if (user instanceof Staff) {
                        preparedStatement.setString(7, ((Staff) user).getRole()); // Additional role field
                    } else {
                        preparedStatement.setString(7, ((Customer) user).getAddress()); // Additional address field
                        preparedStatement.setString(8, ((Customer) user).getPaymentDetails()); // Additional paymentDetails field
                    }
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resultSet, preparedStatement, and connection
            closeResultSet(resultSet);
            closePreparedStatement(preparedStatement);
            closeConnection(connection);
        }
    }
    
 // Method to store services into the Services table
   private static void storeServices(List<Service> services) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        connection = getConnection();
        Statement statement = connection.createStatement();
        for (Service service : services) {
            // Check if the service already exists in the database
            String checkQuery = "SELECT COUNT(*) FROM Services WHERE name = ?";
            preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setString(1, service.getName());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                // Service does not exist, so insert into the database
                String insertQuery = "INSERT INTO Services (name, description, cost) VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, service.getName());
                preparedStatement.setString(2, service.getDescription());
                preparedStatement.setDouble(3, service.getCost());
                preparedStatement.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resultSet, preparedStatement, and connection
        closeResultSet(resultSet);
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }
}

 // Method to store appointments into the Booking table
    private static void storeAppointments(List<Appointment> appointments, List<User> users) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    try {
        connection = getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM Booking");

        for (Appointment appointment : appointments) {
            // Check if the appointment code already exists in the database
            String checkQuery = "SELECT COUNT(*) FROM Booking WHERE code = ?";
            preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, appointment.getCode());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count == 0) {
                // Appointment code does not exist, so insert into the database
                // Get customer ID based on username
                int customerID = getCustomerID(appointment.getCustomer(), users);
                // Get staff ID based on username
                int staffID = getStaffID(appointment.getStaffMember(), users);

                String insertQuery = "INSERT INTO Booking (code, duration, date, time, StaffstaffID, ServicesserviceID, CustomercustomerID) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setInt(1, appointment.getCode());
                preparedStatement.setInt(2, appointment.getDuration());
                preparedStatement.setDate(3, new java.sql.Date(appointment.getDate().getTime()));
                
                // Parse the string representation of time into a LocalTime object
                LocalTime localTime = LocalTime.parse(appointment.getTime());
                // Convert LocalTime to java.sql.Time
                java.sql.Time sqlTime = java.sql.Time.valueOf(localTime);
                
                preparedStatement.setTime(4, sqlTime);
                preparedStatement.setInt(5, staffID);
                preparedStatement.setInt(6, appointment.getService().getServiceID());
                preparedStatement.setInt(7, customerID);
                preparedStatement.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resultSet, preparedStatement, and connection
        closeResultSet(resultSet);
        closePreparedStatement(preparedStatement);
        closeConnection(connection);
    }
}

    private static int getCustomerID(String customer, List<User> users) {
		for(User u : users) {
			if(u instanceof Customer) {
				Customer c = (Customer) u;
				if(c.getUsername().equals(customer)) {
					return c.getCustomerID();
				}
			}
		}
		return -1;
	}
    
    private static int getStaffID(String staffID, List<User> users) {
    	for(User u : users) {
			if(u instanceof Staff) {
				Staff s = (Staff) u;
				if(s.getUsername().equals(staffID)) {
					return s.getStaffID();
				}
			}
		}
		return -1;
	}

	// Method to close PreparedStatement
    private static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
