package server.network;

import server.entity.Question;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler extends Thread {

    Socket clientSocket;

    public Handler(Socket socketToClient) {
        this.clientSocket = socketToClient;
    }

    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());) {

            String[] categories = {"Geografi", "Historia", "Vetenskap"};

            while (true) {
                
                out.writeObject(categories);
                out.flush();

                String categoryInput = (String) in.readObject();


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

                int score = (int) (in.readObject());
                System.out.println("Spelaren hade " + score + " rätt!");


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


 
