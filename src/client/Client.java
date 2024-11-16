package client;

import org.w3c.dom.ls.LSOutput;
import server.entity.Player;
import server.entity.Question;
import server.network.Protocol;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client {
        Client() {
            StartWindow startWindow = new StartWindow();
            int port = 55566;
            String ip = "127.0.0.1";
            String name;
            ImageIcon avatar;

            try (Socket socket = new Socket(ip, port);
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                //Skickar spelaren till servern
                out.writeObject(startWindow.getPlayer());
                
                boolean isWaiting = false;

                while (true) {
                    Protocol state = (Protocol) in.readObject();
                    if (state.equals(Protocol.WAITING)) {
                        if (!isWaiting) {
                            System.out.println("Vänta på andra spelaren");
                            isWaiting = true;
                        }
                        
                    } else if (state.equals(Protocol.SENT_CATEGORY)) {
                        isWaiting = false;
                        
                        // Läser in kategorier
                        System.out.println(in.readObject());
                        out.writeObject(userInput.readLine());
                        out.flush();
                        

                    } else if (state.equals(Protocol.SENT_QUESTIONS)) {
                        isWaiting = false;
                        //Läs in tre frågor
                        ArrayList<Question> questions = (ArrayList<Question>) in.readObject();
                        for (Question question : questions) {
                            System.out.println(question.getQuestion());
                        }

                        //Skickar antal rätt till server
                        out.writeObject(3);
                        out.flush();
                        
                    }
                }

            } catch (UnknownHostException e) {
                System.err.println("Okänd värd: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Fel vid anslutning eller dataläsning: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {
            Client client = new Client();
        }
    }


