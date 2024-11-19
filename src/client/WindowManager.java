package client;

import server.entity.Player;
import server.entity.Question;

import java.util.ArrayList;

public class WindowManager {
    private StartWindow startWindow;
    private GameWindow gameWindow;
    private CategoryWindow categoryWindow;

    public WindowManager() {
        startWindow = new StartWindow();
        startWindow.setVisible(true);
        gameWindow = new GameWindow();
        categoryWindow = new CategoryWindow();
    }
    
    public void setGameWindowVisibility(Boolean isVisible) {
        gameWindow.setVisible(isVisible);
    }
    public void setCategoryWindowVisibility(Boolean isVisible) {
        categoryWindow.setVisible(isVisible);
    }
    
    public Player getPlayer() {
        return startWindow.getPlayer();
    }

    

    public void displayQuestion(Question question) {
        gameWindow.updateQuestion(question);
    }
}
