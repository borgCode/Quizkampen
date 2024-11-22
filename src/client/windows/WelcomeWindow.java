package client.windows;

import client.WindowManager;

import javax.swing.*;
import java.awt.*;

public class WelcomeWindow extends JFrame {

    public WelcomeWindow(WindowManager windowManager) {
        setTitle("Välkommen till Nya Quizkampen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,1,10,10));

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

        add(loginButton);
        add(registerButton);
        add(guestButton);

        setVisible(true);
    }
}
