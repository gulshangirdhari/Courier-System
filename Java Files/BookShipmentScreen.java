import javax.swing.*;       // for GUI components
import java.awt.*;          // for colors, fonts, dimensions
import java.awt.event.*;    // for button and mouse events
import java.sql.*;          // for database connection
import java.util.Random;    // for generating tracking ID
import java.util.Vector;    // to store customer IDs matching search

public class BookShipmentScreen extends JFrame {
    private JTextField customerSearchField;
    private JList<String> suggestionsList;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;
    private int selectedCustomerId = -1; // to store the selected customer's ID
    private JTextField receiverNameField, receiverAddressField, destinationField, weightField;
    private JComboBox<String> typeDropdown;
    private Vector<Integer> customerIds = new Vector<>(); // stores matching customer IDs

    public BookShipmentScreen() {
        setTitle("Book Shipment");

        // Full screen setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245)); // light gray

        // Title label
        JLabel titleLabel = new JLabel("Book Shipment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBounds((screenSize.width - 500) / 2, 30, 500, 40);
        add(titleLabel);

        // Label and search field for finding customer by name or email
        JLabel customerLabel = new JLabel("Customer Name or Email:");
        customerLabel.setBounds(400, 100, 270, 30);
        customerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(customerLabel);

        customerSearchField = new JTextField();
        customerSearchField.setBounds(650, 100, 270, 30);
        add(customerSearchField);

        // List for showing matching suggestions
        listModel = new DefaultListModel<>();
        suggestionsList = new JList<>(listModel);
        suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(suggestionsList);
        scrollPane.setBounds(520, 130, 250, 100);
        scrollPane.setVisible(false); // hide initially
        add(scrollPane);

        // When user types in search box, update suggestions
        customerSearchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchSuggestions();
            }
        });

        // When user clicks on a suggestion
        suggestionsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int index = suggestionsList.getSelectedIndex();
                if (index != -1) {
                    selectedCustomerId = customerIds.get(index); // store selected customer ID
                    customerSearchField.setText(suggestionsList.getSelectedValue());
                    scrollPane.setVisible(false); // hide list after selection
                    JOptionPane.showMessageDialog(null, "Customer selected. You can now book shipment.");
                }
            }
        });

        // Create form fields
        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        receiverNameField = createFieldWithLabel("Receiver Name:", 200, labelFont);
        receiverAddressField = createFieldWithLabel("Receiver Address:", 250, labelFont);
        destinationField = createFieldWithLabel("Destination:", 300, labelFont);
        weightField = createFieldWithLabel("Weight (kg):", 350, labelFont);

        // Shipment type dropdown
        JLabel typeLabel = new JLabel("Shipment Type:");
        typeLabel.setBounds(400, 400, 150, 30);
        typeLabel.setFont(labelFont);
        add(typeLabel);

        typeDropdown = new JComboBox<>(new String[]{"Standard", "Express", "Same Day"});
        typeDropdown.setBounds(560, 400, 210, 30);
        add(typeDropdown);

        // Book shipment button
        JButton bookButton = new JButton("Book Shipment");
        bookButton.setBounds((screenSize.width - 180) / 2, 460, 180, 40);
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        bookButton.setBackground(new Color(0, 153, 76));
        bookButton.setForeground(Color.WHITE);
        add(bookButton);

        // When book button is clicked
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookShipment();
            }
        });

        // Back button to return to dashboard
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, screenSize.height - 120, 120, 40);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        add(backButton);

        // Go back to employee dashboard
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EmployeeDashboardScreen();
            }
        });

        setVisible(true);
    }

    // Reusable method to create a label and text field
    private JTextField createFieldWithLabel(String label, int yPos, Font font) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(400, yPos, 150, 30);
        jLabel.setFont(font);
        add(jLabel);

        JTextField textField = new JTextField();
        textField.setBounds(560, yPos, 210, 30);
        add(textField);
        return textField;
    }

    // Method to search for matching customers from the database
    private void searchSuggestions() {
        String input = customerSearchField.getText().trim();
        if (input.isEmpty()) {
            scrollPane.setVisible(false);
            return;
        }

        listModel.clear();      // clear old suggestions
        customerIds.clear();    // clear old ids

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "root")) {
            PreparedStatement pst = con.prepareStatement("SELECT customer_id, name, email FROM customers WHERE name LIKE ? OR email LIKE ? LIMIT 10");
            String likeInput = "%" + input + "%";
            pst.setString(1, likeInput);
            pst.setString(2, likeInput);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                listModel.addElement(name + " <" + email + ">"); // show in suggestion list
                customerIds.add(id); // store ID separately
            }

            scrollPane.setVisible(!listModel.isEmpty()); // show list if we have results
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching suggestions: " + ex.getMessage());
        }
    }

    // Method to generate a unique tracking ID
    private String generateTrackingID() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder("TRK"); // start with TRK
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Method to book shipment and save it to the database
    private void bookShipment() {
        // Check if customer is selected
        if (selectedCustomerId == -1) {
            JOptionPane.showMessageDialog(null, "Please select a valid customer from suggestions.");
            return;
        }

        try {
            // Read form values
            String receiverName = receiverNameField.getText();
            String receiverAddress = receiverAddressField.getText();
            String destination = destinationField.getText();
            String weightStr = weightField.getText();
            String shipmentType = (String) typeDropdown.getSelectedItem();

            // Check if any field is empty
            if (receiverName.isEmpty() || receiverAddress.isEmpty() || destination.isEmpty() || weightStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required.");
                return;
            }

            double weight = Double.parseDouble(weightStr); // convert string to number
            String trackingId = generateTrackingID();      // generate unique ID
            String status = "Booked";                      // set status

            // Save to database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "root");
            PreparedStatement pst = con.prepareStatement("INSERT INTO shipments (customer_id, receiver_name, receiver_address, destination, weight, shipment_type, tracking_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            pst.setInt(1, selectedCustomerId);
            pst.setString(2, receiverName);
            pst.setString(3, receiverAddress);
            pst.setString(4, destination);
            pst.setDouble(5, weight);
            pst.setString(6, shipmentType);
            pst.setString(7, trackingId);
            pst.setString(8, status);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Shipment booked successfully!\nTracking ID: " + trackingId);
                dispose();
                new EmployeeDashboardScreen(); // go back to dashboard
            }

            con.close(); // close connection
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "Tracking ID already exists. Try again.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Weight must be a number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error booking shipment: " + ex.getMessage());
        }
    }
}
