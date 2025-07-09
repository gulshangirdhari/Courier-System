import javax.swing.*;       // for creating GUI components
import java.awt.*;          // for colors, fonts, screen size, etc
import java.awt.event.*;    // for handling button click actions

public class UserSelectionScreen extends JFrame {

    public UserSelectionScreen() {
        // Get screen size and set full-screen window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // also makes it full screen
        setLayout(null);  // using manual layout
        setDefaultCloseOperation(EXIT_ON_CLOSE); // close app on X button
        getContentPane().setBackground(new Color(245, 245, 245)); // light gray background

        // Main heading text
        JLabel title = new JLabel("Select Your Role", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));          // set font and size
        title.setForeground(new Color(0, 102, 204));              // blue color text
        title.setBounds((screenSize.width - 500) / 2, 100, 500, 50); // center it on screen
        add(title);  // add heading to screen

        // Button for Employee Login
        JButton employeeButton = new JButton("Employee Login");
        employeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        employeeButton.setBackground(new Color(0, 153, 76));  // green background
        employeeButton.setForeground(Color.WHITE);            // white text
        employeeButton.setBounds((screenSize.width - 300) / 2, 200, 300, 50);
        add(employeeButton);

        // Button for Existing Customer Login
        JButton existingCustomerButton = new JButton("Existing Customer Login");
        existingCustomerButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        existingCustomerButton.setBackground(new Color(0, 153, 76));
        existingCustomerButton.setForeground(Color.WHITE);
        existingCustomerButton.setBounds((screenSize.width - 300) / 2, 280, 300, 50);
        add(existingCustomerButton);

        // Button for New Customer Registration
        JButton newCustomerButton = new JButton("New Customer Registration");
        newCustomerButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        newCustomerButton.setBackground(new Color(0, 153, 76));
        newCustomerButton.setForeground(Color.WHITE);
        newCustomerButton.setBounds((screenSize.width - 300) / 2, 360, 300, 50);
        add(newCustomerButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0)); // red background
        backButton.setForeground(Color.WHITE);          // white text
        backButton.setBounds(20, screenSize.height - 120, 120, 40); // bottom-left side
        add(backButton);

        // When employee button is clicked
        employeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();                     // close current screen
                new EmployeeLoginScreen();     // open employee login screen
            }
        });

        // When existing customer button is clicked
        existingCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();                         // close current screen
                new ExistingCustomerLoginScreen(); // open customer login screen
            }
        });

        // When new customer button is clicked
        newCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();                         // close current screen
                new CustomerRegistrationScreen();  // open registration screen
            }
        });

        // When back button is clicked
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();               // close this screen
                new WelcomeScreen();     // go back to welcome screen
            }
        });

        setVisible(true); // show the screen
    }
}
