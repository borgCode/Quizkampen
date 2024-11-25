package server.entity;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private String username;
    private String password;
    private String name;
    private int numOfWins;
    private int numOfLosses;
    private String avatarPath;


    public Player(String name, String avatarPath) {
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public void setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
    }

    public int getNumOfLosses() {
        return numOfLosses;
    }

    public void setNumOfLosses(int numOfLosses) {
        this.numOfLosses = numOfLosses;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public ImageIcon getAvatar(String imagePath) {
        ImageIcon avatarIcon = new ImageIcon(imagePath);
        Image scaledImage = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); 
        return new ImageIcon(scaledImage);
    }
    
}
