import javax.swing.*;
import java.awt.*;
import java.awt.event.*; // Needed for ActionListener
import java.sql.*;        // Needed for database connection

public class ExistingCustomerLoginScreen extends JFrame {
    public ExistingCustomerLoginScreen() {
        setTitle("Customer Login");

        // Set screen to full size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading Label
        JLabel title = new JLabel("Customer Login", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 204));
        title.setBounds((screenSize.width - 400) / 2, 100, 400, 50);
        add(title);

        // Email Label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds((screenSize.width - 500) / 2, 200, 100, 30);
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(emailLabel);

        // Email Input Field
        JTextField emailField = new JTextField();
        emailField.setBounds((screenSize.width - 300) / 2, 200, 200, 30);
        add(emailField);

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds((screenSize.width - 500) / 2, 260, 100, 30);
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(passLabel);

        // Password Field (JPasswordField hides input with dots)
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds((screenSize.width - 300) / 2, 260, 200, 30);
        add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds((screenSize.width - 100) / 2, 320, 100, 35);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        add(loginButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // Login button click action using traditional ActionListener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String email = emailField.getText().trim(); // trim() removes spaces from beginning/end
                String password = new String(passwordField.getPassword()); // getPassword() gives char[], we convert to String

                // Check if fields are empty
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both email and password.");
                    return;
                }

                // Declare connection variables
                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;

                try {
                    // Connect to database (JDBC URL, username, password)
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "root");

                    // Prepare SQL query to verify email and password
                    pst = con.prepareStatement("SELECT * FROM customers WHERE email = ? AND password = ?");
                    pst.setString(1, email);
                    pst.setString(2, password);

                    // Execute the query
                    rs = pst.executeQuery();

                    if (rs.next()) {
                        // If match found, login successful
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        dispose(); // Close current screen
                        new CustomerDashboardScreen(email); // Go to customer dashboard
                    } else {
                        // If no match found
                        JOptionPane.showMessageDialog(null, "Invalid email or password.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Show error in console
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                } finally {
                    // Always close database resources in finally block
                    try { if (rs != null) rs.close(); } catch (Exception ex) {}
                    try { if (pst != null) pst.close(); } catch (Exception ex) {}
                    try { if (con != null) con.close(); } catch (Exception ex) {}
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserSelectionScreen(); // Go back to user type selection
            }
        });

        setVisible(true); // Show the frame
    }
}
