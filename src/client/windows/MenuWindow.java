package client.windows;

import client.WindowManager;
import server.entity.Player;

import javax.swing.*;
import java.awt.*;

public class MenuWindow extends JFrame {
    
    
    public MenuWindow(Player currentPlayer, WindowManager windowManager) {

        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setResizable(false);
        setFocusable(false);
        
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton button1 = new JButton("Spela mot slumpmässig motståndare");
        button1.addActionListener(e -> windowManager.getNetworkHandler().startRandomGame(currentPlayer));
        
        JButton button2 = new JButton("Leta efter spelare att spela mot");
        JButton button3 = new JButton("Se topplista");
        centerPanel.add(button1);
        centerPanel.add(button2);
        centerPanel.add(button3);
        
        JLabel label = new JLabel(currentPlayer.getName());
        
        centerPanel.add(label);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        JButton topRightButton = new JButton("Inställningar");
        topRightButton.setPreferredSize(new Dimension(80, 25)); 
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topRightButton, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        add(mainPanel);
        
        setVisible(true);
    }
    
}
