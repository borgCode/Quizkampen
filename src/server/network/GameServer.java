package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GameServer {
    private ArrayList<Socket> clientList;

    public GameServer() {
        clientList = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(55566)) {
            while (true) {
                Socket socketToClient = serverSocket.accept();
                clientList.add(socketToClient);
                if (clientList.size() == 2) {
                    GameSession gameSession = new GameSession(clientList.get(0), clientList.get(1));
                    gameSession.start();
                    clientList.clear();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
    }

}