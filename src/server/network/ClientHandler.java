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
    private boolean isLookingForGame;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final UserDataManager userDataManager;
    private Player currentPlayer;

    
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
                ClientPreGameProtocol request = (ClientPreGameProtocol) inputStream.readObject();
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
                        this.isLookingForGame = true;
                        findMatch();
                        return;
                    case ClientPreGameProtocol.SEARCH_FOR_PLAYER:
                        findFriend();
                        return;
                    case ClientPreGameProtocol.SHOW_TOP_LIST:
                        sendListOfPlayersRanked();

                }
            }
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    


    private void registerUser() throws IOException, ClassNotFoundException {

        Player newPlayer = (Player) inputStream.readObject();

        // Kontrollerar om användarnamnet redan finns
        if (userDataManager.doesUsernameExist(newPlayer.getUsername())) {
            outputStream.writeObject(ServerPreGameProtocol.REGISTER_FAIL_USERNAME_TAKEN);
            outputStream.flush();
            return;
        }
        boolean success = userDataManager.registerNewUser(newPlayer);
        if (success) {
            outputStream.writeObject(ServerPreGameProtocol.REGISTER_SUCCESS);
        } else {
            outputStream.writeObject(ServerPreGameProtocol.REGISTER_FAIL);
        }
        outputStream.flush();
    }

    private void loginUser() throws IOException, ClassNotFoundException {
        String[] nameAndPass = (String[]) inputStream.readObject();
        Player player = userDataManager.authenticatePlayer(nameAndPass[0], nameAndPass[1]);
        if (player != null) {
            this.currentPlayer = player;
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
                if (otherClient != this && otherClient.isLookingForGame) {
                    otherClient.isLookingForGame = false;
                    this.isLookingForGame = false;
                    gameServer.startGame(this, otherClient);
                    return;
                }
            }
        }
    }
    private void findFriend() throws IOException, ClassNotFoundException {
        String friendName = (String) inputStream.readObject();
        
        Player friendPlayer = userDataManager.getPlayerByUsername(friendName);
        if (friendPlayer == null) {
            outputStream.writeObject(ServerPreGameProtocol.PLAYER_NOT_FOUND);
            outputStream.flush();
            return;
        }
        
        synchronized (gameServer) {
            List<ClientHandler> clients = gameServer.getClientHandlers();
            for (ClientHandler otherClient : clients) {
                if (otherClient != this 
                        && otherClient.currentPlayer != null 
                        && otherClient.currentPlayer.getUsername().equals(friendName) 
                        && !otherClient.isLookingForGame) {
                    otherClient.getOutputStream().writeObject(ServerPreGameProtocol.INVITE);
                    otherClient.getOutputStream().writeObject(ServerPreGameProtocol.FRIEND_INVITE);
                    otherClient.getOutputStream().writeObject(currentPlayer.getName());
                    otherClient.getOutputStream().flush();
                    
                    String friendResponse = (String) otherClient.getInputStream().readObject();
                    if (friendResponse.equals(ClientPreGameProtocol.CLIENT_INVITE_ACCEPTED)) {
                        outputStream.writeObject(ServerPreGameProtocol.INVITE_ACCEPTED);
                        this.isLookingForGame = false;
                        otherClient.isLookingForGame = false;
                        System.out.println("Starting game iwth friend");
                        gameServer.startGame(this, otherClient);
                        return;
                    } else {
                        outputStream.writeObject(ServerPreGameProtocol.INVITE_REJECTED);
                        outputStream.flush();
                        return;
                    }
                    
                }
            }
            
        }
        outputStream.writeObject(ServerPreGameProtocol.PLAYER_NOT_AVAILABLE);
        outputStream.flush();
        
        
    }
    private void sendListOfPlayersRanked() throws IOException {
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
