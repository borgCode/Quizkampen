package server.network;

import client.network.ClientPreGameProtocol;
import server.data.UserDataManager;
import server.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameServer gameServer;
    private boolean isInGame;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    UserDataManager userDataManager;
    
    
    public ClientHandler(Socket clientSocket, GameServer gameServer) {
        this.clientSocket = clientSocket;
        this.gameServer = gameServer;
        userDataManager = UserDataManager.getInstance();
        
        
    }

    @Override
    public void run() {
        try {
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
        
            
            while (true) {
                //Titta vad det är klienten vill göra
                System.out.println("Receiving request");
                ClientPreGameProtocol request = (ClientPreGameProtocol) inputStream.readObject();
                System.out.println(request);
                if (request == null) {
                    break;
                }
                
                switch (request) {
                    case ClientPreGameProtocol.REGISTER_USER:
                        registerUser();
                        break;
                    case ClientPreGameProtocol.LOGIN_USER:
                        loginUser();
                        break;
                    case ClientPreGameProtocol.START_RANDOM_GAME:
                        findMatch();
                        return;
                    case ClientPreGameProtocol.SEARCH_FOR_PLAYER:
                    case ClientPreGameProtocol.SHOW_TOP_LIST:
                        sendListOfAllPlayers();
                        
                }
            }
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    
    private void registerUser() throws IOException, ClassNotFoundException {
        Player newPlayer = (Player) inputStream.readObject();
        if (userDataManager.registerNewUser(newPlayer)) {
            outputStream.writeObject(ServerPreGameProtocol.REGISTER_SUCCESS);
        } else {
            outputStream.writeObject(ServerPreGameProtocol.REGISTER_FAIL);
        }
    }

    private void loginUser() throws IOException, ClassNotFoundException {
        String[] nameAndPass = (String[]) inputStream.readObject();
        Player player = userDataManager.authenticatePlayer(nameAndPass[0], nameAndPass[1]);
        if (player != null) {
            outputStream.writeObject(ServerPreGameProtocol.LOGIN_SUCCESS);
            outputStream.writeObject(player);
            outputStream.flush();
        } else {
            outputStream.writeObject(ServerPreGameProtocol.LOGIN_FAIL);
            outputStream.flush();
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

    private void sendListOfAllPlayers() throws IOException {
        ArrayList<Player> allPLayers = userDataManager.getAllPlayersRanked();
        if (allPLayers.isEmpty()) {
            outputStream.writeObject(ServerPreGameProtocol.NO_REGISTERED_PLAYERS);
        } else {
            outputStream.writeObject(ServerPreGameProtocol.TOP_LIST_SENT);
            outputStream.writeObject(userDataManager.getAllPlayersRanked());
            outputStream.flush();
        }
    }
    
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }
}
