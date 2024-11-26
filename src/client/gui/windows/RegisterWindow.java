package client.gui.windows;

import client.gui.WindowManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class RegisterWindow extends JFrame {

    private String selectedAvatarPath;
    private ArrayList<JButton> avatarButtons = new ArrayList<>();

    public RegisterWindow(WindowManager windowManager) {
        setTitle("Registrera dig");
        setSize(400, 550);
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setLayout(new BorderLayout()); // Gör så att det går att lägga till komponenter ovanpå
        setContentPane(backgroundLabel);

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false); // Gör panelen genomskinlig
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Användarnamn:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel nameLabel = new JLabel("Visningsnamn:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Lösenord:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Checkbox för att visa/dölja lösenordet
        JCheckBox showPasswordCheckBox = new JCheckBox("Visa lösenord");
        showPasswordCheckBox.setOpaque(false); // Gör checkboxen genomskinlig
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Visa texten i lösenordsfältet
            } else {
                passwordField.setEchoChar('*'); // Döljer texten med stjärnor
            }
        });

        fieldPanel.add(usernameLabel);
        fieldPanel.add(usernameField);
        fieldPanel.add(Box.createVerticalStrut(10));
        fieldPanel.add(nameLabel);
        fieldPanel.add(nameField);
        fieldPanel.add(Box.createVerticalStrut(10));
        fieldPanel.add(passwordLabel);
        fieldPanel.add(passwordField);
        fieldPanel.add(Box.createVerticalStrut(10));
        fieldPanel.add(showPasswordCheckBox);
        fieldPanel.add(Box.createVerticalStrut(10));

        backgroundLabel.add(fieldPanel, BorderLayout.NORTH);

        // Panel för avatarval
        JPanel avatarPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        TitledBorder avatarBorder = BorderFactory.createTitledBorder("Välj din avatar");
        avatarBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        avatarPanel.setBorder(avatarBorder);
        avatarPanel.setOpaque(false); // Gör panelen genomskinlig
        loadAvatarGrid(avatarPanel);

        backgroundLabel.add(avatarPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Gör panelen genomskinlig

        JButton registerButton = new JButton("Registrera dig");
        registerButton.setFocusable(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setPreferredSize(new Dimension(200, 30));
        registerButton.setBackground(Color.WHITE);
        registerButton.setForeground(Color.BLACK);

        JButton backButton = new JButton("Tillbaka");
        backButton.setFocusable(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Kontrollera att alla fält är ifyllda
            if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alla fält måste fyllas i.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedAvatarPath == null) {
                JOptionPane.showMessageDialog(this, "Vänligen välj en avatar.", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (windowManager.getNetworkHandler().registerUser(username, password, name, selectedAvatarPath)) {
                JOptionPane.showMessageDialog(this, "Registrering lyckades!");
                windowManager.showLoginWindow();
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Användarnamnet är upptaget. Försök igen!", "Felmeddelande", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener för backButton
        backButton.addActionListener(e -> {
            setVisible(false);
            windowManager.showWelcomeWindow();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);


        setVisible(true);
    }
    // Metod för att ladda avatarer
    private void loadAvatarGrid(JPanel avatarPanel) {
        String avatarsPath = "src/resources/avatars/"; // Sökvägen till alla avatarer
        File folder = new File(avatarsPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] avatarFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

            if (avatarFiles != null && avatarFiles.length == 6) { // Kontrollerar att det finns exakt 6 avatarer
                for (File file : avatarFiles) {
                    ImageIcon avatarIcon = new ImageIcon(file.getAbsolutePath());
                    Image scaledImage = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Skalar om bilden
                    avatarIcon = new ImageIcon(scaledImage);

                    JButton avatarButton = new JButton();
                    avatarButton.setFocusable(false);
                    avatarButton.setContentAreaFilled(false); // Tar bort bakgrund
                    avatarButton.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje
                    avatarButton.setIcon(avatarIcon);
                    avatarButton.setHorizontalAlignment(SwingConstants.CENTER);
                    avatarButton.setVerticalAlignment(SwingConstants.CENTER);

                    avatarButton.setPreferredSize(new Dimension(50, 50)); // Sätter knappstorleken

                    avatarButton.setActionCommand(file.getName());
                    avatarButton.addActionListener(e -> {
                        for (JButton button : avatarButtons) {
                            button.setBorder(BorderFactory.createEmptyBorder());
                        }
                        avatarButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
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
}


