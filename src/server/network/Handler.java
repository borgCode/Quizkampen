package server.network;

import server.entity.Question;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler extends Thread{
    
    Socket clientSocket;
    
    public Handler(Socket socketToClient) {
        this.clientSocket = socketToClient;
    }

    public void run(){
        try(
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());)
        {
            
            String question = "Sveriges största stad";
            String[] options = {"1. Stockholm, ", "2. Oslo, ", "3. Köpenhamn, ", "4. Helsinki"};
            String answer = "Stockholm";
            Question question1 = new Question(question, options, answer);
            
            out.writeObject(question1);
            String resultat = (String)in.readObject();
            if (resultat.equals(answer)) {
                System.out.println("Du hade rätt");
            } else {
                System.out.println("Fel svar! Rätt svar är " + answer);
            }
            
//            //TODO: Fixa till koden 
//            String inputLine;
//            Protocol protocol = new Protocol(); 
//            out.writeObject(protocol.processInput(null));
//
//            //servar frågeloopen
//            while ((inputLine = (String)in.readObject()) != null) {
//                out.writeObject(protocol.processInput(inputLine));
//            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


 
