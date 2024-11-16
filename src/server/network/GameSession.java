package server.network;

import server.entity.Question;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameSession extends Thread {

    private final Socket player1;
    private final Socket player2;
    private final QuestionBank questionBank;


    public GameSession(Socket player1, Socket player2) {
        System.out.println("Två spelare anslutna");
        this.player1 = player1;
        this.player2 = player2;
        this.questionBank = new QuestionBank();
    }

    public void run() {
        try (
                ObjectOutputStream outPlayer1 = new ObjectOutputStream(player1.getOutputStream());
                ObjectInputStream inPlayer1 = new ObjectInputStream(player1.getInputStream());
                ObjectOutputStream outPlayer2 = new ObjectOutputStream(player2.getOutputStream());
                ObjectInputStream inPlayer2 = new ObjectInputStream(player2.getInputStream())
        )
        {

            //testkategorier

            //TODO ändra keyvalues i hashmap till svenska
            ArrayList<String> categories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap", "Nöje", "TV", "Spel", "Mat", "Literatur", "Sport"));

            System.out.println("Nu läser vi in spelare");
            Player player1 = (Player) inPlayer1.readObject();
            Player player2 = (Player) inPlayer2.readObject();
            System.out.println(player1.getName());

            
            outPlayer1.writeObject("test");
            outPlayer2.writeObject("test");


            while (true) {
                Game game = new Game(player1, player2);

                //TODO Logik för vem som börjar

                //TODO Logik för 3 random kategorier + veta vilken som redan spelats

                ArrayList<String> randomCategories = new ArrayList<>(List.of("Geografi", "Historia", "Vetenskap"));

                //Skickar tre kategorier till client
                outPlayer1.writeObject(randomCategories);
                outPlayer1.flush();

                //Tar emot client svar på kategori
                String categoryInput = (String) inPlayer1.readObject();

                //Hämta random frågor från questionBank
                ArrayList<Question> questions = questionBank.getRandomQuestionsByCategory(categoryInput);

                outPlayer1.writeObject(questions);
                outPlayer1.flush();


                //Tar emot hur många rätt använadaren hade
                int scorePlayer1 = (int) (inPlayer1.readObject());
                //game.setPlayer1Score(scorePlayer1);


                //Skicka till spelare 2
                outPlayer2.writeObject(questions);
                outPlayer2.flush();


                int scorePlayer2 = (int) (inPlayer2.readObject());
                //game.setPlayer2Score(scorePlayer2);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
        //testkategorier
            String[] categories = {"Geografi", "Historia", "Vetenskap"};

            while (true) {

                //Skickar tre kategorier till client
                out.writeObject(categories);
                out.flush();

                //Tar emot client svar på kategori
                String categoryInput = (String) in.readObject();

                //Testfrågor beronde på vilken kategori som valdes
                if (categoryInput.equals("Geografi")) {
                    String[] geoOptions1 = {"Afrika", "Europa", "Asien", "Antarktis"};
                    Question geoQ1 = new Question("Vilken kontinent är den största till ytan?", geoOptions1, "Asien");


                    String[] geoOptions2 = {"Amazonas", "Nilen", "Yangtze", "Mississippi"};
                    Question geoQ2 = new Question("Vilken flod är världens längsta?", geoOptions2, "Nilen");


                    String[] geoOptions3 = {"Paris", "London", "Rom", "Berlin"};
                    Question geoQ3 = new Question("Vilken stad kallas 'Ljusets stad'?", geoOptions3, "Paris");

                    Question[] questions = {geoQ1, geoQ2, geoQ3};

                    out.writeObject(questions);
                    out.flush();
                } else if (categoryInput.equals("Historia")) {

                    String[] histOptions1 = {"André the Giant", "Genghis Khan", "Alexander den store", "Napoleon Bonaparte"};
                    Question histQ1 = new Question("Vem var ledare för det största sammanhängande imperiet i historien?", histOptions1, "Genghis Khan");

                    String[] histOptions2 = {"Första världskriget", "Andra världskriget", "Napoleonskrigen", "Koreakriget"};
                    Question histQ2 = new Question("Vilket krig började 1939?", histOptions2, "Andra världskriget");

                    String[] histOptions3 = {"Karl XII", "Gustav Vasa", "Olof Skötkonung", "Erik XIV"};
                    Question histQ3 = new Question("Vem grundade det svenska kungariket?", histOptions3, "Gustav Vasa");

                    Question[] questions = {histQ1, histQ2, histQ3};

                    out.writeObject(questions);
                    out.flush();
                } else if (categoryInput.equals("Vetenskap")) {

                    String[] sciOptions1 = {"Jupiter", "Mars", "Venus", "Merkurius"};
                    Question sciQ1 = new Question("Vilken planet är den största i vårt solsystem?", sciOptions1, "Jupiter");

                    String[] sciOptions2 = {"Charles Darwin", "Albert Einstein", "Nikola Tesla", "Marie Curie"};
                    Question sciQ2 = new Question("Vem utvecklade teorin om naturligt urval?", sciOptions2, "Charles Darwin");

                    String[] sciOptions3 = {"Cellen", "Atomen", "Molekylen", "Energipartikeln"};
                    Question sciQ3 = new Question("Vad kallas den minsta enheten i levande organismer?", sciOptions3, "Cellen");

                    Question[] questions = {sciQ1, sciQ2, sciQ3};

                    out.writeObject(questions);
                    out.flush();
                }

                //Tar emot hur många rätt använadaren hade
                int score = (int) (in.readObject());
                System.out.println("Spelaren hade " + score + " rätt!");


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

