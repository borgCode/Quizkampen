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
        
        
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = getCenterPanel(currentPlayer, windowManager);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        JButton topRightButton = new JButton("Inställningar");
        topRightButton.setPreferredSize(new Dimension(80, 25)); 
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topRightButton, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        add(mainPanel);
        
        setVisible(true);
    }

    private JPanel getCenterPanel(Player currentPlayer, WindowManager windowManager) {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton button1 = new JButton("Spela mot slumpmässig motståndare");
        button1.addActionListener(e -> windowManager.getNetworkHandler().startRandomGame(currentPlayer));

        JButton button2 = new JButton("Leta efter spelare att spela mot");


        JButton button3 = new JButton("Se topplista");
        button3.addActionListener(e -> {
            ArrayList<Player> players = windowManager.getNetworkHandler().getAllPLayersRanked();
            if (players == null) {
                JOptionPane.showConfirmDialog(this, "Inga spelare i topplistan");
            } else {
                initPlayerRankings(players);
            }
            
        });
        centerPanel.add(button1);
        centerPanel.add(button2);
        centerPanel.add(button3);

        JLabel label = new JLabel(currentPlayer.getName());

        centerPanel.add(label);
        return centerPanel;
    }

    private void initPlayerRankings(ArrayList<Player> players) {
        
        JList<Player> playerJList = new JList<>(players.toArray(new Player[0]));
        playerJList.setCellRenderer(new PlayerRenderer());
        
        JScrollPane scrollPane = new JScrollPane(playerJList);

        JButton closeButton = new JButton("Stäng");
        closeButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(closeButton).dispose();
        });

        JFrame rankingsFrame = new JFrame("Topplista");
        rankingsFrame.setSize(400, 550);
        rankingsFrame.setLayout(new BorderLayout());
        rankingsFrame.add(scrollPane, BorderLayout.CENTER);
        rankingsFrame.add(closeButton, BorderLayout.SOUTH);
        rankingsFrame.setVisible(true);
    }

}
