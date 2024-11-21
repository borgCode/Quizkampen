package client;

import server.entity.Game;
import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {
    private StartWindow startWindow;
    private GameWindow gameWindow;
    private CategoryWindow categoryWindow;
    private String selectedCategory;
    private ScoreWindow scoreWindow;
    private Player player1, player2;
    private Game game;


    public WindowManager() {
        startWindow = new StartWindow();
        startWindow.setVisible(true);
        gameWindow = new GameWindow();
        gameWindow.setAnswerListener(selectedAnswer -> {
            System.out.println("Selected answer: " + selectedAnswer);
        });

        categoryWindow = new CategoryWindow();

    }

    public void setGameWindowVisibility(Boolean isVisible) {
        gameWindow.setVisible(isVisible);
    }


    public Player getPlayer() {
        return startWindow.getPlayer();
    }

    public void displayQuestion(Question question) {
        gameWindow.updateQuestion(question);
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public void resetRound() {
        gameWindow.resetRound();
    }

    public void showCategoryWindow(ArrayList<String> categories) {
        categoryWindow.updateCategories(categories);
        categoryWindow.setListener(category -> setSelectedCategory(category));
        // Skapar CategoryWindow bara när det behövs
        categoryWindow.setVisible(true);
    }
    public void setSelectedCategory(String category) {
        this.selectedCategory = category;
    }
    public String getSelectedCategory() {
        return selectedCategory;
    }


    // TODO: showScoreWindow och updateScore
    /*
    public void showScorewWindow(Player player, ArrayList<Integer> scoreList) {
        if (scoreWindow == null) {
            scoreWindow = new ScoreWindow(player1, player2);

            // Lägg till ActionListener för "Nästa rond"-knappen
            scoreWindow.getNextRoundButton().addActionListener(e -> {
                scoreWindow.setVisible(false);
                gameWindow.setVisible(true);
            });
        }

        // Uppdatera poäng och visa ScoreWindow
        scoreWindow.updateScores(List<Integer> player1Scores, List<Integer>player2Scores);
        scoreWindow.setVisible(true);
        gameWindow.setVisible(false);
        }
        */

    }




