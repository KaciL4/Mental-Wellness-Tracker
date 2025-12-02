import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mental Wellness Tracker System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000,700);
            frame.setLocationRelativeTo(null);
            frame.add(new GUI());
            frame.setVisible(true);
        });
    }
}