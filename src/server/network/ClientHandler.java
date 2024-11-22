package server.network;

import client.network.ClientPreGameProtocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameServer gameServer;
    private boolean isInGame;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    
    public ClientHandler(Socket clientSocket, GameServer gameServer) {
        this.clientSocket = clientSocket;
        this.gameServer = gameServer;
        
    }

    @Override
    public void run() {
        try {
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
        
            
            while (true) {
                //Titta vad det är klienten vill göra
                ClientPreGameProtocol request = (ClientPreGameProtocol) inputStream.readObject();
                if (request == null) {
                    break;
                }
                
                switch (request) {
                    case ClientPreGameProtocol.REGISTER_USER:
                        
                    case ClientPreGameProtocol.LOGIN_USER:
                        
                    case ClientPreGameProtocol.START_RANDOM_GAME:
                        System.out.println("Server received " + ClientPreGameProtocol.START_RANDOM_GAME);
                        findMatch();
                        return;
                    case ClientPreGameProtocol.SEARCH_FOR_PLAYER:
                        
                }
            }
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void findMatch() {
        synchronized (gameServer) {
            
            //Hitta en annan client som väntar på att få spela
            List<ClientHandler> clients = gameServer.getClientHandlers();
            for (ClientHandler otherClient : clients) {
                if (otherClient != this && !otherClient.isInGame) {
                    otherClient.isInGame = true;
                    this.isInGame = true;
                    gameServer.startGame(this, otherClient);
                    return;
                }
            }
        }
    }
    

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }
}
