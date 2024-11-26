package server.network;

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

            outPlayer1.writeObject(GameSessionProtocol.WAITING_FOR_OPPONENT);
            outPlayer2.writeObject(GameSessionProtocol.WAITING_FOR_OPPONENT);
            outPlayer1.flush();
            outPlayer2.flush();
            
            
            //Hämta spelar objekt från båda klienterna
            Player player1 = (Player) inPlayer1.readObject();
            Player player2 = (Player) inPlayer2.readObject();
            

            outPlayer1.writeObject(GameSessionProtocol.GAME_START);
            outPlayer2.writeObject(GameSessionProtocol.GAME_START);
            outPlayer1.flush();
            outPlayer2.flush();


            //Lägg streamsen och players i arrays som används i loop för att växla mellan dem
            ObjectOutputStream[] outputStreams = {outPlayer1, outPlayer2};
            ObjectInputStream[] inputStreams = {inPlayer1, inPlayer2};
            Player[] players = {player1, player2};
            int currentPlayer = 0;


            sendScoreWindowData(player1, player2, outPlayer1, outPlayer2, totalRounds);


            while (true) {
                Game game = new Game(player1, player2);

                //Berätta för klient 2 att den ska vänta på andra spelarens tur
                ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV-serier", "TV-spel", "Mat", "Litteratur", "Sport"));
                for (int i = 0; i < totalRounds; i++){
                    // Låt currentplayer välja kategori
                    String selectedCategory = handleCategorySelection(outputStreams[currentPlayer], inputStreams[currentPlayer], categories);
                    if (selectedCategory == null) {
                        handlePlayerGaveUp(currentPlayer, (currentPlayer + 1) % 2, outPlayer1, outPlayer2);
                        return;
                    }

                    //Hämta random frågor från questionManager och skapa Round object
                    Round round = new Round(questionManager.getRandomQuestionsByCategory(selectedCategory.toLowerCase()), selectedCategory);
                    // Spelaren som vlade kategori får svara
                    if (!processQuestions(outputStreams[currentPlayer], inputStreams[currentPlayer], game, players[currentPlayer], round)) {
                        handlePlayerGaveUp(currentPlayer, (currentPlayer + 1) % 2, outPlayer1, outPlayer2);
                        return;
                    }

                    ArrayList<Integer> player1Score = round.getOpponentRoundScore();

                    // Andra spelaren får svara
                    if (!processQuestions(outputStreams[(currentPlayer + 1) % 2], inputStreams[(currentPlayer +1) % 2], game, players[(currentPlayer + 1) % 2], round)) {
                        handlePlayerGaveUp((currentPlayer + 1) % 2, currentPlayer, outPlayer1, outPlayer2);
                        return;
                    }

                    ArrayList<Integer> player2Score = round.getOpponentRoundScore();

                    sendResultsToOpponent(outputStreams[(currentPlayer + 1) % 2], player1Score);

                    sendResultsToOpponent(outputStreams[currentPlayer], player2Score);

                    // Informera om att vänta
                    outputStreams[currentPlayer].writeObject(GameSessionProtocol.WAITING);
                    currentPlayer = (currentPlayer + 1) % 2;

                }



                outPlayer1.writeObject(GameSessionProtocol.GAME_OVER);
                outPlayer1.flush();
                outPlayer2.writeObject(GameSessionProtocol.GAME_OVER);
                outPlayer2.flush();


                findWinner(outPlayer1,outPlayer2,game.getPlayer1Score(),game.getPlayer2Score());


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
        outPlayer1.writeObject(GameSessionProtocol.SEND_SCORE_WINDOW_DATA);
        outPlayer1.writeObject(totalRounds);
        outPlayer1.writeObject(player2);
        outPlayer2.writeObject(GameSessionProtocol.SEND_SCORE_WINDOW_DATA);
        outPlayer2.writeObject(totalRounds);
        outPlayer2.writeObject(player1);
    }


    private String handleCategorySelection(ObjectOutputStream outputStream, ObjectInputStream inputStream, ArrayList<String> categories) throws IOException, ClassNotFoundException {
        //Hämta random lista med 3 kategorier
        outputStream.writeObject(GameSessionProtocol.SENT_CATEGORY);
        ArrayList<String> randomCategories = getRandomCategories(categories);

        //Skickar tre kategorier till client
        outputStream.writeObject(randomCategories);
        outputStream.flush();

        Object clientResponse = inputStream.readObject();

        //Kollar om spelaren gav upp
        if (clientResponse.equals(GameSessionProtocol.CLIENT_GAVE_UP)) {
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

    private boolean processQuestions(ObjectOutputStream outputStream, ObjectInputStream inputStream, Game game, Player player1, Round round) throws IOException, ClassNotFoundException {

        outputStream.writeObject(GameSessionProtocol.SENT_QUESTIONS);
        outputStream.writeObject(round.getCurrentQuestions());
        outputStream.flush();

        //Kollar om spelaren har gett up
        Object clientResponse = inputStream.readObject();
        if (clientResponse.equals(GameSessionProtocol.CLIENT_GAVE_UP)) {
            return false;
        }

        //Tar emot hur många rätt använadaren hade
        ArrayList<Integer> opponentRoundScore = (ArrayList<Integer>) clientResponse;
        game.incrementScore(player1, opponentRoundScore);
        round.setOpponentRoundScore(opponentRoundScore);

        return true;
    }

    private void sendResultsToOpponent(ObjectOutputStream outputStream, ArrayList<Integer> score) throws IOException {
        //Skicka resultat till andra spelaren
        outputStream.writeObject(GameSessionProtocol.SENT_ROUND_SCORE);
        outputStream.writeObject(score);
        outputStream.flush();
    }

    private void handlePlayerGaveUp(int currentPlayer, int i, ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2) throws IOException {
        System.out.println("Player " + (currentPlayer + 1) + " gav upp!");


        if (currentPlayer == 0) {
            outPlayer1.writeObject(GameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer1.writeObject("Du gav upp! Din motståndare vann");
            outPlayer2.writeObject(GameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer2.writeObject("Den andra spelaren gav upp! Du vann");
        } else {
            outPlayer2.writeObject(GameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer2.writeObject("Du gav upp! Din motståndare vann");
            outPlayer1.writeObject(GameSessionProtocol.PLAYER_GAVE_UP);
            outPlayer1.writeObject("Den andra spelaren gav upp! Du vann");
        }

        }
    private boolean handlePlayAgain(ObjectOutputStream outPlayer1, ObjectOutputStream outPlayer2,
                                    ObjectInputStream inPlayer1, ObjectInputStream inPlayer2) throws IOException, ClassNotFoundException {

        outPlayer1.writeObject(GameSessionProtocol.SENT_PLAY_AGAIN);
        outPlayer2.writeObject(GameSessionProtocol.SENT_PLAY_AGAIN);
        outPlayer1.flush();
        outPlayer2.flush();

        boolean playAgainPlayer1, playAgainPlayer2;
        try {
            playAgainPlayer1 = (boolean) inPlayer1.readObject();
        } catch (Exception e) {
            outPlayer2.writeObject(GameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer2.flush();
            //player2.getClientSocket().close();
            //player1.getClientSocket().close();
            return false;
        }

        try {
            playAgainPlayer2 = (boolean) inPlayer2.readObject();
        } catch (Exception e) {
            outPlayer1.writeObject(GameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer1.flush();
            player1.getClientSocket().close();
            player2.getClientSocket().close();
            return false;
        }

        if (playAgainPlayer1 && playAgainPlayer2) {
            return true;
        } else {
            outPlayer1.writeObject(GameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer2.writeObject(GameSessionProtocol.PLAY_AGAIN_DENIED);
            outPlayer1.flush();
            outPlayer2.flush();
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
        } else if (player2Score > player1Score) {
            outPlayer1.writeObject(loser);
            outPlayer2.writeObject(winner);
        }
    }

}


