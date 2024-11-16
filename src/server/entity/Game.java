package server.entity;

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

    public void incrementScore(Player player, int score) {
        if (player.equals(player1)) {
            player1Score += score;
        } else {
            player2Score += score;
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}

// https://youtrack.jetbrains.com/issue/CWM-9503 check this URL ! 

