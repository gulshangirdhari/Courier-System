import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerDashboardScreen extends JFrame {
    private String customerEmail;

    public CustomerDashboardScreen(String email) {
        this.customerEmail = email;

        setTitle("Customer Dashboard");

        // Set the screen to full size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title label on top
        JLabel title = new JLabel("Welcome to Customer Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 204)); // blue color
        title.setBounds((screenSize.width - 600) / 2, 60, 600, 50);
        add(title);

        // Show email of the customer who is logged in
        JLabel emailLabel = new JLabel("Logged in as: " + customerEmail, SwingConstants.CENTER);
        emailLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        emailLabel.setForeground(Color.DARK_GRAY);
        emailLabel.setBounds((screenSize.width - 400) / 2, 130, 400, 30);
        add(emailLabel);

        // Button for tracking shipment
        JButton trackShipmentBtn = new JButton("Track Shipment");
        trackShipmentBtn.setBounds((screenSize.width - 300) / 2, 200, 300, 50);
        trackShipmentBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        trackShipmentBtn.setBackground(new Color(0, 153, 76)); // green
        trackShipmentBtn.setForeground(Color.WHITE);
        add(trackShipmentBtn);

        // Button to view customer bookings
        JButton viewBookingsBtn = new JButton("View My Bookings");
        viewBookingsBtn.setBounds((screenSize.width - 300) / 2, 270, 300, 50);
        viewBookingsBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        viewBookingsBtn.setBackground(new Color(0, 153, 76)); // green
        viewBookingsBtn.setForeground(Color.WHITE);
        add(viewBookingsBtn);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds((screenSize.width - 300) / 2, 340, 300, 50);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoutBtn.setBackground(new Color(255, 102, 102)); // light red
        logoutBtn.setForeground(Color.WHITE);
        add(logoutBtn);

        // Back button at the bottom left
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0)); // red
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // Action for Track Shipment button
        trackShipmentBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close current screen
                new TrackShipmentScreen(customerEmail); // go to tracking screen
            }
        });

        // Action for View My Bookings button
        viewBookingsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close current screen
                new ViewBookingsScreen(customerEmail); // go to booking screen
            }
        });

        // Action for Logout button
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close dashboard
                new WelcomeScreen(); // go to welcome screen
            }
        });

        // Action for Back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // go back to login screen
                new ExistingCustomerLoginScreen();
            }
        });

        setVisible(true); // finally show everything
    }
}
