import SWING.LibraryMainWindow;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Launch the Library main swing interface
        SwingUtilities.invokeLater(() -> LibraryMainWindow.main(args));
    }
}