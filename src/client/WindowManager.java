package client;

import client.windows.CategoryWindow;
import client.windows.GameWindow;
import client.windows.ScoreWindow;
import client.windows.StartWindow;
import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;

public class WindowManager {
    private StartWindow startWindow;
    private GameWindow gameWindow;
    private CategoryWindow categoryWindow;
    private String selectedCategory;
    private ScoreWindow scoreWindow;

    public WindowManager() {
        startWindow = new StartWindow();
        startWindow.setVisible(true);
        gameWindow = new GameWindow();
        gameWindow.setAnswerListener(selectedAnswer -> {
            System.out.println("Selected answer: " + selectedAnswer);
        });
        scoreWindow = new ScoreWindow();
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

    public boolean hasUserGivenUp() {
        return scoreWindow.hasUserGivenUp();
    }

    public void initScoreWindowData(int rounds, Player player, Player opponent) {
        scoreWindow.setRounds(rounds);
        scoreWindow.setPlayers(player, opponent);
        
    }
    public void initScoreWindow() {
        scoreWindow.initScoreWindow();
    }

    public ScoreWindow getScoreWindow() {
        return this.scoreWindow;
    }
}
