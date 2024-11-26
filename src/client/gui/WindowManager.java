package client.gui;

import client.gui.windows.*;
import client.network.NetworkHandler;
import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private GuestWindow guestWindow;
    private final QuestionWindow questionWindow;
    private final CategoryWindow categoryWindow;
    private MenuWindow menuWindow;
    private String selectedCategory;
    private final ScoreWindow scoreWindow;
    private NetworkHandler networkHandler;
    private RegisterWindow registerWindow;
    private WelcomeWindow welcomeWindow;
    private String currentCategory;

    public WindowManager() {
        guestWindow = new GuestWindow(this);
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
        return guestWindow.getPlayer();
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
    public void switchPlayButton() {
        scoreWindow.switchPlayButton();
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
    public void resetScoreList() {
        scoreWindow.resetScoreList();
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
    

    public void showStartWindow() {
        guestWindow = new GuestWindow(this);
        guestWindow.setVisible(true);
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public void updateCategory() {
        scoreWindow.updateCategory(currentCategory);
    }
}


