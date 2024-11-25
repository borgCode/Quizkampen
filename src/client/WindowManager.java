package client;

import client.windows.CategoryWindow;
import client.windows.QuestionWindow;
import client.windows.ScoreWindow;
import client.windows.StartWindow;
import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private final StartWindow startWindow;
    private final QuestionWindow questionWindow;
    private final CategoryWindow categoryWindow;
    private String selectedCategory;
    private final ScoreWindow scoreWindow;
    private List<String> roundCategories = new ArrayList<>();

    public WindowManager() {
        startWindow = new StartWindow();
        startWindow.setVisible(true);
        questionWindow = new QuestionWindow();
        questionWindow.setAnswerListener(selectedAnswer -> {
            System.out.println("Selected answer: " + selectedAnswer);
        });
        scoreWindow = new ScoreWindow();
        categoryWindow = new CategoryWindow();
    }

    //TODO: NYTT:

    public void printAllCategoriesFromScoreWindow() {
        scoreWindow.printAllRoundCategories();
    }

    public int getCurrentRound() {
        return scoreWindow.getCurrentRound();
    }
    public void updateRoundCategory(int roundNumber, String category) {
        // Säkerställ att listan är tillräckligt stor
        while (roundCategories.size() < roundNumber) {
            roundCategories.add(null); // Lägg till tomma platser
        }

        // Uppdatera kategorin för ronden
        roundCategories.set(roundNumber - 1, category);

        // Uppdatera ScoreWindow
        scoreWindow.updateAllRoundCategories(roundCategories);
    }

    public void updateAllRoundsInScoreWindow() {
        scoreWindow.updateAllRoundCategories(roundCategories);
    }

    public List<String> getRoundCategories() {
        return roundCategories;
    }


    public void initScoreWindowData(int rounds, Player player, Player opponent) {
        scoreWindow.setRounds(rounds);
        scoreWindow.setPlayers(player, opponent);

    }

    public void initScoreWindow() {
        scoreWindow.initScoreWindow();
        scoreWindow.setVisible(true);
    }

    public void showCategoryWindow(ArrayList<String> categories) {
        scoreWindow.setVisible(false);
        categoryWindow.updateCategories(categories);
        categoryWindow.setListener(category -> setSelectedCategory(category));
        categoryWindow.setVisible(true);
    }

    public void showQuestionWindow() {
        questionWindow.setVisible(true);
        categoryWindow.setVisible(false);
        scoreWindow.setVisible(false);
    }

    public void showScoreWindow() {
        scoreWindow.setVisible(true);
        questionWindow.setVisible(false);
    }


    public Player getPlayer() {
        return startWindow.getPlayer();
    }

    public void displayQuestion(Question question) {
        questionWindow.updateQuestion(question);
    }

    public QuestionWindow getGameWindow() {
        return questionWindow;
    }

    public void resetRound() {
        questionWindow.resetRound();
    }


    public void setSelectedCategory(String category) {
        this.selectedCategory = category;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public boolean hasUserGivenUp() {
        return scoreWindow.hasUserGivenUp();
    }

    public void updatePlayerScore(List<Integer> scoreList) {
        scoreWindow.updatePlayerScore(scoreList);
    }

    public void updateOpponentScore(List<Integer> opponentScore) {
        scoreWindow.updateOpponentScore(opponentScore);
    }

    public void nextRound() {
        scoreWindow.nextRound();
    }

    public boolean hasClickedPlay() {
        return scoreWindow.hasClickedPlay();
    }


    public void setHasClickedPlay(boolean hasClicked) {
        scoreWindow.setHasClickedPlay(hasClicked);
    }
}


