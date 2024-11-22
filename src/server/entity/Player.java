package server.entity;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String password;
    private int numOfWins;
    private int numOfLosses;
    private String avatarPath;


    public Player(String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
    }
    

    public String getName() {
        return name;
    }
    

    public String getAvatarPath() {
        return avatarPath;
    }

    public ImageIcon getAvatar(String imagePath) {
        ImageIcon avatarIcon = new ImageIcon(imagePath);
        Image scaledImage = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); 
        return new ImageIcon(scaledImage);
    }
    
}
