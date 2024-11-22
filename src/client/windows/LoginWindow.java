package client.windows;

import client.WindowManager;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    public LoginWindow(WindowManager windowManager) {
        setTitle("Logga in");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,400);
        setLayout(new GridLayout(3,1,10,10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Logga in");
        JButton backButton = new JButton("Tillbaka");

        // Action listener för loginButton
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Skicka inloggningsuppgifter till servern
            if (windowManager.authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Inloggning lyckades!");
                windowManager.showScoreWindow();
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Felaktigt användarnamn eller lösenord!");
            }
        });

        // Action listener för backButton
        backButton.addActionListener(e -> {
            setVisible(false);
            windowManager.showWelcomeWindow();
        });

        add(new JLabel("Användarnamn:"));
        add(usernameField);
        add(new JLabel("Lösenord:"));
        add(passwordField);
        add(loginButton);
        add(backButton);

        setVisible(true);
    }
}

