package client;

import client.gui.WindowManager;
import client.network.NetworkHandler;

public class GameClient {

    public GameClient() {
        WindowManager windowManager = new WindowManager();
        new NetworkHandler(windowManager);
    }

    public static void main(String[] args) {
        new GameClient();
    }
}
