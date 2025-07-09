import javax.swing.*;       // Imports Swing GUI components like JFrame, JLabel, JButton, etc.
import java.awt.*;          // Imports classes for layout, color, fonts, screen size, etc.
import java.awt.event.*;    // Imports classes for handling events like button clicks and timers

public class WelcomeScreen extends JFrame {
    private JLabel animatedLabel;     // Label that will move (animate) across the screen
    private Timer animationTimer;     // Timer that updates animation step by step
    private int x = 1200;             // Initial X-position for the animated label (starts from right side of screen)

    public WelcomeScreen() {
        // Get the full screen size of the monitor
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set the frame size to full screen
        setSize(screenSize);
        setLayout(null);  // We use absolute positioning instead of a layout manager

        // Close the window and exit the program when the user clicks "X"
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set background color of the window
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray

        // Create a moving title label (animated text)
        animatedLabel = new JLabel("ThariTrack - Smart Courier System"); // Main title
        animatedLabel.setFont(new Font("Serif", Font.BOLD, 40));        // Font and size
        animatedLabel.setForeground(new Color(0, 102, 204));            // Text color (blue)
        animatedLabel.setBounds(x, 150, 800, 60);                       // Initial position
        add(animatedLabel);                                             // Add it to the frame

        // Timer to move the label continuously across the screen
        animationTimer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                x -= 2;  // Move the label 2 pixels to the left
                if (x < -800) {
                    // If the label goes completely off-screen to the left, reset it to the right
                    x = screenSize.width;
                }
                // Update label position with new X-coordinate
                animatedLabel.setBounds(x, 150, 800, 60);
            }
        });
        animationTimer.start();  // Start the animation

        // Create a subtitle label just below the animated title
        JLabel subtitle = new JLabel("Delivering with trust, tracking with ease", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 22));      // Font style and size
        subtitle.setBounds((screenSize.width - 600) / 2, 230, 600, 30); // Center it horizontally
        subtitle.setForeground(Color.DARK_GRAY);                       // Text color
        add(subtitle);                                                 // Add it to the frame

        // "Get Started" button to proceed
        JButton startButton = new JButton("Get Started");
        startButton.setBounds((screenSize.width - 150) / 2, 320, 150, 45); // Center the button
        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));         // Font styling
        startButton.setBackground(new Color(0, 153, 76));                  // Green background
        startButton.setForeground(Color.WHITE);                            // White text
        add(startButton);                                                  // Add it to the frame

        // What should happen when the "Get Started" button is clicked
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animationTimer.stop();  // Stop the animation timer
                dispose();              // Close this WelcomeScreen window
                new UserSelectionScreen(); // Open the next screen (user selection)
            }
        });

        setVisible(true); // Make the window visible on screen
    }

    public static void main(String[] args) {
        new WelcomeScreen(); // Run the program by creating an object of WelcomeScreen
    }
}
