package server.entity;

import java.util.ArrayList;

public class Game {
    private Player player1;
    private Player player2;
    private int player1Score;
    private int player2Score;
    private String winner;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = 0;
        this.player2Score = 0;
    }

    public void incrementScore(Player player, ArrayList<Integer> scoreList) {
        if (player.equals(player1)) {
            for (Integer score : scoreList) {
                player1Score += score;
            }
        } else {
            for (Integer score : scoreList) {
                player2Score+= score;
            }
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}

// https://youtrack.jetbrains.com/issue/CWM-9503 check this URL ! 

