package client.network;


import client.WindowManager;
import server.entity.Player;
import server.entity.Question;
import server.network.GameSessionProtocol;
import server.network.ServerPreGameProtocol;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class NetworkHandler {
    private WindowManager windowManager;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public NetworkHandler(WindowManager windowManager) {
        this.windowManager = windowManager;
        windowManager.setNetworkHandler(this);
        windowManager.showWelcomeWindow();

        int port = 55566;
        String ip = "127.0.0.1";


        try {
            
            Socket socket = new Socket(ip, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Okänd värd: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fel vid anslutning eller dataläsning: " + e.getMessage());
            e.printStackTrace();
        }


        //TODO Koppla till start random game knapp i GUI


        //TODO registrera metod

        //TODO logga in metod

        //TODO Topplista metod

    }


    public void startRandomGame(Player currentPlayer) {
        //Berätta för server att vi vill spela mot en random spelare
        try {

            System.out.println(currentPlayer.getName() + " wants to start game");
            outputStream.writeObject(ClientPreGameProtocol.START_RANDOM_GAME);
            outputStream.flush();

            //Vänta på att server svarar
            GameSessionProtocol serverMessage = (GameSessionProtocol) inputStream.readObject();
            if (serverMessage.equals(GameSessionProtocol.WAITING_FOR_OPPONENT)) {
                System.out.println("Waiting for an opponent...");
            }
            
            
            //Skickar spelaren till servern efter svar från server
            outputStream.writeObject(currentPlayer);
            outputStream.flush();

            //Servern meddelar att en spelare är hittad och spelet startar
            serverMessage = (GameSessionProtocol) inputStream.readObject();
            if (serverMessage.equals(GameSessionProtocol.GAME_START)) {
                System.out.println("Game starting!");
            }

            Object serverResponse = inputStream.readObject();
            if (serverResponse.equals(GameSessionProtocol.SEND_SCORE_WINDOW_DATA)) {
                int rounds = (Integer) inputStream.readObject();
                Player opponent = (Player) inputStream.readObject();
                windowManager.initScoreWindowData(rounds, currentPlayer, opponent);
                windowManager.initScoreWindow();
            }
            
            handleGameSession();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGameSession() {
        new Thread(() -> {
            try {
                while (true) {
                    if (windowManager.hasUserGivenUp()) {
                        sendGiveUpSignal(outputStream);
                        continue;
                    }
                    windowManager.nextRound();
                    GameSessionProtocol state = (GameSessionProtocol) inputStream.readObject();
                    // Kollar om skickat total rounds och skriver ut i konsolen antalet rundor
                    switch (state) {
                        case GameSessionProtocol.WAITING:
                            windowManager.showScoreWindow();
                            break;
                        case GameSessionProtocol.SENT_CATEGORY:
                            while (!windowManager.hasClickedPlay()) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handleCategorySelection(outputStream, inputStream);
                            break;
                        case GameSessionProtocol.SENT_QUESTIONS:
                            while (!windowManager.hasClickedPlay()) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            handleQuestionRound(outputStream, inputStream);
                            windowManager.setHasClickedPlay(false);
                            break;
                        case GameSessionProtocol.SENT_ROUND_SCORE:
                            List<Integer> opponentScore = (ArrayList<Integer>) inputStream.readObject();
                            windowManager.updateOpponentScore(opponentScore);
                            break;
                        case GameSessionProtocol.GAME_OVER:
                            System.out.println("Game over");
                            outputStream.flush();
                            String resultat = (String) inputStream.readObject();
                            System.out.println(resultat);
                            break;
                        case GameSessionProtocol.PLAYER_GAVE_UP:
                            String message = (String) inputStream.readObject();
                            System.out.println(message);
                    }
                }
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }).start();
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
        windowManager.setSelectedCategory(null);
    }

    private void sendGiveUpSignal(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(GameSessionProtocol.CLIENT_GAVE_UP);
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
        System.out.println(scoreList.size());
        windowManager.updatePlayerScore(scoreList);

        //TODO skapa metod för att updatea GUI med rondresultat

        windowManager.showScoreWindow();
        //Skickar antal rätt till server
        outputStream.writeObject(scoreList);
        outputStream.flush();
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
}

