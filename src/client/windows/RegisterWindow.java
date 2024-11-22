package client.windows;

import client.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RegisterWindow extends JFrame {
    public RegisterWindow(WindowManager windowManager) {
        setTitle("Registrera dig");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JTextField usernameField = new JTextField();
        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Tillfälligt ska fixa snyggare
        JComboBox<String> avatarComboBox = new JComboBox<>(new String[] {
                "Avatar1", "Avatar2", "Avatar3", "Avatar4"
        });

        JButton registerButton = new JButton("Registrera dig");
        JButton backButton = new JButton("Tillbaka");

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());
            String avatar = (String) avatarComboBox.getSelectedItem(); // Kan vara en filväg

            // Skicka registreringsuppgifter till servern
            if (windowManager.registerUser(username, name, password, avatar)) {
                JOptionPane.showMessageDialog(this, "Registrering lyckades!");
                windowManager.showStartWindow();
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Registreringen misslyckades. Försök igen.");
            }
        });

        add(new JLabel("Användarnamn:"));
        add(usernameField);
        add(new JLabel("Namn:"));
        add(nameField);
        add(new JLabel("Lösenord:"));
        add(passwordField);
        add(new JLabel("Välj avatar:"));
        add(avatarComboBox);
        add(registerButton);
        add(backButton);

        setVisible(true);
    }
}

