package server.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public static void findWinner (ObjectOutputStream p1, ObjectOutputStream p2, int p1Score, int p2Score) throws IOException {
        String tie = "Spelet blev lika!";
        String loser = "Du Förlora!";
        String winner = "Du vann!";
        if (p1Score == p2Score) {
            p1.writeObject(tie);
            p2.writeObject(tie);
        } else if (p1Score > p2Score) {
            p1.writeObject(winner);
            p2.writeObject(loser);
        } else if (p2Score > p1Score) {
            p1.writeObject(loser);
            p2.writeObject(winner);
        }

    }
    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }
}

// https://youtrack.jetbrains.com/issue/CWM-9503 check this URL !
