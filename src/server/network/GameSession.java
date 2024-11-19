package server.network;

import server.entity.Game;
import server.entity.Player;
import server.entity.Question;
import server.entity.Round;
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
            int currentPlayer = 0;

            while (true) {
                Game game = new Game(player1, player2);

                //Berätta för klient 2 att den ska vänta på andra spelarens tur
                outPlayer2.writeObject(Protocol.WAITING);
                ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV-serier", "TV-spel", "Mat", "Litteratur", "Sport"));
                for (int i = 0; i < 6; i++){
                    // Låt currentplayer välja kategori
                    String selectedCategory = handleCategorySelection(outputStreams[currentPlayer], inputStreams[currentPlayer], categories);

                    //Hämta random frågor från questionBank och skapa Round object
                    Round round = new Round(questionBank.getRandomQuestionsByCategory(selectedCategory.toLowerCase()), selectedCategory);
                    // Spelaren som vlade kategori får svara
                    processQuestions(outputStreams[currentPlayer], inputStreams[currentPlayer], game, players[currentPlayer], round);
                    sendResultsToOpponent(outputStreams[(currentPlayer + 1) % 2], round);

                    // Andra spelaren får svara
                    processQuestions(outputStreams[(currentPlayer + 1) % 2], inputStreams[(currentPlayer +1) % 2], game, players[(currentPlayer + 1) % 2], round);
                    sendResultsToOpponent(outputStreams[currentPlayer], round);

                    // Informera om att vänta
                    outputStreams[currentPlayer].writeObject(Protocol.WAITING);
                    currentPlayer = (currentPlayer + 1) % 2;

                }



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

    


    private String handleCategorySelection(ObjectOutputStream outputStream, ObjectInputStream inputStream, ArrayList<String> categories) throws IOException, ClassNotFoundException {
        //Hämta random lista med 3 kategorier
        outputStream.writeObject(Protocol.SENT_CATEGORY);
        ArrayList<String> randomCategories = GameLogic.getRandomCategories(categories);

        //Skickar tre kategorier till client
        outputStream.writeObject(randomCategories);
        outputStream.flush();

        //Tar emot client svar på kategori
        String categoryInput = (String) inputStream.readObject();

        //Ta bort kategorin som redan är spelad
        GameLogic.removeCategoryFromList(categories, categoryInput);
        
        return categoryInput;
    }

    private void processQuestions(ObjectOutputStream outputStream, ObjectInputStream inputStream, Game game, Player player1, Round round) throws IOException, ClassNotFoundException {
        
        outputStream.writeObject(Protocol.SENT_QUESTIONS);
        outputStream.writeObject(round.getCurrentQuestions());
        outputStream.flush();


        //Tar emot hur många rätt använadaren hade
        ArrayList<Integer> opponentRoundScore = (ArrayList<Integer>) inputStream.readObject();
        game.incrementScore(player1, opponentRoundScore);
        round.setOpponentRoundScore(opponentRoundScore);
    }

    private void sendResultsToOpponent(ObjectOutputStream outputStream, Round round) throws IOException {
        //Skicka resultat till andra spelaren
        outputStream.writeObject(Protocol.SENT_ROUND_SCORE);
        outputStream.writeObject(round.getOpponentRoundScore());
        outputStream.flush();
    }
}


