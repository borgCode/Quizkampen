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
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            //Skickar spelaren till servern
            out.writeObject(windowManager.getPlayer());


            while (true) {
                Protocol state = (Protocol) in.readObject();
                if (state.equals(Protocol.WAITING)) {
                    System.out.println("Väntar på andra spelaren");
                } else if (state.equals(Protocol.SENT_CATEGORY)) {
                    // Läser in kategorier från servern
                    ArrayList<String> categories = (ArrayList<String>) in.readObject();

                    // Använder WindowManager till att visa CategoryWindow
                    windowManager.showCategoryWindow(categories);



//                    // Visar kategorier i CategoryWindow
//                    //TODO Använda windowmanager istället för att skicka kategorier
//                    CategoryWindow categoryWindow = new CategoryWindow(categories);
//                    categoryWindow.setVisible(true);

                    // Vänta tills användaren väljer en kategori
                    while (windowManager.getSelectedCategory() == null) {
                        Thread.sleep(100);
                    }

                    // Skicka vald kategori till servern
                    String selectedCategory = windowManager.getSelectedCategory();
                    out.writeObject(selectedCategory);
                    out.flush();

                } else if (state.equals(Protocol.SENT_QUESTIONS)) {
                    windowManager.resetRound();
                    
                    //Läs in tre frågor
                    ArrayList<Question> questions = (ArrayList<Question>) in.readObject();
                    
                    List<Integer> scoreList = new ArrayList<>();
                    
                    windowManager.setGameWindowVisibility(true);
                    
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

                        while (!windowManager.getGameWindow().isHasAnswered()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        windowManager.getGameWindow().setHasAnswered(false);
                        
                        
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    
}

