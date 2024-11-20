package server.entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private int playerScore;
    private int numOfLosses;
    private ImageIcon avatar;
    private Player opponent;


    private List<Boolean> answers  = new ArrayList<>();;

    // Getter och Setter för svar
    public List<Boolean> getAnswers() {
        return answers;
    }

    public void addAnswer(boolean isCorrect) {
    }


    public Player(String name, ImageIcon avatar) {
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

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageIcon getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageIcon avatar) {
        this.avatar = avatar;
    }
}
