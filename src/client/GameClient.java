package client;

import client.gui.WindowManager;
import client.network.NetworkHandler;

public class GameClient {
    
    //TODO Gör så fönstrern öppnar på varandra

    public GameClient() {
        WindowManager windowManager = new WindowManager();
        new NetworkHandler(windowManager);
    }

    public static void main(String[] args) {
        new GameClient();
    }
}
