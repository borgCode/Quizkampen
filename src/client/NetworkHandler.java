package client;


import server.entity.Player;
import server.entity.Question;
import server.network.Protocol;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class NetworkHandler {
    private WindowManager windowManager;

    NetworkHandler(WindowManager windowManager) {
        this.windowManager = windowManager;

        int port = 55566;
        String ip = "127.0.0.1";

        String name;
        ImageIcon avatar;


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

            //Skickar spelaren till servern
            out.writeObject(windowManager.getPlayer());

            Object serverResponse = in.readObject();
            if (serverResponse.equals(Protocol.SEND_SCORE_WINDOW_DATA)) {
                int rounds = (Integer) in.readObject();
                Player opponent = (Player) in.readObject();
                windowManager.initScoreWindowData(rounds, windowManager.getPlayer(), opponent);
                windowManager.initScoreWindow();
            }


            while (true) {
                if (windowManager.hasUserGivenUp()) {
                    sendGiveUpSignal(out);
                    continue;
                }
                windowManager.nextRound();
                Protocol state = (Protocol) in.readObject();
                // Kollar om skickat total rounds och skriver ut i konsolen antalet rundor
                switch (state) {
                    case Protocol.WAITING:
                        windowManager.showScoreWindow();
                        break;
                    case Protocol.SENT_CATEGORY:
                        handleCategorySelection(out, in);
                        break;
                    case Protocol.SENT_QUESTIONS:
                        handleQuestionRound(out, in);
                        break;
                    case Protocol.SENT_ROUND_SCORE:
                        List<Integer> opponentScore = (ArrayList<Integer>) in.readObject();
                        windowManager.updateOpponentScore(opponentScore);
                        break;
                    case Protocol.GAME_OVER:
                        System.out.println("Game over");
                        out.flush();
                        String resultat = (String) in.readObject();
                        System.out.println(resultat);
                        break;
                    case Protocol.PLAYER_GAVE_UP:
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

        // Skicka vald kategori till servern
        out.writeObject(windowManager.getSelectedCategory());
        out.flush();
        windowManager.setSelectedCategory(null);
    }

    private void sendGiveUpSignal(ObjectOutputStream out) throws IOException {
        out.writeObject(Protocol.CLIENT_GAVE_UP);
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

