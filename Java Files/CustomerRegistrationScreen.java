import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerRegistrationScreen extends JFrame {
    public CustomerRegistrationScreen() {
        setTitle("Customer Registration");

        // Full screen setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel title = new JLabel("New Customer Registration", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(0, 102, 204));
        title.setBounds((screenSize.width - 500) / 2, 50, 500, 40);
        add(title);

        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        int labelX = 400, fieldX = 600, y = 130, h = 30, gap = 50;

        // Name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(labelX, y, 180, h);
        nameLabel.setFont(labelFont);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(fieldX, y, 250, h);
        add(nameField);
        y += gap;

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(labelX, y, 180, h);
        emailLabel.setFont(labelFont);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(fieldX, y, 250, h);
        add(emailField);
        y += gap;

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(labelX, y, 180, h);
        phoneLabel.setFont(labelFont);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(fieldX, y, 250, h);
        add(phoneField);
        y += gap;

        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(labelX, y, 180, h);
        addressLabel.setFont(labelFont);
        add(addressLabel);

        JTextArea addressArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(addressArea);
        scrollPane.setBounds(fieldX, y, 250, 60);
        add(scrollPane);
        y += 70;

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(labelX, y, 180, h);
        passwordLabel.setFont(labelFont);
        add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(fieldX, y, 250, h);
        add(passwordField);
        y += gap;

        // Retype Password
        JLabel retypePassLabel = new JLabel("Retype Password:");
        retypePassLabel.setBounds(labelX, y, 180, h);
        retypePassLabel.setFont(labelFont);
        add(retypePassLabel);

        JPasswordField retypePasswordField = new JPasswordField();
        retypePasswordField.setBounds(fieldX, y, 250, h);
        add(retypePasswordField);
        y += gap;

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(fieldX, y, 120, 40);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(new Color(0, 153, 76));
        registerButton.setForeground(Color.WHITE);
        add(registerButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // Register Button Action
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get data from user
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressArea.getText();
                String password = new String(passwordField.getPassword());
                String retypePassword = new String(retypePasswordField.getPassword());

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()
                        || retypePassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!");
                    return;
                }

                // Check password length first
                if (password.length() < 7) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long.");
                    return;
                }

                // Check if passwords match
                if (!password.equals(retypePassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }

                // Database logic
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root",
                            "root");

                    // Try to insert user
                    PreparedStatement pst = con.prepareStatement(
                            "INSERT INTO customers(name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)");
                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, phone);
                    pst.setString(4, address);
                    pst.setString(5, password);

                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(null, "Registration successful!");
                        dispose();
                        new ExistingCustomerLoginScreen();
                    }

                    con.close();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    // Email already in use (unique constraint)
                    JOptionPane.showMessageDialog(null, "Email already registered.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserSelectionScreen();
            }
        });

        setVisible(true);
    }
}
