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
    UserDataManager userDataManager;
    private boolean isOutsideMainLoop = false;
    private Player currentPlayer;
    private boolean saveResponse;
    private ClientPreGameProtocol requestTemp;


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
                while (isOutsideMainLoop) {
                    Thread.sleep(100);
                }

                if (clientSocket.isClosed()) {
                    System.out.println("Socket closed");
                    return;
                }

                //Titta vad det är klienten vill göra
                ClientPreGameProtocol request = (ClientPreGameProtocol) inputStream.readObject();
                if (saveResponse) {
                    requestTemp = request;
                }
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
                        this.isOutsideMainLoop = true;
                        break;
                    case ClientPreGameProtocol.SEARCH_FOR_PLAYER:
                        this.isOutsideMainLoop = true;
                        findFriend();
                        break;
                    case ClientPreGameProtocol.SHOW_TOP_LIST:
                        sendListOfPlayersRanked();
                    case ClientPreGameProtocol.EXIT_CLIENT:
                        System.out.println("Closing socket");
                        clientSocket.close();
                        break;

                }
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
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

    private void findMatch() throws IOException {
        synchronized (gameServer) {

            //Hitta en annan client som väntar på att få spela
            List<ClientHandler> clients = gameServer.getClientHandlers();
            for (ClientHandler otherClient : clients) {
                if (otherClient != this && otherClient.isLookingForGame) {
                    otherClient.isLookingForGame = false;
                    this.isLookingForGame = false;
                    otherClient.isOutsideMainLoop = true;
                    outputStream.writeObject(ServerPreGameProtocol.GAME_START);
                    otherClient.getOutputStream().writeObject(ServerPreGameProtocol.GAME_START);
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
            isOutsideMainLoop = false;
            System.out.println("player not found");
            return;
        }

        synchronized (gameServer) {
            List<ClientHandler> clients = gameServer.getClientHandlers();
            for (ClientHandler otherClient : clients) {
                if (otherClient != this
                        && otherClient.currentPlayer != null
                        && otherClient.currentPlayer.getUsername().equals(friendName)
                        && !otherClient.isLookingForGame) {

                    otherClient.isOutsideMainLoop = true;
                    System.out.println("Sent friend invite");
                    otherClient.outputStream.writeObject(ServerPreGameProtocol.INVITE);
                    otherClient.getOutputStream().writeObject(ServerPreGameProtocol.FRIEND_INVITE);
                    otherClient.getOutputStream().writeObject(currentPlayer.getName());
                    otherClient.getOutputStream().flush();

                    otherClient.saveResponse();
                    ClientPreGameProtocol friendResponse =  otherClient.getResponse();
                    while (friendResponse == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        friendResponse = otherClient.getResponse();
                    }


                    if (friendResponse.equals(ClientPreGameProtocol.CLIENT_INVITE_ACCEPTED)) {
                        System.out.println("Sending accepted");
                        outputStream.writeObject(ServerPreGameProtocol.INVITE_RESPONSE);
                        outputStream.writeObject(ServerPreGameProtocol.INVITE_ACCEPTED);
                        this.isLookingForGame = false;
                        otherClient.isLookingForGame = false;
                        System.out.println("Starting game");
                        outputStream.writeObject(ServerPreGameProtocol.GAME_START);
                        otherClient.getOutputStream().writeObject(ServerPreGameProtocol.GAME_START);
                        gameServer.startGame(this, otherClient);
                        System.out.println("Game started");
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

    private ClientPreGameProtocol getResponse() {
        return requestTemp;
    }

    private void saveResponse() {
        this.saveResponse = true;
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

    public void setOutsideMainLoop(boolean outsideMainLoop) {
        isOutsideMainLoop = outsideMainLoop;
    }
}