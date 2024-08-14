-- Use the servicemanagementsystem schema
USE servicemanagementsystem;

-- Create Staff table
CREATE TABLE Staff (
    staffID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    userName VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    email VARCHAR(100),
    phoneNumber VARCHAR(20),
    role VARCHAR(50)
);

-- Create Customer table
CREATE TABLE Customer (
    customerID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    userName VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    address VARCHAR(255),
    email VARCHAR(100),
    phoneNumber VARCHAR(20),
    paymentDetails VARCHAR(255)
);

-- Create Services table
CREATE TABLE Services (
    serviceID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    cost DECIMAL(10,2)
);

-- Create Booking table
CREATE TABLE Booking (
    code INT AUTO_INCREMENT PRIMARY KEY,
    duration INT,
    date DATE,
    time TIME,
    StaffstaffID INT,
    ServicesserviceID INT,
    CustomercustomerID INT,
    FOREIGN KEY (StaffstaffID) REFERENCES Staff(staffID),
    FOREIGN KEY (ServicesserviceID) REFERENCES Services(serviceID),
    FOREIGN KEY (CustomercustomerID) REFERENCES Customer(customerID)
);

-- Create Customer_Services table (Many-to-Many Relationship)
CREATE TABLE Customer_Services (
    CustomercustomerID INT,
    ServicesserviceID INT,
    PRIMARY KEY (CustomercustomerID, ServicesserviceID),
    FOREIGN KEY (CustomercustomerID) REFERENCES Customer(customerID),
    FOREIGN KEY (ServicesserviceID) REFERENCES Services(serviceID)
);
