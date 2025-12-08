package util;

import javax.swing.*;
import java.util.Scanner;

public class CLIUtil {
    public static String getString(String mess) {
        String input = JOptionPane.showInputDialog(null, mess);
        return input;
    }

    public static int getInt(String mess) {
        int input = Integer.parseInt(JOptionPane.showInputDialog(null, mess));
        return input;
    }
}