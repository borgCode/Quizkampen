package server.data;

import server.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class UserDataManager {
    private static UserDataManager instance;
    private final ConcurrentHashMap<String, Player> players;

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
                Player player = new Player(parts[2], parts[5]);
                player.setUsername(parts[0]);
                player.setPassword(parts[1]);
                player.setNumOfWins(Integer.parseInt(parts[3]));
                player.setNumOfLosses(Integer.parseInt(parts[4]));
                players.put(player.getUsername(), player);
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void updatePlayerStats(String username, boolean isWin) {
        Player player = players.get(username);
        if (player != null) {
            if (isWin) {
                player.setNumOfWins(player.getNumOfWins() + 1);
            } else {
                player.setNumOfLosses(player.getNumOfLosses() + 1);
            }
            saveUsersToFile();
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/server/data/userdata.csv"))) {
            writer.write("Username,Password,Name,Wins,Losses,AvatarPath\n");
            for (Player player : players.values()) {
                writer.write(String.format("%s,%s,%s,%d,%d,%s\n",
                        player.getUsername(),
                        player.getPassword(),
                        player.getName(),
                        player.getNumOfWins(),
                        player.getNumOfLosses(),
                        player.getAvatarPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving users to file", e);
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
            bufferedWriter.write((String.format("%s,%s,%s,%d,%d,%s\n",
                    newPlayer.getUsername(),
                    newPlayer.getPassword(),
                    newPlayer.getName(),
                    0,
                    0,
                    newPlayer.getAvatarPath())));
            
        } catch (IOException e) {
            return false;
        }
        
        //Uppdatera Map
        players.put(newPlayer.getUsername(), newPlayer);
        return true;
    }
    
    public ArrayList<Player> getAllPlayersRanked() {
        ArrayList<Player> playersList = new ArrayList<>(players.values());
        
        //Sortera listan till flest vinster först
        playersList.sort(Comparator.comparingInt(Player::getNumOfWins).reversed());
        
        return playersList;
    }

    public boolean doesUsernameExist(String username) {
        return players.containsKey(username);
    }

    public Player getPlayerByUsername(String friendName) {
        return players.get(friendName);
    }
}
