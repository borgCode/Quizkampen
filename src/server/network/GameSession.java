package server.network;

import server.entity.Game;
import server.entity.Player;
import server.entity.Question;
import server.logic.GameLogic;
import server.logic.QuestionBank;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameSession extends Thread {

    private final Socket player1Socket;
    private final Socket player2Socket;
    private final QuestionBank questionBank;


    public GameSession(Socket player1Socket, Socket player2Socket) {
        System.out.println("Två spelare anslutna");
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
        this.questionBank = new QuestionBank();
    }

    public void run() {
        try (
                ObjectOutputStream outPlayer1 = new ObjectOutputStream(player1Socket.getOutputStream());
                ObjectInputStream inPlayer1 = new ObjectInputStream(player1Socket.getInputStream());
                ObjectOutputStream outPlayer2 = new ObjectOutputStream(player2Socket.getOutputStream());
                ObjectInputStream inPlayer2 = new ObjectInputStream(player2Socket.getInputStream())
        ) {

            

            //Hämta spelar objekt från båda klienterna
            Player player1 = (Player) inPlayer1.readObject();
            Player player2 = (Player) inPlayer2.readObject();
            
            //Lägg streamsen och players i arrays som används i loop för att växla mellan dem
            ObjectOutputStream[] outputStreams = {outPlayer1, outPlayer2};
            ObjectInputStream[] inputStreams = {inPlayer1, inPlayer2};
            Player[] players = {player1, player2};
            int[] roundScores = new int[2];
            
            while (true) {


                Game game = new Game(player1, player2);
                HashMap<String, Integer> roundResults = new HashMap<>();
                
                //Berätta för klienten vilken state den är i
                outPlayer2.writeObject(Protocol.WAITING);
                outPlayer1.writeObject(Protocol.SENT_CATEGORY);

                ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV", "Spel", "Mat", "Literatur", "Sport"));

                System.out.println("List size innan remove: " + categories.size());

                //Hämta random lista med 3 kategorier
                ArrayList<String> randomCategories = GameLogic.getRandomCategories(categories);
                
                //Skickar tre kategorier till client
                outPlayer1.writeObject(randomCategories);
                outPlayer1.flush();

                //Tar emot client svar på kategori
                String categoryInput = (String) inPlayer1.readObject();

                //Ta bort kategorin som redan är spelad
                GameLogic.removeCategoryFromList(categories, categoryInput);

                System.out.println("List size efter remove: " + categories.size());
                
                //Hämta random frågor från questionBank
                ArrayList<Question> questions = questionBank.getRandomQuestionsByCategory(categoryInput.toLowerCase());

                outPlayer1.writeObject(Protocol.SENT_QUESTIONS);
                outPlayer1.writeObject(questions);
                outPlayer1.flush();


                //Tar emot hur många rätt använadaren hade
                roundScores[0] = (int) inPlayer1.readObject();
                game.incrementScore(player1, roundScores[0]);
                

                System.out.println("Spelaren hade " + roundScores[0] + " rätt");


                outPlayer1.writeObject(Protocol.WAITING);
                
                int currentPlayer = 1;

                for (int i = 0; i < 5; i++) {

                    outputStreams[currentPlayer].writeObject(Protocol.SENT_QUESTIONS);
                    outputStreams[currentPlayer].writeObject(questions);
                    outputStreams[currentPlayer].flush();

                    //Tar emot hur många rätt använadaren hade
                    roundScores[currentPlayer] = (int) (inputStreams[currentPlayer].readObject());
                    game.incrementScore(players[currentPlayer], roundScores[currentPlayer]);
                    
                    //Lägga in båda spelarnas scores i hashmap för att skicka båda tillsammans
                    roundResults.put(players[(currentPlayer + 1) % 2].getName(), roundScores[(currentPlayer + 1) % 2]);
                    roundResults.put(players[currentPlayer].getName(), roundScores[currentPlayer]);

                    //Skicka rondresultat till båda spelarna
                    outputStreams[currentPlayer].writeObject(Protocol.SENT_ROUND_SCORE);
                    outputStreams[currentPlayer].writeObject(roundResults);
                    outputStreams[currentPlayer].flush();
                    
                    outputStreams[(currentPlayer + 1) % 2].writeObject(Protocol.SENT_ROUND_SCORE);
                    outputStreams[(currentPlayer + 1) % 2].writeObject(roundResults);
                    outputStreams[(currentPlayer + 1) % 2].flush();
                    
                    outputStreams[currentPlayer].writeObject(Protocol.SENT_CATEGORY);
                    
                    //Hämta random lista med 3 kategorier
                    randomCategories = GameLogic.getRandomCategories(categories);

                    //Skickar tre kategorier till client
                    outputStreams[currentPlayer].writeObject(randomCategories);
                    outputStreams[currentPlayer].flush();

                    //Tar emot client svar på kategori
                    categoryInput = (String) inputStreams[currentPlayer].readObject();

                    //Ta bort kategorin som redan är spelad
                    GameLogic.removeCategoryFromList(categories, categoryInput);

                    //Hämta random frågor från questionBank
                    questions = questionBank.getRandomQuestionsByCategory(categoryInput.toLowerCase());

                    outputStreams[currentPlayer].writeObject(Protocol.SENT_QUESTIONS);
                    outputStreams[currentPlayer].writeObject(questions);
                    outputStreams[currentPlayer].flush();

                    int score2 = (int) (inputStreams[currentPlayer].readObject());
                    game.incrementScore(players[currentPlayer], score2);
                    
                    outputStreams[currentPlayer].writeObject(Protocol.WAITING);
                    
                    currentPlayer = (currentPlayer + 1) % 2;
                }
                

                outPlayer1.writeObject(Protocol.SENT_QUESTIONS);
                outPlayer1.writeObject(questions);
                outPlayer1.flush();

                //Tar emot hur många rätt använadaren hade
                int scoreLastRound = (int) inPlayer1.readObject();
                game.incrementScore(player1, scoreLastRound);

                outPlayer1.writeObject(Protocol.WAITING);
                
                //TODO end game logic
                
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



