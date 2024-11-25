package client.gui.windows;

import client.gui.WindowManager;
import client.network.NetworkHandler;
import server.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

public class StartWindow extends JFrame {

    private JTextField nameField;
    private String selectedAvatarPath;
    private ArrayList<JButton> avatarButtons = new ArrayList<>();
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public StartWindow(WindowManager windowManager) {

        // Grundinställningar för startfönstret
        setTitle("Quizkampen");
        setSize(400, 550);
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att det går att lägga till komponenter ovanpå

        // Huvudpanel för alla komponenter
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false); // Gör panelen genomskinlig så bakgrundsbilden syns

        // Övre panel: Bilden högst upp
        JLabel headerImage = new JLabel(new ImageIcon("src/resources/categoryImages/quizkampen.png")); // Sökväg till bilden
        headerImage.setHorizontalAlignment(SwingConstants.CENTER); // Centrerar bilden
        headerImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Lägger till padding runt bilden
        mainPanel.add(headerImage, BorderLayout.NORTH);

        // Panel för namn och avatarer
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Gör panelen genomskinlig

        // Namndelen
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Skriv in ditt namn: ");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(20); // Textfält för namn
        nameField.setFont(new Font("Arial", Font.BOLD, 16));
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(10)); // Lägger till mellanrum mellan label och textfält
        namePanel.add(nameField);
        namePanel.add(Box.createVerticalStrut(20)); // Lägger till mellanrum efter textfältet

        contentPanel.add(namePanel);

        // Avatarer med rubrik
        JLabel avatarTitle = new JLabel("Välj din avatar:");
        avatarTitle.setFont(new Font("Arial", Font.BOLD, 16));
        avatarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Lägger till mellanrum under texten

        JPanel avatarPanel = new JPanel(new GridLayout(2, 3, 15, 15)); // 2 rader, 3 kolumner, med mellanrum
        avatarPanel.setOpaque(false); // Gör panelen genomskinlig
        loadAvatarGrid(avatarPanel); // Ladda avatar-knapparna

        contentPanel.add(avatarTitle);
        contentPanel.add(avatarPanel);
        contentPanel.add(Box.createVerticalStrut(20)); // Lägger till mellanrum under avatarerna
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel för knappar

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Lägg till mellanrum mellan knapparna
        buttonPanel.setOpaque(false);

        JButton startButton = new JButton("Fortsätt som gäst");
        startButton.setFocusable(false);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setPreferredSize(new Dimension(200, 30));
        startButton.addActionListener(e -> startGame()); // ActionListener för spel-knappen

        JButton backButton = new JButton("Tillbaka");
        backButton.setFocusable(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            // Stänger detta fönster och öppnar WelcomeWindow
            setVisible(false);
            windowManager.showWelcomeWindow();
        });

        buttonPanel.add(startButton);
//        buttonPanel.add(Box.createHorizontalStrut(20)); // Lägger till mellanrum mellan knapparna
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // KeyListener för Enter-knappen
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startGame();
                }
            }
        });

        // Lägger till allt till bakgrundsetiketten
        backgroundLabel.add(mainPanel);

        // Lägger till bakgrundsbilden till fönstret
        setContentPane(backgroundLabel);

//        setVisible(true);
    }

    // Metod för att ladda avatarer
    private void loadAvatarGrid(JPanel avatarPanel) {
        String avatarsPath = "src/resources/avatars/"; // Sökvägen till alla avatarer
        File folder = new File(avatarsPath);

        // Kontrollerar om avatar mappen finns
        if (folder.exists() && folder.isDirectory()) {
            File[] avatarFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

            if (avatarFiles != null && avatarFiles.length == 6) { // Kontrollerar att det finns exakt 6 avatarer
                for (File file : avatarFiles) {
                    ImageIcon avatarIcon = new ImageIcon(file.getAbsolutePath());
                    Image scaledImage = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Ställer in avatarernas storlek
                    avatarIcon = new ImageIcon(scaledImage);

                    // Skapar knapp
                    JButton avatarButton = new JButton();
                    avatarButton.setFocusable(false);
                    avatarButton.setContentAreaFilled(false); // Tar bort bakgrund
                    avatarButton.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje


                    avatarButton.setIcon(avatarIcon); // Sätter avataren som knappens ikon
                    avatarButton.setHorizontalAlignment(SwingConstants.CENTER);
                    avatarButton.setVerticalAlignment(SwingConstants.CENTER);


                    avatarButton.setPreferredSize(new Dimension(50, 50)); // Sätter knappstorleken

                    avatarButton.setActionCommand(file.getName()); // Sätter action command till filnamnet
                    avatarButton.addActionListener(e -> {
                        // Highlighta den valda avataren
                        for (JButton button : avatarButtons) {
                            button.setBorder(BorderFactory.createEmptyBorder()); // Återställer kantlinjen runt avataren
                        }

                        avatarButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); // Markerar den valda avataren

                        // Gör om action command (filnamn) till en ImageIcon för att langa in det i player korrekt
                        selectedAvatarPath = "src/resources/avatars/" + e.getActionCommand();
                    });

                    avatarButtons.add(avatarButton);
                    avatarPanel.add(avatarButton);

                }
            } else {
                JOptionPane.showMessageDialog(this, "6 avatarer är ett måste!", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Avatar mappen hittas ej!", "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metod för att starta spelet
    private void startGame() {
        String playerName = nameField.getText().trim();

        // Kontrollerar att inte namnfältet är tomt, skriver ut felmeddelande annars
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vänligen skriv in ditt namn.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Kontrollerar att en avatar är vald, annars skriver ut felmeddelande
        if (selectedAvatarPath == null) {
            JOptionPane.showMessageDialog(this, "Vänligen välj en avatar.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
            return;
        }

        player = new Player(playerName, selectedAvatarPath);

        // Skicka spelaren till WindowManager och starta NetworkHandler
        WindowManager windowManager = new WindowManager();
        windowManager.setLoggedInPlayer(player);

        // Starta NetworkHandler i en ny tråd
        new Thread(() -> new NetworkHandler(windowManager)).start();

        // Stänger StartWindow
        dispose();

    }

}
