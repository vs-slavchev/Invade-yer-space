package utility;

import javax.swing.*;

public class InvadeError {
    public static void show(String errorMessage) {
        JOptionPane.showMessageDialog(null,
                "Error: \n" + errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
