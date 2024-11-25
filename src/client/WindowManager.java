package client;

import client.network.ClientPreGameProtocol;
import client.network.NetworkHandler;
import client.windows.*;
import server.entity.Player;
import server.entity.Question;
import server.network.ServerPreGameProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private StartWindow startWindow;
    private final QuestionWindow questionWindow;
    private final CategoryWindow categoryWindow;
    private String selectedCategory;
    private final ScoreWindow scoreWindow;
    private WelcomeWindow welcomeWindow;
    private LoginWindow loginWindow;
    private RegisterWindow registerWindow;
    private Player loggedInPlayer;
    private NetworkHandler networkHandler;

    public WindowManager() {
        //this.networkHandler = new NetworkHandler(this);
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
        welcomeWindow = new WelcomeWindow(this);
        welcomeWindow.setVisible(true);
    }

    public void showLoginWindow() {
        loginWindow = new LoginWindow(this);
        loginWindow.setVisible(true);
    }

    public void showRegisterWindow() {
        registerWindow = new RegisterWindow(this);
        registerWindow.setVisible(true);
    }

    public void showStartWindow() {
        startWindow = new StartWindow(this);
        startWindow.setVisible(true);
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
        if (loggedInPlayer != null) {
            return loggedInPlayer;
        } else {
            return startWindow.getPlayer();
        }
    }

    public void displayQuestion(Question question) {
        questionWindow.updateQuestion(question);
    }

    public void setLoggedInPlayer(Player player) {
        this.loggedInPlayer = player;
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

    public Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public boolean registerUser(String username, String name, String password, String avatar) {
        return networkHandler.registerUser(username, name, password, avatar);
    }



        // TILLFÄLLIG MAIN FÖR TEST
        public static void main(String[] args) {
            WindowManager windowManager = new WindowManager();

            windowManager.showWelcomeWindow();
        }


}


