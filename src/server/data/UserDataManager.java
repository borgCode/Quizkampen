package server.data;

import server.entity.Player;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataManager {
    private static UserDataManager instance;
    private ConcurrentHashMap<String, Player> players;

    public UserDataManager() {
        players = new ConcurrentHashMap<>();
        loadUsersFromFile();
    }

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    private void loadUsersFromFile() {
        
        
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
            
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    public Player authenticatePlayer(String userName, String password) {
        Player player = players.get(userName);
        if (player != null && player.getPassword().equals(password)) {
            return player;
        }
        return null;
    }

    public boolean registerNewUser(Player newPlayer) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/server/data/userdata.csv", true))) {
            bufferedWriter.write((String.format("%s,%s,%d,%d,%s\n",
                    newPlayer.getName(),
                    newPlayer.getPassword(),
                    0,
                    0,
                    newPlayer.getAvatarPath())));
            
        } catch (IOException e) {
            return false;
        }
        
        //Uppdatera Map
        players.put(newPlayer.getName(), newPlayer);
        return true;
    }
}
