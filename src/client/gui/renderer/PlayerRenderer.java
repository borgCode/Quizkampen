package client.gui.renderer;

import server.entity.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PlayerRenderer extends JPanel implements ListCellRenderer<Player> {
    
    private JLabel avatarLabel;
    private JLabel nameLabel;
    private JLabel statsLabel;

    public PlayerRenderer() {
        setLayout(new BorderLayout());
        avatarLabel = new JLabel();
        nameLabel = new JLabel();
        statsLabel = new JLabel();


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(nameLabel);
        infoPanel.add(statsLabel);
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        add(avatarLabel, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
        
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Player> list, Player player, int index, boolean isSelected, boolean cellHasFocus) {
        ImageIcon avatarIcon = new ImageIcon(player.getAvatarPath());
        Image scaledAvatar = avatarIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        avatarLabel.setIcon(new ImageIcon(scaledAvatar));

        nameLabel.setText(player.getName());
        statsLabel.setText("Vinster: " + player.getNumOfWins() + " | Förluster: " + player.getNumOfLosses());
        

        return this;
        
    }
}
