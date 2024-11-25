package client.gui.windows;

import client.gui.PlayerRenderer;
import client.gui.WindowManager;
import server.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuWindow extends JFrame {

    public MenuWindow(Player currentPlayer, WindowManager windowManager) {
        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setResizable(false);
        setFocusable(false);

        // Bakgrundsbild
        JLabel background = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        background.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Marginaler mellan komponenter

        // Välkomstmeddelande
        JLabel welcomeLabel = new JLabel("Välkommen, " + currentPlayer.getName() + "!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0; // Kolumn
        gbc.gridy = 0; // Rad
        gbc.gridwidth = 3; // Sträcker sig över alla kolumner
        background.add(welcomeLabel, gbc);

        // Knapp 1 - Slumpad spelare
        JButton button1 = new JButton("Slumpad spelare");
        button1.setFont(new Font("Arial", Font.BOLD, 16));
        button1.setPreferredSize(new Dimension(160, 120));
        button1.setBackground(new Color(25, 210, 139)); // TILLFÄLLIG FÄRG KANSKE
        button1.setForeground(Color.WHITE);
        button1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button1.setFocusable(false);
        button1.addActionListener(e -> windowManager.getNetworkHandler().startRandomGame(currentPlayer));

        // Lägger till bild i knappen
        ImageIcon randomIcon = new ImageIcon("src/resources/images/slumpad.png");
        button1.setIcon(randomIcon);

        // Placerar bilden ovanför texten
        button1.setHorizontalTextPosition(SwingConstants.CENTER);
        button1.setVerticalTextPosition(SwingConstants.BOTTOM);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        background.add(button1, gbc);

        // Knapp 2 - Spela mot en vän
        JButton button2 = new JButton("Spela mot en vän");
        button2.setFont(new Font("Arial", Font.BOLD, 16));
        button2.setPreferredSize(new Dimension(160, 120));
        button2.setBackground(new Color(72, 159, 254)); // TILLFÄLLIG FÄRG KANSKE
        button2.setForeground(Color.WHITE);
        button2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button2.setFocusable(false);

        // Lägger till bild i knappen
        ImageIcon friendIcon = new ImageIcon("src/resources/images/vän.png");
        button2.setIcon(friendIcon);

        // Placerar bilden ovanför texten
        button2.setHorizontalTextPosition(SwingConstants.CENTER);
        button2.setVerticalTextPosition(SwingConstants.BOTTOM);

        gbc.gridx = 1;
        gbc.gridy = 1;
        background.add(button2, gbc);

        // Knapp 3 - Topplista
        JButton button3 = new JButton("Topplista");
        button3.setFont(new Font("Arial", Font.BOLD, 16));
        button3.setPreferredSize(new Dimension(200, 50));
        button3.setBackground(new Color(67, 120, 189)); // TILLFÄLLIG FÄRG KANSKE
        button3.setForeground(Color.WHITE);
        button3.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button3.setFocusable(false);
        button3.addActionListener(e -> {
            ArrayList<Player> players = windowManager.getNetworkHandler().getAllPLayersRanked();
            if (players == null) {
                JOptionPane.showMessageDialog(this, "Inga spelare i topplistan");
            } else {
                initPlayerRankings(players);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Tar hela bredden
        background.add(button3, gbc);

        add(background);
        setVisible(true);
    }

    private void initPlayerRankings(ArrayList<Player> players) {
        JList<Player> playerJList = new JList<>(players.toArray(new Player[0]));
        playerJList.setCellRenderer(new PlayerRenderer());

        JScrollPane scrollPane = new JScrollPane(playerJList);

        JButton closeButton = new JButton("Stäng");
        closeButton.addActionListener(e -> SwingUtilities.getWindowAncestor(closeButton).dispose());

        JFrame rankingsFrame = new JFrame("Topplista");
        rankingsFrame.setSize(400, 550);
        rankingsFrame.setLayout(new BorderLayout());
        rankingsFrame.add(scrollPane, BorderLayout.CENTER);
        rankingsFrame.add(closeButton, BorderLayout.SOUTH);
        rankingsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // TILLFÄLLIG MAIN FÖR TEST
        Player dummyPlayer = new Player("TestSpelare", "src/resources/avatars/Poliwag.png");
        WindowManager dummyWindowManager = new WindowManager();

        SwingUtilities.invokeLater(() -> new MenuWindow(dummyPlayer, dummyWindowManager));
    }
}
