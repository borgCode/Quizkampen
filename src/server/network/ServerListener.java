package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
    
    public ServerListener() {
        try (ServerSocket serverSocket = new ServerSocket(55566)) {
            while (true) {
                Socket socketToClient = serverSocket.accept();
                Handler clientHandler = new Handler(socketToClient);
                clientHandler.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ServerListener serverListener = new ServerListener();
    }
    
}
