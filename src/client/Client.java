package client;

import server.entity.Question;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {
    JPanel panel = new JPanel();

    
    Client(){
        int port = 55566;
        String ip = "127.0.0.1";
        
        try(Socket socket = new Socket(ip,port);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ){

            Question question = (Question) in.readObject();
            for (int i = 0; i < question.getOptions().length; i++) {
                System.out.println(question.getOptions()[i]);
            }
            
            out.writeObject(userInput.readLine());
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        Client client = new Client();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
