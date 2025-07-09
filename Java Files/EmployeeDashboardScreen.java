import javax.swing.*;       // for GUI components like JFrame, JLabel, JButton
import java.awt.*;          // for screen size, fonts, colors
import java.awt.event.*;    // for handling button clicks

public class EmployeeDashboardScreen extends JFrame {

    public EmployeeDashboardScreen() {
        setTitle("Employee Dashboard");  // title of the window

        // Set full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get screen size
        setSize(screenSize);                        // set window size
        setExtendedState(JFrame.MAXIMIZED_BOTH);    // maximize window
        setLayout(null);                            // manual positioning of components
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // exit app on closing
        getContentPane().setBackground(new Color(245, 245, 245)); // light gray background

        // Main title label
        JLabel title = new JLabel("Employee Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));         // big bold font
        title.setForeground(new Color(0, 102, 204));             // blue color text
        title.setBounds((screenSize.width - 500) / 2, 100, 500, 50); // center it
        add(title); // add to window

        // Button for booking a shipment
        JButton bookShipmentButton = new JButton("Book Shipment");
        bookShipmentButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        bookShipmentButton.setBackground(new Color(0, 153, 76)); // green background
        bookShipmentButton.setForeground(Color.WHITE);           // white text
        bookShipmentButton.setBounds((screenSize.width - 300) / 2, 220, 300, 50);
        add(bookShipmentButton);

        // Button for updating a shipment
        JButton updateShipmentButton = new JButton("Update Shipment");
        updateShipmentButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        updateShipmentButton.setBackground(new Color(0, 153, 76));
        updateShipmentButton.setForeground(Color.WHITE);
        updateShipmentButton.setBounds((screenSize.width - 300) / 2, 300, 300, 50);
        add(updateShipmentButton);

        // Back button to go to previous screen
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0)); // red background
        backButton.setForeground(Color.WHITE);          // white text
        backButton.setBounds(20, screenSize.height - 120, 120, 40); // bottom left
        add(backButton);

        // When Book Shipment button is clicked
        bookShipmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();               // close this screen
                new BookShipmentScreen(); // open booking screen
            }
        });

        // When Update Shipment button is clicked
        updateShipmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();                 // close this screen
                new UpdateShipmentScreen(); // open update screen
            }
        });

        // When Back button is clicked
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();                // close this screen
                new UserSelectionScreen(); // go back to role selection screen
            }
        });

        setVisible(true); // show the window
    }
}
