import javax.swing.*; // For GUI components like JFrame, JLabel, JButton, etc.
import java.awt.*; // For screen size, layout, fonts, and colors
import java.awt.event.*; // For handling button clicks
import java.sql.*; // For database connectivity (JDBC)
import javax.swing.table.DefaultTableModel; // For creating a table model to manage data in JTable

// Class to display all bookings of a logged-in customer
public class ViewBookingsScreen extends JFrame {
    private String customerEmail; // Stores the email of the logged-in customer
    private JTable bookingsTable; // Table component to show bookings
    private DefaultTableModel tableModel; // Model used by JTable to manage its data

    // Constructor takes customer's email to load their bookings
    public ViewBookingsScreen(String email) {
        // Validate: email should not be null or empty
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid user email.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Close the screen
            return; // Stop execution if email is invalid
        }

        this.customerEmail = email; // Save email for later use
        setTitle("View My Bookings"); // Set window title

        // Get screen size and set full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Get full screen size
        setSize(screenSize); // Set the window size to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setLayout(null); // Using manual positioning instead of layout managers
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit app when window is closed

        // Heading at top
        JLabel titleLabel = new JLabel("Your Bookings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36)); // Large bold font
        titleLabel.setForeground(new Color(0, 102, 204)); // Blue text color
        titleLabel.setBounds((screenSize.width - 500) / 2, 30, 500, 40); // Centered horizontally
        add(titleLabel);

        // Show currently logged-in user’s email
        JLabel emailLabel = new JLabel("Logged in as: " + customerEmail, SwingConstants.CENTER);
        emailLabel.setFont(new Font("Serif", Font.PLAIN, 18)); // Medium font
        emailLabel.setBounds((screenSize.width - 500) / 2, 80, 500, 30);
        add(emailLabel);

        // Columns to be shown in the table
        String[] columnNames = {
            "Tracking ID",
            "Receiver Name",
            "Destination",
            "Weight (kg)",
            "Shipment Type",
            "Status",
            "Shipment Date"
        };

        // Create table model with no rows but with column names
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create JTable and attach the model
        bookingsTable = new JTable(tableModel);
        bookingsTable.setFont(new Font("Serif", Font.PLAIN, 16)); // Table font
        bookingsTable.setRowHeight(25); // Each row height
        bookingsTable.setEnabled(false); // Make table read-only (non-editable)

        // Add table to scroll pane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBounds(100, 130, screenSize.width - 200, screenSize.height - 300);
        add(scrollPane);

        // Back button to go back to Customer Dashboard
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0)); // Red color
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // What happens when Back button is clicked
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dispose(); // Close this screen
                new CustomerDashboardScreen(customerEmail); // Go back to dashboard
            }
        });

        // Load bookings from the database into the table
        loadBookings();

        setVisible(true); // Show the window
    }

    // This method connects to the DB and shows customer’s bookings
    private void loadBookings() {
        // Connection info for your MySQL database
        String url = "jdbc:mysql://localhost:3306/courier_db";
        String user = "root";
        String password = "root";

        try {
            // Connect to MySQL database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Step 1: Get customer_id using email
            PreparedStatement customerStmt = conn.prepareStatement("SELECT customer_id FROM customers WHERE email = ?");
            customerStmt.setString(1, customerEmail); // Set the email in query
            ResultSet customerRs = customerStmt.executeQuery(); // Run the query

            // If no such customer found
            if (!customerRs.next()) {
                JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose(); // Close screen
                return;
            }

            int customerId = customerRs.getInt("customer_id"); // Extract the customer_id

            // Step 2: Get all shipments for this customer using their customer_id
            PreparedStatement shipmentStmt = conn.prepareStatement(
                "SELECT tracking_id, receiver_name, destination, weight, shipment_type, status, shipment_date " +
                "FROM shipments WHERE customer_id = ? ORDER BY shipment_date DESC"
            );
            shipmentStmt.setInt(1, customerId); // Bind customer_id to query
            ResultSet shipmentRs = shipmentStmt.executeQuery(); // Run query

            tableModel.setRowCount(0); // Clear table before filling new rows

            // Step 3: Add each booking row to the table
            while (shipmentRs.next()) {
                Object[] row = {
                    shipmentRs.getString("tracking_id"),
                    shipmentRs.getString("receiver_name"),
                    shipmentRs.getString("destination"),
                    shipmentRs.getDouble("weight"),
                    shipmentRs.getString("shipment_type"),
                    shipmentRs.getString("status"),
                    shipmentRs.getTimestamp("shipment_date")
                };
                tableModel.addRow(row); // Add row to the table
            }

            // If no bookings found for this customer
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No bookings found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            // If there's any issue in database operations
            ex.printStackTrace(); // Print error for debugging
            JOptionPane.showMessageDialog(this, "Error retrieving bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Close screen
        }
    }
}
