package client;

import server.entity.Question;

import java.io.*;
import java.net.Socket;

public class Client {
    
    Client(){
        int port = 55566;
        String ip = "127.0.0.1";
        
        try(Socket socket = new Socket(ip,port);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ){
            
            while (true) {
                //Läser inte kategorier från server
                String[] categories = (String[]) in.readObject();
                for (int i = 0; i < categories.length; i++) {
                    System.out.println(i + ": " + categories[i]);
                }

                //Klient väljer en kategori och skickar det till server
                out.writeObject(userInput.readLine());
                out.flush();

                //Frågor beronde på vilken kategori som valdes
                Question[] questions = (Question[]) in.readObject();

                
                //räknar hur många rätt och skickar till server
                int scoreCounter = 0;
                for (int i = 0; i < questions.length; i++) {
                    System.out.println(questions[i].getQuestion());
                    for (int j = 0; j < questions[i].getOptions().length; j++) {
                        System.out.println(j + ": " + questions[i].getOptions()[j]);
                    }
                    if (userInput.readLine().equals(questions[i].getCorrectAnswer())) {
                        scoreCounter++;
                    }
                }

                out.writeObject(scoreCounter);
                out.flush();
            }
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        Client client = new Client();

    }
}
