import javax.swing.*;       // for GUI components like JFrame, JLabel, JButton, etc.
import java.awt.*;          // for things like colors, fonts, screen size
import java.awt.event.*;    // for handling button clicks

public class EmployeeLoginScreen extends JFrame {
    public EmployeeLoginScreen() {
        // Get the full screen size of computer
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);                        // set window size to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);    // make sure it opens maximized
        setLayout(null);                            // we are placing elements manually
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // close app when window is closed
        getContentPane().setBackground(new Color(245, 245, 245)); // set light gray background

        // Heading text at top
        JLabel title = new JLabel("Employee Login", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));           // font style and size
        title.setForeground(new Color(0, 102, 204));               // blue color text
        title.setBounds((screenSize.width - 400) / 2, 100, 400, 50); // center it
        add(title);

        // Label for username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        userLabel.setBounds((screenSize.width - 300) / 2, 200, 100, 30);
        add(userLabel);

        // Text field for entering username
        JTextField usernameField = new JTextField();
        usernameField.setBounds((screenSize.width - 300) / 2 + 110, 200, 200, 30);
        add(usernameField);

        // Label for password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passLabel.setBounds((screenSize.width - 300) / 2, 260, 100, 30);
        add(passLabel);

        // Password field for entering password (hides the text)
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds((screenSize.width - 300) / 2 + 110, 260, 200, 30);
        add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginButton.setBackground(new Color(0, 153, 76));   // green background
        loginButton.setForeground(Color.WHITE);             // white text
        loginButton.setBounds((screenSize.width - 100) / 2, 330, 100, 40);
        add(loginButton);

        // Back button to go back to previous screen
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0)); // red background
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        add(backButton);

        // What happens when Login button is clicked
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get the text entered by user
                String username = usernameField.getText().trim(); // remove spaces
                String password = new String(passwordField.getPassword()).trim(); // get password text

                // check if username and password are correct
                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Login successful!"); // show message
                    dispose(); // close login screen
                    new EmployeeDashboardScreen(); // open employee dashboard
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Try again."); // show error
                }
            }
        });

        // What happens when Back button is clicked
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // close this screen
                new UserSelectionScreen(); // go back to user selection screen
            }
        });

        setVisible(true); // show the window
    }
}
