ThariTrack - Courier Tracking System

This is a Java and MySQL-based Courier Tracking System. It allows employees to book and update shipments, and customers to track and view their bookings.

Features

- Full GUI made with Java Swing
- Welcome screen with animation
- Role selection (Employee / Customer)
- Employee login and dashboard
- Book shipment for existing customers
- Update shipment status and history
- Customer registration and login
- Track shipment with tracking history
- View all bookings in table format

Technologies Used

- Java (Swing)
- MySQL
- JDBC (Database connection)
- Visual studio code IDE
- Vector, JTable, JScrollPane, Timer (advanced Swing features)

Project Structure

- .java files (all screens and logic)
- courier_db.sql (database script)
- README.md (this file)

How to Run

1. Import the SQL file into MySQL.
2. Open project in your Java IDE.
3. Make sure database credentials match (username = root, password = root).
4. Run WelcomeScreen.java to start.

Notes

- Tracking ID is auto-generated.
- All GUI screens are fullscreen with manual layout using setBounds().
- JDBC is used for database operations like SELECT, INSERT, UPDATE.
