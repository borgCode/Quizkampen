package client.gui.windows;

import client.gui.WindowManager;

import javax.swing.*;
import java.awt.*;

public class WelcomeWindow extends JFrame {

    public WelcomeWindow(WindowManager windowManager) {
        setTitle("Välkommen till Nya Quizkampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,1,10,10));
        setResizable(false);
        setFocusable(false);

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att vi kan placera komponenter ovanpå

        // Bild högst upp
        JLabel headerImage = new JLabel(new ImageIcon("src/resources/categoryImages/quizkampen.png"));
        headerImage.setHorizontalAlignment(SwingConstants.CENTER); // Centrerar bilden
        headerImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Padding runt bilden

        // Panel för knapparna
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false); // Gör panelen genomskinlig

        // Logga in knapp
        JButton loginButton = new JButton("Logga in");
        loginButton.addActionListener(e -> {
            windowManager.showLoginWindow();
            setVisible(false);
        });

        // Registrera dig knappen
        JButton registerButton = new JButton("Registrera dig");
        registerButton.addActionListener(e -> {
            windowManager.showRegisterWindow();
            setVisible(false);
        });

        // Spela som gäst knappen
        JButton guestButton = new JButton("Spela som gäst");
        guestButton.addActionListener(e -> {
            windowManager.showStartWindow();
            setVisible(false);
        });

        // Sätter stilen på knapparna
        JButton[] buttons = {loginButton, registerButton, guestButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setPreferredSize(new Dimension(200, 40));
            button.setFocusable(false);
            button.setBackground(new Color(96, 140, 203)); // TILLFÄLLIG FÄRG
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Lägg till kant
        }

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(guestButton);

        // Lägg till header-bilden och knapparna till bakgrundslabel
        backgroundLabel.add(headerImage, BorderLayout.NORTH);
        backgroundLabel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(backgroundLabel);

        setVisible(true);
    }
}
