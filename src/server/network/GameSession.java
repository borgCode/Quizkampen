package server.network;

import server.entity.Game;
import server.entity.Player;
import server.entity.Question;
import server.logic.GameLogic;
import server.logic.QuestionBank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameSession extends Thread {

    private final Socket player1Socket;
    private final Socket player2Socket;
    private final QuestionBank questionBank;
    private ArrayList<Question> currentQuestions;
    private String selectedCategory;
    private ArrayList<Integer> opponentRoundScore;


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

            while (true) {


                Game game = new Game(player1, player2);


                //Berätta för klienten vilken state den är i
                outPlayer2.writeObject(Protocol.WAITING);
                outPlayer1.writeObject(Protocol.SENT_CATEGORY);

                ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV", "Spel", "Mat", "Literatur", "Sport"));

                
                handleCategorySelection(outPlayer1, inPlayer1, categories);

                //Hämta random frågor från questionBank
                currentQuestions = questionBank.getRandomQuestionsByCategory(selectedCategory.toLowerCase());

                processQuestions(outPlayer1, inPlayer1, game, player1);
                
                outPlayer1.writeObject(Protocol.WAITING);
                outPlayer1.flush();

                int currentPlayer = 1;

                for (int i = 0; i < 5; i++) {

                    
                    processQuestions(outputStreams[currentPlayer], inputStreams[currentPlayer], game, players[currentPlayer]);

                    //Skicka resultat till andra spelaren
                    outputStreams[(currentPlayer + 1) % 2].writeObject(Protocol.SENT_ROUND_SCORE);
                    outputStreams[(currentPlayer + 1) % 2].writeObject(opponentRoundScore);
                    outputStreams[(currentPlayer + 1) % 2].flush();

                    outputStreams[currentPlayer].writeObject(Protocol.SENT_CATEGORY);
                    
                    handleCategorySelection(outputStreams[currentPlayer], inputStreams[currentPlayer], categories);
                    

                    //Hämta random frågor från questionBank
                    currentQuestions = questionBank.getRandomQuestionsByCategory(selectedCategory.toLowerCase());

                    processQuestions(outputStreams[currentPlayer], inputStreams[currentPlayer], game, players[currentPlayer]);
                    

                    outputStreams[currentPlayer].writeObject(Protocol.WAITING);

                    currentPlayer = (currentPlayer + 1) % 2;
                }


                outPlayer1.writeObject(Protocol.SENT_QUESTIONS);
                outPlayer1.writeObject(currentQuestions);
                outPlayer1.flush();

                //Tar emot hur många rätt använadaren hade
                opponentRoundScore = (ArrayList<Integer>) inPlayer1.readObject();
                game.incrementScore(player1, opponentRoundScore);

                //outPlayer1.writeObject(Protocol.WAITING);

                //TODO end game logic

                outPlayer1.writeObject(Protocol.GAME_OVER);
                outPlayer2.writeObject(Protocol.GAME_OVER);


                GameLogic.findWinner(outPlayer1,outPlayer2,game.getPlayer1Score(),game.getPlayer2Score());
                
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private void handleCategorySelection(ObjectOutputStream outputStream, ObjectInputStream inputStream, ArrayList<String> categories) throws IOException, ClassNotFoundException {
        //Hämta random lista med 3 kategorier
        ArrayList<String> randomCategories = GameLogic.getRandomCategories(categories);

        //Skickar tre kategorier till client
        outputStream.writeObject(randomCategories);
        outputStream.flush();

        //Tar emot client svar på kategori
        String categoryInput = (String) inputStream.readObject();

        //Ta bort kategorin som redan är spelad
        GameLogic.removeCategoryFromList(categories, categoryInput);

        selectedCategory = categoryInput;
    }

    private void processQuestions(ObjectOutputStream outputStream, ObjectInputStream inputStream, Game game, Player player1) throws IOException, ClassNotFoundException {
        
        outputStream.writeObject(Protocol.SENT_QUESTIONS);
        outputStream.writeObject(currentQuestions);
        outputStream.flush();


        //Tar emot hur många rätt använadaren hade
        opponentRoundScore = (ArrayList<Integer>) inputStream.readObject();
        game.incrementScore(player1, opponentRoundScore);
    }
}


