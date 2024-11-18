package server.entity;

import java.util.ArrayList;

public class Round {
    private ArrayList<Question> currentQuestions;
    private String selectedCategory;
    private ArrayList<Integer> opponentRoundScore;

    public Round(ArrayList<Question> currentQuestions, String selectedCategory) {
        this.currentQuestions = currentQuestions;
        this.selectedCategory = selectedCategory;
    }

    public ArrayList<Question> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(ArrayList<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ArrayList<Integer> getOpponentRoundScore() {
        return opponentRoundScore;
    }

    public void setOpponentRoundScore(ArrayList<Integer> opponentRoundScore) {
        this.opponentRoundScore = opponentRoundScore;
    }
}
