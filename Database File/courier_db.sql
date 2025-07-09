-- Create a new database
CREATE DATABASE courier_db;
USE courier_db;
drop database courier_db;
-- Table for Customers
CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    address VARCHAR(100),
    phone VARCHAR(15)
);

-- Table for Shipments
CREATE TABLE shipments (
    shipment_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    destination VARCHAR(100),
    weight DECIMAL(5,2),
    status VARCHAR(50),
    date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Table for Employees (for login)
CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);
select * from customers;