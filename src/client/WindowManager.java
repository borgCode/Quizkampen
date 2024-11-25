package client;

import client.network.NetworkHandler;
import client.windows.*;
import server.entity.Player;
import server.entity.Question;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private final StartWindow startWindow;
    private final QuestionWindow questionWindow;
    private final CategoryWindow categoryWindow;
    private String selectedCategory;
    private final ScoreWindow scoreWindow;
    private NetworkHandler networkHandler;
    

    public WindowManager() {
        startWindow = new StartWindow();
//        startWindow.setVisible(true);
        questionWindow = new QuestionWindow();
        questionWindow.setAnswerListener(selectedAnswer -> {
            System.out.println("Selected answer: " + selectedAnswer);
        });
        scoreWindow = new ScoreWindow();
        categoryWindow = new CategoryWindow();
    }
    public void showWelcomeWindow() {
        WelcomeWindow welcomeWindow = new WelcomeWindow(this);
        welcomeWindow.setVisible(true);
    }

    public void showLoginWindow() {
        LoginWindow loginWindow = new LoginWindow(this);
        loginWindow.setVisible(true);
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

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
    

    public void initMenuWindow(Player currentPlayer) {
        MenuWindow menuWindow = new MenuWindow(currentPlayer, this);
    }
}


