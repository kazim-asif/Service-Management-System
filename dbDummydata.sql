-- Insert dummy data into Staff table
INSERT INTO Staff (firstName, lastName, userName, password, email, phoneNumber, role) VALUES
('John', 'Doe', 'johndoe', 'password123', 'john.doe@example.com', '1234567890', 'Manager'),
('Jane', 'Smith', 'janesmith', 'password456', 'jane.smith@example.com', '9876543210', 'Staff');

-- Insert dummy data into Customer table
INSERT INTO Customer (firstName, lastName, userName, password, address, email, phoneNumber, paymentDetails) VALUES
('Alice', 'Johnson', 'alicej', 'abc123', '123 Main St, Cityville', 'alice.johnson@example.com', '555-1234', 'Credit Card: XXXX-XXXX-XXXX-1234'),
('Bob', 'Williams', 'bobw', 'def456', '456 Oak Ave, Townsville', 'bob.williams@example.com', '555-5678', 'PayPal: bob@example.com');

-- Insert dummy data into Services table
INSERT INTO Services (name, description, cost) VALUES
('Cleaning', 'Basic cleaning service', 50.00),
('Repairs', 'Repair services for various equipment', 100.00),
('Electrician', 'Repair minor electrical issues', 75.00);

-- Insert dummy data into Booking table
INSERT INTO Booking (duration, date, time, StaffstaffID, ServicesserviceID, CustomercustomerID) VALUES
(2, '2024-03-20', '10:00:00', 1, 1, 1),
(1, '2024-03-22', '14:00:00', 2, 2, 2),
(3, '2024-03-25', '11:00:00', 1, 3, 2);

-- Insert dummy data into Customer_Services table
INSERT INTO Customer_Services (CustomercustomerID, ServicesserviceID) VALUES
(1, 1),
(1, 2),
(2, 3);
