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


        JLabel background = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        background.setLayout(new BorderLayout());

        JPanel centerPanel = getCenterPanel(currentPlayer, windowManager);
        JPanel topPanel = createTopPanel(currentPlayer);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(centerPanel, BorderLayout.CENTER);
        add(background);

        setVisible(true);
    }

    private JPanel createTopPanel(Player currentPlayer) {

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Välkommen, " + currentPlayer.getName());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(welcomeLabel);
        return topPanel;
    }

    private JPanel getCenterPanel(Player currentPlayer, WindowManager windowManager) {
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerPanel.setOpaque(false);

        JButton button1 = new JButton("Spela mot slumpmässig motståndare");
        button1.addActionListener(e -> windowManager.getNetworkHandler().startRandomGame(currentPlayer));

        JButton button2 = new JButton("Leta efter spelare att spela mot");

        JButton button3 = new JButton("Se topplista");
        button3.addActionListener(e -> {
            ArrayList<Player> players = windowManager.getNetworkHandler().getAllPLayersRanked();
            if (players == null) {
                JOptionPane.showMessageDialog(this, "Inga spelare i topplistan");
            } else {
                initPlayerRankings(players);
            }
        });

        centerPanel.add(button1);
        centerPanel.add(button2);
        centerPanel.add(button3);

        return centerPanel;
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
}
