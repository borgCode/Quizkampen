package server.data;

import server.entity.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UserDataManager {
    public static HashMap<String, Player> players;
    

    public static void loadUsersFromFile() {
        players = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("src/server/data/userdata.csv"))) {
            //Skippa första raden
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Player player = new Player(parts[0], parts[4]);
                player.setPassword(parts[1]);
                player.setNumOfWins(Integer.parseInt(parts[2]));
                player.setNumOfLosses(Integer.parseInt(parts[3]));
                players.put(player.getName(), player);
            }
            
            for (String player : players.keySet()) {
                System.out.println(player);
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
