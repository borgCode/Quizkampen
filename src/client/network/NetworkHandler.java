package client.network;


import client.WindowManager;
import server.entity.Player;
import server.entity.Question;
import server.network.GameSessionProtocol;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class NetworkHandler {
    private WindowManager windowManager;

    public NetworkHandler(WindowManager windowManager) {
        this.windowManager = windowManager;

        int port = 55566;
        String ip = "127.0.0.1";
        

        //Den här behövs för att inte skicka iväg null objekt med klient 2 - kanske finns något annat sätt att lösa detta på?
        while (windowManager.getPlayer() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try (Socket socket = new Socket(ip, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            
            
            //TODO Koppla till start random game knapp i GUI
            startRandomGame(out, in);
            
            //TODO registrera metod
            
            //TODO logga in metod
            
            //TODO Topplista metod
            


            while (true) {
                if (windowManager.hasUserGivenUp()) {
                    sendGiveUpSignal(out);
                    continue;
                }
                windowManager.nextRound();
                GameSessionProtocol state = (GameSessionProtocol) in.readObject();
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
                        handleCategorySelection(out, in);
                        break;
                    case GameSessionProtocol.SENT_QUESTIONS:
                        while (!windowManager.hasClickedPlay()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handleQuestionRound(out, in);
                        windowManager.setHasClickedPlay(false);
                        break;
                    case GameSessionProtocol.SENT_ROUND_SCORE:
                        List<Integer> opponentScore = (ArrayList<Integer>) in.readObject();
                        windowManager.updateOpponentScore(opponentScore);
                        break;
                    case GameSessionProtocol.GAME_OVER:
                        System.out.println("Game over");
                        out.flush();
                        String resultat = (String) in.readObject();
                        System.out.println(resultat);
                        break;
                    case GameSessionProtocol.PLAYER_GAVE_UP:
                        String message = (String) in.readObject();
                        System.out.println(message);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Okänd värd: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fel vid anslutning eller dataläsning: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startRandomGame(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        //Berätta för server att vi vill spela mot en random spelare
        out.writeObject(ClientPreGameProtocol.START_RANDOM_GAME);
        out.flush();

        //Vänta på att server svarar
        GameSessionProtocol serverMessage = (GameSessionProtocol) in.readObject();
        if (serverMessage.equals(GameSessionProtocol.WAITING_FOR_OPPONENT)) {
            System.out.println("Waiting for an opponent...");
        }


        //Skickar spelaren till servern efter svar från server
        out.writeObject(windowManager.getPlayer());
        out.flush();

        //Servern meddelar att en spelare är hittad och spelet startar
        serverMessage = (GameSessionProtocol) in.readObject();
        if (serverMessage.equals(GameSessionProtocol.GAME_START)) {
            System.out.println("Game starting!");
        }

        Object serverResponse = in.readObject();
        if (serverResponse.equals(GameSessionProtocol.SEND_SCORE_WINDOW_DATA)) {
            int rounds = (Integer) in.readObject();
            Player opponent = (Player) in.readObject();
            windowManager.initScoreWindowData(rounds, windowManager.getPlayer(), opponent);
            windowManager.initScoreWindow();
        }
    }


    private void handleCategorySelection(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException, InterruptedException {
        // Läser in kategorier från servern
        ArrayList<String> categories = (ArrayList<String>) in.readObject();

        // Använder WindowManager till att visa CategoryWindow
        windowManager.showCategoryWindow(categories);


        // Vänta tills användaren väljer en kategori
        while (windowManager.getSelectedCategory() == null) {
            if (windowManager.hasUserGivenUp()) {
                sendGiveUpSignal(out);
                return;
            }
            Thread.sleep(100);
        }

        /*// Skicka vald kategori till servern
        out.writeObject(windowManager.getSelectedCategory());
        out.flush();
        windowManager.setSelectedCategory(null);*/

        //TODO:NYTT
        String selectedCategory = windowManager.getSelectedCategory();
        if (selectedCategory == null) {
            System.err.println("Ingen kategori vald!");
            return;
        }

        int currentRound = windowManager.getCurrentRound();
        windowManager.updateRoundCategory(currentRound, selectedCategory);

        // Uppdatera alla ronder i ScoreWindow
        windowManager.updateAllRoundsInScoreWindow();

        // Skriv ut alla kategorier för felsökning
        windowManager.printAllCategoriesFromScoreWindow();

        out.writeObject(selectedCategory); // Skicka kategori till servern
        windowManager.setSelectedCategory(null); // Återställ vald kategori
    }

    private void sendGiveUpSignal(ObjectOutputStream out) throws IOException {
        out.writeObject(GameSessionProtocol.CLIENT_GAVE_UP);
    }

    private void handleQuestionRound(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        windowManager.resetRound();

        //Läs in tre frågor
        ArrayList<Question> questions = (ArrayList<Question>) in.readObject();

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
        out.writeObject(scoreList);
        out.flush();
    }
}

