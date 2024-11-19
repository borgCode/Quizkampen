package client;

import server.entity.Player;

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
    
    public Player getPlayer() {
        return startWindow.getPlayer();
    }
}
