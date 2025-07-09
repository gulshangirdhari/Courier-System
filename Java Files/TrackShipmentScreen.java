import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TrackShipmentScreen extends JFrame {
    private JTextField trackingIdField;
    private JTextArea resultArea;
    private String customerEmail;

    public TrackShipmentScreen(String email) {
        // Validate that email is not null or empty
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid user email.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        this.customerEmail = email;
        setTitle("Track Shipment");

        // Set the screen to full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Title Label
        JLabel titleLabel = new JLabel("Track Shipment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBounds((screenSize.width - 500) / 2, 30, 500, 40);
        add(titleLabel);

        // Label and field for tracking ID input
        JLabel trackingIdLabel = new JLabel("Enter Tracking ID:");
        trackingIdLabel.setBounds(300, 100, 200, 30);
        trackingIdLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(trackingIdLabel);

        trackingIdField = new JTextField();
        trackingIdField.setBounds(520, 100, 250, 30);
        add(trackingIdField);

        // Track Button
        JButton trackButton = new JButton("Track");
        trackButton.setBounds((screenSize.width - 150) / 2, 150, 150, 40);
        trackButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        trackButton.setBackground(new Color(0, 153, 76));
        trackButton.setForeground(Color.WHITE);
        add(trackButton);

        // Result area where shipment and tracking info will be shown
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(200, 220, screenSize.width - 400, screenSize.height - 350);
        add(scrollPane);

        // Track button action
        trackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trackShipment();
            }
        });

        // Back button to return to Customer Dashboard
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerDashboardScreen(customerEmail);
            }
        });

        setVisible(true);
    }

    private void trackShipment() {
        String trackingId = trackingIdField.getText().trim();

        // Make sure user entered a tracking ID
        if (trackingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a tracking ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Connect to MySQL
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "root");

            // First, get customer ID using the email
            PreparedStatement customerStmt = con.prepareStatement("SELECT customer_id FROM customers WHERE email = ?");
            customerStmt.setString(1, customerEmail);
            ResultSet customerRs = customerStmt.executeQuery();

            if (!customerRs.next()) {
                JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int customerId = customerRs.getInt("customer_id");

            // Now check if this tracking ID belongs to this customer
            PreparedStatement pst1 = con.prepareStatement("SELECT * FROM shipments WHERE tracking_id = ? AND customer_id = ?");
            pst1.setString(1, trackingId);
            pst1.setInt(2, customerId);
            ResultSet rs1 = pst1.executeQuery();

            StringBuilder result = new StringBuilder();

            if (rs1.next()) {
                // Show basic shipment details
                result.append("--- Shipment Details ---\n");
                result.append("Receiver: ").append(rs1.getString("receiver_name")).append("\n");
                result.append("Address: ").append(rs1.getString("receiver_address")).append("\n");
                result.append("Destination: ").append(rs1.getString("destination")).append("\n");
                result.append("Weight: ").append(rs1.getDouble("weight")).append(" kg\n");
                result.append("Type: ").append(rs1.getString("shipment_type")).append("\n");
                result.append("Status: ").append(rs1.getString("status")).append("\n");
                result.append("Shipment Date: ").append(rs1.getTimestamp("shipment_date")).append("\n\n");

                // Show tracking history for this shipment
                result.append("--- Tracking History ---\n");
                PreparedStatement pst2 = con.prepareStatement("SELECT * FROM tracking_history WHERE tracking_id = ? ORDER BY updated_at DESC");
                pst2.setString(1, trackingId);
                ResultSet rs2 = pst2.executeQuery();

                while (rs2.next()) {
                    result.append("[ ").append(rs2.getTimestamp("updated_at")).append(" ] ");
                    result.append(rs2.getString("location")).append(" - ");
                    result.append(rs2.getString("status"));
                    String note = rs2.getString("notes");
                    if (note != null && !note.isEmpty()) {
                        result.append(" (Note: ").append(note).append(")");
                    }
                    result.append("\n");
                }
            } else {
                // No record found
                result.append("No shipment found for this tracking ID \nor it does not belong to you.");
            }

            // Show result in text area
            resultArea.setText(result.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving shipment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
