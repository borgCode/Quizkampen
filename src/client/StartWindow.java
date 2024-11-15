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
        setTitle("Quizkampen");
        setSize(400,200);
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(20);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        nameLabel.setForeground(Color.black);
        nameLabel.setBackground(Color.white);

        JLabel avatarLabel = new JLabel("Choose your avatar: ");
        avatarLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        avatarComboBox = new JComboBox<>();

        JButton startButton = new JButton("START");
        startButton.setFocusable(false);
        startButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.white);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(nameLabel);
        add(nameField);
        add(avatarLabel);
        add(avatarComboBox);
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {startGame();}
        });

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
            }
        });

        loadAvatars();
        setVisible(true);

    }
    private void loadAvatars() {
        String avatarsPath = ""; //TODO, skapa mapp med avatars

        avatarComboBox.setPreferredSize(new Dimension(150, 40));

    }


    private void startGame() {
        String playerName = nameField.getText().trim();
        ImageIcon selectedAvatar = (ImageIcon) avatarComboBox.getSelectedItem();

        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (selectedAvatar == null) {
            JOptionPane.showMessageDialog(this, "Please select an avatar.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            new GameWindow(playerName, selectedAvatar);
            dispose();
        }
    }

}

