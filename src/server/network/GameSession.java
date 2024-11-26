package server.network;

import client.network.ClientGameSessionProtocol;
import server.data.QuestionManager;
import server.entity.Game;
import server.entity.Player;
import server.entity.Round;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSession implements Runnable {

    private final ClientHandler player1;
    private final ClientHandler player2;
    private final QuestionManager questionManager;
    private final int totalRounds;



    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.questionManager = QuestionManager.getInstance();
        // Sätter rundor på totalRounds från läsning av PropertiesManager
        this.totalRounds = PropertiesManager.totalRoundsSet();
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream outPlayer1 = player1.getOutputStream();
                ObjectInputStream inPlayer1 = player1.getInputStream();
                ObjectOutputStream outPlayer2 = player2.getOutputStream();
                ObjectInputStream inPlayer2 = player2.getInputStream();
        ) {
            
            
            //Hämta spelar objekt från båda klienterna
            Player player1 = (Player) inPlayer1.readObject();
            Player player2 = (Player) inPlayer2.readObject();
            
            //Lägg streamsen och players i arrays som används i loop för att växla mellan dem
            ObjectOutputStream[] outputStreams = {outPlayer1, outPlayer2};
            ObjectInputStream[] inputStreams = {inPlayer1, inPlayer2};
            Player[] players = {player1, player2};
            int currentPlayer = 0;


            sendScoreWindowData(player1, player2, outPlayer1, outPlayer2, totalRounds);
            
            
            mainLoop:
            while (true) {
                Game game = new Game(player1);

                //Berätta för klient 2 att den ska vänta på andra spelarens tur
                ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV-serier", "TV-spel", "Mat", "Litteratur", "Sport"));
                for (int i = 0; i < totalRounds; i++){
                    outputStreams[(currentPlayer + 1) % 2].writeObject(ServerGameSessionProtocol.WAITING);
                    outputStreams[(currentPlayer + 1) % 2].flush();
                    
                    
                    // Låt currentplayer välja kategori
                    String selectedCategory = handleCategorySelection(outputStreams[currentPlayer], inputStreams[currentPlayer], categories);
                    if (selectedCategory == null) {
                        handlePlayerGaveUp(currentPlayer,  outPlayer1, outPlayer2);
                        continue mainLoop;
                    }

                    outputStreams[(currentPlayer + 1) % 2].writeObject(ServerGameSessionProtocol.SENT_CATEGORY_TO_OPPONENT);
                    outputStreams[(currentPlayer + 1) % 2].writeObject(selectedCategory);
                    outputStreams[(currentPlayer + 1) % 2].flush();


                    //Hämta random frågor från questionManager och skapa Round object
                    Round round = new Round(questionManager.getRandomQuestionsByCategory(selectedCategory.toLowerCase()));
                    // Spelaren som valde kategori får svara
                    if (!processQuestions(outputStreams[currentPlayer], inputStreams[currentPlayer], game, players[currentPlayer], round)) {
                        handlePlayerGaveUp(currentPlayer,  outPlayer1, outPlayer2);
                        continue mainLoop;
                    }

                    ArrayList<Integer> currentPlayerScore = round.getRoundScore();

                    // Andra spelaren får svara
                    if (!processQuestions(outputStreams[(currentPlayer + 1) % 2], inputStreams[(currentPlayer + 1) % 2], game, players[(currentPlayer + 1) % 2], round)) {
                        handlePlayerGaveUp((currentPlayer + 1) % 2, outPlayer1, outPlayer2);
                        continue mainLoop;
                    }

                    ArrayList<Integer> opponentPlayerScore = round.getRoundScore();

                    sendResultsToOpponent(outputStreams[(currentPlayer + 1) % 2], currentPlayerScore);

                    sendResultsToOpponent(outputStreams[currentPlayer], opponentPlayerScore);

                    // Informera om att vänta
                    outputStreams[currentPlayer].writeObject(ServerGameSessionProtocol.WAITING);
                    currentPlayer = (currentPlayer + 1) % 2;

                }



                outPlayer1.writeObject(ServerGameSessionProtocol.GAME_OVER);
                outPlayer1.flush();
                outPlayer2.writeObject(ServerGameSessionProtocol.GAME_OVER);
                outPlayer2.flush();


                findWinner(outPlayer1,outPlayer2,game.getPlayer1Score(),game.getPlayer2Score());
                
                //TODO Uppdatera vinster i fil/map


                if (!handlePlayAgain(outPlayer1,outPlayer2,inPlayer1,inPlayer2)) {
                    System.out.println("Avbryter spelet");
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void sendScoreWindowData(Player player1, Player player2, ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2, int totalRounds) throws IOException {
        outPlayer1.writeObject(ServerGameSessionProtocol.SEND_SCORE_WINDOW_DATA);
        outPlayer1.writeObject(totalRounds);
        outPlayer1.writeObject(player2);
        outPlayer2.writeObject(ServerGameSessionProtocol.SEND_SCORE_WINDOW_DATA);
        outPlayer2.writeObject(totalRounds);
        outPlayer2.writeObject(player1);
    }


    private String handleCategorySelection(ObjectOutputStream outputStream, ObjectInputStream inputStream, ArrayList<String> categories) throws IOException, ClassNotFoundException {
        //Hämta random lista med 3 kategorier
        outputStream.writeObject(ServerGameSessionProtocol.SENT_CATEGORY);
        ArrayList<String> randomCategories = getRandomCategories(categories);

        //Skickar tre kategorier till client
        outputStream.writeObject(randomCategories);
        outputStream.flush();

        Object clientResponse = inputStream.readObject();

        //Kollar om spelaren gav upp
        if (clientResponse.equals(ClientGameSessionProtocol.CLIENT_GAVE_UP)) {
            return null;
        }

        //Tar emot client svar på kategori
        String categoryInput = (String) clientResponse;

        //Ta bort kategorin som redan är spelad
        removeCategoryFromList(categories, categoryInput);

        return categoryInput;
    }

    
    private ArrayList<String> getRandomCategories(ArrayList<String> categories) {
        Collections.shuffle(categories);
        return new ArrayList<>(categories.subList(0, 3));
        
    }
    private void removeCategoryFromList(ArrayList<String> categories, String categoryInput) {
        categories.remove(categoryInput);
    }

    private boolean processQuestions(ObjectOutputStream outputStream, ObjectInputStream inputStream, Game game, Player player, Round round) throws IOException, ClassNotFoundException {
 
        outputStream.writeObject(ServerGameSessionProtocol.SENT_QUESTIONS);
        outputStream.writeObject(round.getCurrentQuestions());
        outputStream.flush();

        //Kollar om spelaren har gett up
        Object clientResponse = inputStream.readObject();
        if (clientResponse.equals(ClientGameSessionProtocol.CLIENT_GAVE_UP)) {
            return false;
        }

        //Tar emot hur många rätt använadaren hade
        ArrayList<Integer> opponentRoundScore = (ArrayList<Integer>) clientResponse;
        game.incrementScore(player, opponentRoundScore);
        round.setRoundScore(opponentRoundScore);

        return true;
    }

    private void sendResultsToOpponent(ObjectOutputStream outputStream, ArrayList<Integer> score) throws IOException {
        //Skicka resultat till andra spelaren
        outputStream.writeObject(ServerGameSessionProtocol.SENT_ROUND_SCORE);
        outputStream.writeObject(score);
        outputStream.flush();
    }

    private void handlePlayerGaveUp(int currentPlayer, ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2) throws IOException {
        //TODO Hantera give up på server sida
        
        if (currentPlayer == 0) {
            outPlayer1.writeObject(ServerGameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer1.writeObject("Du gav upp! Din motståndare vann");
            outPlayer2.writeObject(ServerGameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer2.writeObject("Den andra spelaren gav upp! Du vann");
        } else {
            outPlayer2.writeObject(ServerGameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer2.writeObject("Du gav upp! Din motståndare vann");
            outPlayer1.writeObject(ServerGameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer1.writeObject("Den andra spelaren gav upp! Du vann");
        }

        }
    private boolean handlePlayAgain(ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2,
                                    ObjectInputStream inPlayer1, ObjectInputStream inPlayer2) throws IOException, ClassNotFoundException {

        ClientGameSessionProtocol responsePlayer1 = (ClientGameSessionProtocol) inPlayer1.readObject();
        ClientGameSessionProtocol responsePlayer2 = (ClientGameSessionProtocol) inPlayer2.readObject();

        System.out.println("play again" + responsePlayer1);
        System.out.println("play again" + responsePlayer2);
        
        if (responsePlayer1.equals(ClientGameSessionProtocol.PLAY_AGAIN) && responsePlayer2.equals(ClientGameSessionProtocol.PLAY_AGAIN)) {
            System.out.println("Play again success");
            outPlayer1.writeObject(ServerGameSessionProtocol.PLAY_AGAIN_SUCCESS);
            outPlayer2.writeObject(ServerGameSessionProtocol.PLAY_AGAIN_SUCCESS);
            return true;
        } else if (!responsePlayer1.equals(ClientGameSessionProtocol.PLAY_AGAIN)){
            System.out.println("Play again denied ");
            outPlayer2.writeObject(ServerGameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer2.flush();
            return false;
        } else {
            System.out.println("Play again denied ");
            outPlayer1.writeObject(ServerGameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer1.flush();
            return false;
        }
    }

    private void findWinner(ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2, int player1Score, int player2Score) throws IOException {
        String tie = "Spelet blev lika!";
        String loser = "Du Förlora!";
        String winner = "Du vann!";
        if (player1Score == player2Score) {
            outPlayer1.writeObject(tie);
            outPlayer2.writeObject(tie);
        } else if (player1Score > player2Score) {
            outPlayer1.writeObject(winner);
            outPlayer2.writeObject(loser);
        } else {
            outPlayer1.writeObject(loser);
            outPlayer2.writeObject(winner);
        }
    }

}


