package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartWindow extends JFrame {
    
    private JTextField nameField;
    private JComboBox<ImageIcon> avatarComboBox;

    
    public StartWindow() {

        // Grundinställningar för startfönstret
        setTitle("Quizkampen");
        setSize(400,200);
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Skapar label och textfält för användarens namn
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(20);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        nameLabel.setForeground(Color.black);
        nameLabel.setBackground(Color.white);

        // Skapar label för avatar
        JLabel avatarLabel = new JLabel("Choose your avatar: ");
        avatarLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        avatarComboBox = new JComboBox<>();

        // Skapar knapp för att starta spelet
        JButton startButton = new JButton("START");
        startButton.setFocusable(false);
        startButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.white);

        // Centrerar alla komponenter i fönstret
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Lägger till komponenterna i fönstret
        add(nameLabel);
        add(nameField);
        add(avatarLabel);
        add(avatarComboBox);
        add(startButton);

        // ActionListener för startknappen
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {startGame();}
        });

        // KeyListener för Enter-knappen
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
            }
        });

        // Laddar in avatarerna i comboboxen
        loadAvatars();
        setVisible(true);

    }

    // Metod för att ladda avatarer i comboboxen
    private void loadAvatars() {
        String avatarsPath = ""; //TODO, skapa mapp med avatars

        avatarComboBox.setPreferredSize(new Dimension(150, 40));

    }

    // Metod för att starta spelet
    private void startGame() {
        String playerName = nameField.getText().trim();
        ImageIcon selectedAvatar = (ImageIcon) avatarComboBox.getSelectedItem();

        // Kollar om avatar eller namnfältet är tomt, ger felmeddelande vid tomt
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (selectedAvatar == null) {
            JOptionPane.showMessageDialog(this, "Please select an avatar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Startar GameWindow med spelarens namn och valda avatar och stänger start fönstret
            //new GameWindow(playerName, selectedAvatar);
            dispose();
        }
    }

}

