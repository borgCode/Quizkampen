package server.entity;

import javax.swing.*;
import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int numOfWins;
    private int numOfLosses;
    private ImageIcon avatar;
    private Player opponent;

    public Player(String name) {
        this.name = name;
        this.avatar = avatar;
    }
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    public Player getOpponent() {
        return opponent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

