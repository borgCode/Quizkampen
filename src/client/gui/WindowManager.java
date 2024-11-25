package client.gui;

import client.gui.windows.*;
import client.network.NetworkHandler;
import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private final StartWindow startWindow;
    private final QuestionWindow questionWindow;
    private final CategoryWindow categoryWindow;
    private MenuWindow menuWindow;
    private String selectedCategory;
    private final ScoreWindow scoreWindow;
    private NetworkHandler networkHandler;
    private RegisterWindow registerWindow;
    private WelcomeWindow welcomeWindow;

    public WindowManager() {
        startWindow = new StartWindow(this);
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
    public void showRegisterWindow() {
        registerWindow = new RegisterWindow(this);
        registerWindow.setVisible(true);
    }


    public void initScoreWindowData(int rounds, Player player, Player opponent) {
        scoreWindow.setRounds(rounds);
        scoreWindow.setPlayers(player, opponent);

    }

    public void initScoreWindow() {
        scoreWindow.initScoreWindow();
        scoreWindow.setVisible(true);
        menuWindow.setVisible(false);
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

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
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

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }
    

    public void initMenuWindow(Player currentPlayer) {
        menuWindow = new MenuWindow(currentPlayer, this);
    }

    public void setLoggedInPlayer(Player player) {
    }

    public void showStartWindow() {
    }
}


