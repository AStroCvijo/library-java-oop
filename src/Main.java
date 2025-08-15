import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Hello Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Create a label and add it to the frame
        JLabel label = new JLabel("Zdravo, Swing!", SwingConstants.CENTER);
        frame.getContentPane().add(label);

        // Make the window visible
        frame.setVisible(true);
    }
}
