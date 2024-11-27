package client.network;


import client.gui.WindowManager;
import server.entity.Player;
import server.entity.Question;
import server.network.ServerGameSessionProtocol;
import server.network.ServerPreGameProtocol;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class NetworkHandler {
    private WindowManager windowManager;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final int port = 55566;
    private String ip = "127.0.0.1";
    private ServerPreGameProtocol inviteResponseTemp;
    private boolean hasInitScoreWindow;
    private Socket socket;
    private boolean isRunning;

    public NetworkHandler(WindowManager windowManager) {
        this.windowManager = windowManager;
        windowManager.setNetworkHandler(this);
        windowManager.showWelcomeWindow();


        try {
            socket = new Socket(ip, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Okänd värd: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fel vid anslutning eller dataläsning: " + e.getMessage());
            e.printStackTrace();
        
            
        }

    }

    public void startMessageLoop() {
        new Thread(() -> {
            try {
                while (isRunning) {
                    ServerPreGameProtocol message = (ServerPreGameProtocol) inputStream.readObject();
                    System.out.println(message);
                    switch (message) {
                        case GAME_START:
                            startGame();
                            break;
                        case INVITE:
                            handleInviteMessage();
                            break;
                        case INVITE_RESPONSE:
                            ServerPreGameProtocol inviteResponse = (ServerPreGameProtocol) inputStream.readObject();
                            inviteResponseTemp = inviteResponse;
                            break;

                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



    public void startRandomGame(Player currentPlayer) {
        //Berätta för server att vi vill spela mot en random spelare
        try {

            System.out.println("Sending random game start");
            outputStream.writeObject(ClientPreGameProtocol.START_RANDOM_GAME);
            outputStream.flush();

            //Skickar spelaren till servern efter svar från server
            outputStream.writeObject(currentPlayer);
            outputStream.flush();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void startGame() throws IOException, ClassNotFoundException, InterruptedException {
        Object serverResponse = inputStream.readObject();
        if (serverResponse.equals(ServerGameSessionProtocol.SEND_SCORE_WINDOW_DATA)) {
            int rounds = (Integer) inputStream.readObject();
            Player opponent = (Player) inputStream.readObject();
            windowManager.initScoreWindowData(rounds, windowManager.getCurrentPlayer(), opponent);
            if (!hasInitScoreWindow) {
                windowManager.initScoreWindow();
            }

            hasInitScoreWindow = true;
            windowManager.showScoreWindow();
        }

        while (true) {
            if (windowManager.hasUserGivenUp()) {
                sendGiveUpSignal(outputStream);
                windowManager.switchBottomPanel();
                continue;
            }
            windowManager.nextRound();
            ServerGameSessionProtocol state = (ServerGameSessionProtocol) inputStream.readObject();
            System.out.println(state);
            switch (state) {
                case WAITING:
                    windowManager.showScoreWindow();
                    windowManager.setPlayButtonIsEnabled(false);
                    outputStream.flush();
                    break;
                case SENT_CATEGORY:
                    windowManager.setPlayButtonIsEnabled(true);
                    while (!windowManager.hasClickedPlay()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handleCategorySelection(outputStream, inputStream);
                    break;
                case ServerGameSessionProtocol.SENT_CATEGORY_TO_OPPONENT:
                    String currentCategory = (String) inputStream.readObject();
                    windowManager.setCurrentCategory(currentCategory);
                    windowManager.updateCategory();
                    break;
                case PRE_QUESTIONS_CHECK:
                    if(windowManager.hasUserGivenUp()) {
                        sendGiveUpSignal(outputStream);
                    } else {
                        outputStream.writeObject(ClientGameSessionProtocol.QUESTION_READY);
                    }
                    break;
                case ServerGameSessionProtocol.SENT_QUESTIONS:
                    windowManager.setPlayButtonIsEnabled(true);
                    while (!windowManager.hasClickedPlay()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handleQuestionRound(outputStream, inputStream);
                    windowManager.setPlayButtonIsEnabled(false);
                    windowManager.setHasClickedPlay(false);
                    break;
                case ServerGameSessionProtocol.SENT_ROUND_SCORE:
                    List<Integer> opponentScore = (ArrayList<Integer>) inputStream.readObject();
                    windowManager.updateOpponentScore(opponentScore);
                    break;
                case ServerGameSessionProtocol.GAME_OVER:
                    windowManager.switchBottomPanel();
                    windowManager.showScoreWindow();
                    String resultat = (String) inputStream.readObject();
                    JOptionPane.showMessageDialog(null, resultat);
                    break;
                case ServerGameSessionProtocol.PLAYER_GAVE_UP:
                    windowManager.switchBottomPanel();
                    windowManager.showScoreWindow();
                    String message = (String) inputStream.readObject();
                    JOptionPane.showMessageDialog(null, message);
                    break;
                case ServerGameSessionProtocol.PLAY_AGAIN_SUCCESS:
                    windowManager.resetScoreList();
                    windowManager.showScoreWindow();
                    break;
                case ServerGameSessionProtocol.PLAY_AGAIN_DENIED:
                    System.out.println("Andra spelaren avbröt");
                    windowManager.backToMenu();
                    break;
                case LEAVING_GAME:
                    System.out.println("Leaving game");
                    windowManager.resetScoreList();
                    return;
                
            }
        }


    }


    private void handleCategorySelection(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws
            IOException, ClassNotFoundException, InterruptedException {
        // Läser in kategorier från servern
        ArrayList<String> categories = (ArrayList<String>) inputStream.readObject();

        // Använder WindowManager till att visa CategoryWindow
        windowManager.showCategoryWindow(categories);


        // Vänta tills användaren väljer en kategori
        while (windowManager.getSelectedCategory() == null) {
            if (windowManager.hasUserGivenUp()) {
                sendGiveUpSignal(outputStream);
                return;
            }
            Thread.sleep(100);
        }

        // Skicka vald kategori till servern
        outputStream.writeObject(windowManager.getSelectedCategory());
        outputStream.flush();
        windowManager.setCurrentCategory(windowManager.getSelectedCategory());
        windowManager.updateCategory();
        windowManager.setSelectedCategory(null);
    }

    private void sendGiveUpSignal(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(ClientGameSessionProtocol.CLIENT_GAVE_UP);
        windowManager.setHasGivenUp(false);
    }

    private void handleQuestionRound(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws
            IOException, ClassNotFoundException {
        windowManager.resetRound();

        //Läs in tre frågor
        ArrayList<Question> questions = (ArrayList<Question>) inputStream.readObject();

        List<Integer> scoreList = new ArrayList<>();

        windowManager.showQuestionWindow();

        for (Question question : questions) {
            windowManager.displayQuestion(question);

            String[] selectedAnswer = new String[1];

            //Sync block så att bara en tråd kan modifiera selectedAnswer
            synchronized (selectedAnswer) {
                //Settar answerListener så vi får tillbaka användarens svar som sedan settas till selectedAnswer
                windowManager.getGameWindow().setAnswerListener(answer -> {
                    synchronized (selectedAnswer) {
                        selectedAnswer[0] = answer;
                        //När användaren har svarat och svaret har blivit uppdaterat här kan main tråden fortsätta
                        selectedAnswer.notify();
                    }
                });

                try {
                    selectedAnswer.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }


            }
            if (selectedAnswer[0] != null && selectedAnswer[0].equalsIgnoreCase(question.getCorrectAnswer())) {
                scoreList.add(1);
            } else
                scoreList.add(0);


            while (!windowManager.getGameWindow().isHasAnswered()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            windowManager.getGameWindow().setHasAnswered(false);

        }
        windowManager.updatePlayerScore(scoreList);

        windowManager.showScoreWindow();
        //Skickar antal rätt till server
        outputStream.writeObject(scoreList);
        outputStream.flush();
    }

    public boolean registerUser(String username, String password, String name, String avatarPath) {
        try {
            outputStream.writeObject(ClientPreGameProtocol.REGISTER_USER);
            outputStream.flush();

            // Skapa spelarobjekt och skicka till servern
            Player newPlayer = new Player(name, avatarPath);
            newPlayer.setPassword(password);
            newPlayer.setUsername(username);
            outputStream.writeObject(newPlayer);
            outputStream.flush();

            // Tar emot svar från servern
            ServerPreGameProtocol response = (ServerPreGameProtocol) inputStream.readObject();
            return response == ServerPreGameProtocol.REGISTER_SUCCESS;

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean loginUser(String username, String password) {
        try {
            outputStream.writeObject(ClientPreGameProtocol.LOGIN_USER);
            outputStream.writeObject(new String[]{username, password});
            outputStream.flush();

            ServerPreGameProtocol response = (ServerPreGameProtocol) inputStream.readObject();
            if (response.equals(ServerPreGameProtocol.LOGIN_SUCCESS)) {
                Player currentPlayer = (Player) inputStream.readObject();
                windowManager.initMenuWindow(currentPlayer);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return false;

    }

    public ArrayList<Player> getAllPLayersRanked() {
        try {
            outputStream.writeObject(ClientPreGameProtocol.SHOW_TOP_LIST);

            ServerPreGameProtocol response = (ServerPreGameProtocol) inputStream.readObject();
            if (response.equals(ServerPreGameProtocol.TOP_LIST_SENT)) {
                return (ArrayList<Player>) inputStream.readObject();
            } else if (response.equals(ServerPreGameProtocol.NO_REGISTERED_PLAYERS)) {
                return null;
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    public void sendPlayAgainSignal() {
        try {
            outputStream.writeObject(ClientGameSessionProtocol.PLAY_AGAIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendPlayAgainDenied() {
        try {
            outputStream.writeObject(ClientGameSessionProtocol.DENY_PLAY_AGAIN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int inviteFriendToPlay(String friendName) {
        try {
            outputStream.writeObject(ClientPreGameProtocol.SEARCH_FOR_PLAYER);
            outputStream.writeObject(friendName);
            outputStream.flush();

            ServerPreGameProtocol response = getInviteResponseTemp();
            while (response == null) {
                Thread.sleep(100);
                response = getInviteResponseTemp();
            }

            System.out.println("Invite saved response: " + response);
            if (response.equals(ServerPreGameProtocol.INVITE_ACCEPTED)) {
                System.out.println("Returning 0");
                return 0;
            } else if (response.equals(ServerPreGameProtocol.INVITE_REJECTED)) {
                System.out.println("Returning 1");
                return 1;
            } else if (response.equals(ServerPreGameProtocol.PLAYER_NOT_FOUND)) {
                System.out.println("Returning 2");
                return 2;
            } else {
                System.out.println("Returning 3");
                return 3;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private ServerPreGameProtocol getInviteResponseTemp() {
        return this.inviteResponseTemp;
    }

    public void startFriendGame(Player currentPlayer) {
        try {

            //Skickar spelaren till servern efter svar från server
            outputStream.writeObject(currentPlayer);
            outputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInviteMessage() throws IOException, ClassNotFoundException {

        ServerPreGameProtocol message = (ServerPreGameProtocol) inputStream.readObject();

        if (message.equals(ServerPreGameProtocol.FRIEND_INVITE)) {

            String opponentName = (String) inputStream.readObject();
            System.out.println(opponentName);

            int responseOption = JOptionPane.showConfirmDialog(
                    null,
                    opponentName + " har bjudit in dig till en match!",
                    "Inbjudan",
                    JOptionPane.YES_NO_OPTION
            );

            try {
                if (responseOption == JOptionPane.YES_OPTION) {
                    System.out.println("Accepting invite");
                    outputStream.writeObject(ClientPreGameProtocol.CLIENT_INVITE_ACCEPTED);

                    System.out.println("Starting window");
                    windowManager.getNetworkHandler().startFriendGame(windowManager.getCurrentPlayer());
                } else {
                    outputStream.writeObject(ClientPreGameProtocol.REJECT_INVITE);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendExitSignal()  {
        isRunning = false;
        try {
            outputStream.writeObject(ClientPreGameProtocol.EXIT_CLIENT);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
      
    }


}
