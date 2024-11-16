package client;

import org.w3c.dom.ls.LSOutput;
import server.entity.Player;
import server.entity.Question;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;



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


                out.writeObject(startWindow.getPlayer());

                // Läs det första meddelandet från servern
                System.out.println(in.readObject());

                // Skicka användarens namn
                out.writeObject(userInput.readLine());
                out.flush();

                // Läs nästa meddelande
                System.out.println(in.readObject());
                // Läs ytterligare information (om det behövs)
                System.out.println(in.readObject());

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


