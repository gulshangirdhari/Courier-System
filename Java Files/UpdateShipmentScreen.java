import javax.swing.*;       // for GUI components
import java.awt.*;          // for layout, font, color
import java.awt.event.*;    // for button click actions
import java.sql.*;          // for database operations

public class UpdateShipmentScreen extends JFrame {

    public UpdateShipmentScreen() {
        setTitle("Update Shipment Status");  // window title

        // Set full screen window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null); // manual positioning
        setDefaultCloseOperation(EXIT_ON_CLOSE); // close app when user clicks close
        getContentPane().setBackground(new Color(245, 245, 245)); // light background

        // Page title
        JLabel title = new JLabel("Update Shipment Status", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 204));
        title.setBounds((screenSize.width - 500) / 2, 50, 500, 50);
        add(title);

        // Font for all labels
        Font labelFont = new Font("SansSerif", Font.BOLD, 18);

        // Tracking ID input
        JLabel trackingLabel = new JLabel("Tracking ID:");
        trackingLabel.setBounds(400, 150, 150, 30);
        trackingLabel.setFont(labelFont);
        add(trackingLabel);

        JTextField trackingField = new JTextField();
        trackingField.setBounds(560, 150, 210, 30);
        add(trackingField);

        // Status input
        JLabel statusLabel = new JLabel("New Status:");
        statusLabel.setBounds(400, 200, 150, 30);
        statusLabel.setFont(labelFont);
        add(statusLabel);

        JTextField statusField = new JTextField();
        statusField.setBounds(560, 200, 210, 30);
        add(statusField);

        // Location input
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(400, 250, 150, 30);
        locationLabel.setFont(labelFont);
        add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBounds(560, 250, 210, 30);
        add(locationField);

        // Notes input (optional)
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(400, 300, 150, 30);
        notesLabel.setFont(labelFont);
        add(notesLabel);

        JTextArea notesArea = new JTextArea();
        notesArea.setBounds(560, 300, 210, 60);
        notesArea.setLineWrap(true);          // wrap long lines
        notesArea.setWrapStyleWord(true);     // wrap at words, not characters
        add(notesArea);

        // Update Button to save new status
        JButton updateButton = new JButton("Update");
        updateButton.setBounds((screenSize.width - 180) / 2, 400, 180, 40);
        updateButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        updateButton.setBackground(new Color(0, 153, 76));
        updateButton.setForeground(Color.WHITE);
        add(updateButton);

        // Back button to go to dashboard
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // Update Button logic
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trackingId = trackingField.getText().trim();
                String newStatus = statusField.getText().trim();
                String location = locationField.getText().trim();
                String notes = notesArea.getText().trim();

                // Check if all required fields are filled
                if (trackingId.isEmpty() || newStatus.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
                } else {
                    try (
                        // Connect to MySQL
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "root")
                    ) {
                        // Update main shipment status
                        PreparedStatement pst1 = con.prepareStatement(
                            "UPDATE shipments SET status = ? WHERE tracking_id = ?"
                        );
                        pst1.setString(1, newStatus);
                        pst1.setString(2, trackingId);

                        int rowsAffected = pst1.executeUpdate();

                        if (rowsAffected > 0) {
                            // Also insert a new record into tracking history
                            PreparedStatement pst2 = con.prepareStatement(
                                "INSERT INTO tracking_history (tracking_id, location, status, notes) VALUES (?, ?, ?, ?)"
                            );
                            pst2.setString(1, trackingId);
                            pst2.setString(2, location);
                            pst2.setString(3, newStatus);
                            pst2.setString(4, notes);
                            pst2.executeUpdate();

                            JOptionPane.showMessageDialog(null, "Shipment status updated successfully.");

                            // Clear input fields
                            trackingField.setText("");
                            statusField.setText("");
                            locationField.setText("");
                            notesArea.setText("");
                        } else {
                            JOptionPane.showMessageDialog(null, "Tracking ID not found.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(); // show error in console
                        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                    }
                }
            }
        });

        // Back button logic
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close current screen
                new EmployeeDashboardScreen(); // open dashboard
            }
        });

        setVisible(true); // show the window
    }
}
