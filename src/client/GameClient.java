package client;

import client.gui.WindowManager;
import client.network.NetworkHandler;

public class GameClient {
    private NetworkHandler networkHandler;
    private WindowManager windowManager;

    public GameClient() {
        
        windowManager = new WindowManager();
        networkHandler = new NetworkHandler(windowManager);
    }

    public static void main(String[] args) {
        new GameClient();
    }
}
