package client;


import server.entity.Question;
import server.network.Protocol;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


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
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            //Skickar spelaren till servern
            out.writeObject(windowManager.getPlayer());


            while (true) {
                Protocol state = (Protocol) in.readObject();
                if (state.equals(Protocol.WAITING)) {
                    System.out.println("Vänta på andra spelaren");
                } else if (state.equals(Protocol.SENT_CATEGORY)) {
                    // Läser in kategorier
                    System.out.println(in.readObject());
                    out.writeObject(userInput.readLine());
                    out.flush();

                } else if (state.equals(Protocol.SENT_QUESTIONS)) {
                    //Läs in tre frågor
                    ArrayList<Question> questions = (ArrayList<Question>) in.readObject();

                    Scanner scanner = new Scanner(System.in);
                    List<Integer> scoreList = new ArrayList<>();
                    
                    windowManager.setGameWindowVisibility(true);
                    
                    for (Question question : questions) {
                        windowManager.displayQuestion(question);
                        
//                        System.out.println(question.getQuestion());
//                        System.out.println(Arrays.toString(question.getOptions()));
//                        String userAnswer = scanner.next();
//                        if (userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
//                            scoreList.add(1);
//                        } else {
//                            scoreList.add(0);
//                        }
                    }

                    //TODO skapa metod för att updatea GUI med rondresultat


                    //Skickar antal rätt till server
                    out.writeObject(scoreList);
                    out.flush();

                } else if (state.equals(Protocol.SENT_ROUND_SCORE)) {
                    List<Integer> opponentScore = (ArrayList<Integer>) in.readObject();
                    for (Integer score : opponentScore) {
                        System.out.println(score);
                    }
                } else if (state.equals(Protocol.GAME_OVER)) {
                    System.out.println("Game over");
                    out.flush();
                    String resultat = (String) in.readObject();
                    System.out.println(resultat);
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Okänd värd: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fel vid anslutning eller dataläsning: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
}

