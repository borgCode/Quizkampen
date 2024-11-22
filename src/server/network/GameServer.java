package server.network;

import server.data.UserDataManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private final List<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        new GameServer();
    }

    public GameServer() {
        
        try (ServerSocket serverSocket = new ServerSocket(55566)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                synchronized (clientHandlers) {
                    clientHandlers.add(clientHandler);
                }
                new Thread(clientHandler).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void startGame(ClientHandler player1, ClientHandler player2) {
        GameSession gameSession = new GameSession(player1, player2);
        new Thread (gameSession).start();
    }
}