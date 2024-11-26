package server.entity;

import java.util.ArrayList;

public class Round {
    private ArrayList<Question> currentQuestions;
    private ArrayList<Integer> roundScore;

    public Round(ArrayList<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public ArrayList<Question> getCurrentQuestions() {
        return currentQuestions;
    }

    public ArrayList<Integer> getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(ArrayList<Integer> roundScore) {
        this.roundScore = roundScore;
    }
}
