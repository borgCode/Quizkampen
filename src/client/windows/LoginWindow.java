package client.windows;

import client.WindowManager;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    public LoginWindow(WindowManager windowManager) {
        setTitle("Logga in");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,500);
        setLayout(new GridLayout(3,1,10,10));

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att komponenter kan placeras ovanpå
        setContentPane(backgroundLabel);

        // Bild högst upp
        JLabel headerImage = new JLabel(new ImageIcon("src/resources/images/loggain.png"));
        headerImage.setHorizontalAlignment(SwingConstants.CENTER); // Centrerar bilden
        headerImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Padding runt bilden

        // Skapa panel för inloggningsfälten
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false); // Gör panelen genomskinlig
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel usernameLabel = new JLabel("Användarnamn:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(550, 25));

        JLabel passwordLabel = new JLabel("Lösenord:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(550, 25));

        // Checkbox för att visa/dölja lösenordet
        JCheckBox showPasswordCheckBox = new JCheckBox("Visa lösenord");
        showPasswordCheckBox.setOpaque(false); // Gör checkboxen genomskinlig
        showPasswordCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Visa texten i lösenordsfältet
            } else {
                passwordField.setEchoChar('*'); // Döljer texten med stjärnor
            }
        });

        fieldPanel.add(usernameLabel);
        fieldPanel.add(usernameField);
        fieldPanel.add(Box.createVerticalStrut(15));
        fieldPanel.add(passwordLabel);
        fieldPanel.add(passwordField);
        fieldPanel.add(Box.createVerticalStrut(10));
        fieldPanel.add(showPasswordCheckBox);
        fieldPanel.add(Box.createVerticalStrut(20));

        backgroundLabel.add(fieldPanel, BorderLayout.CENTER);
        backgroundLabel.add(headerImage, BorderLayout.NORTH);

        // Skapa panel för knappar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Gör panelen genomskinlig

        JButton loginButton = new JButton("Logga in");
        loginButton.setFocusable(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Color.BLACK);

        JButton backButton = new JButton("Tillbaka");
        backButton.setFocusable(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);

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

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}

