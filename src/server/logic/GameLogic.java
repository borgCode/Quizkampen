package server.logic;

import server.entity.Game;
import server.entity.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {

    public static ArrayList<String> getRandomCategories(ArrayList<String> categories) {
        Collections.shuffle(categories);
        return new ArrayList<>(categories.subList(0, 3));
    }

    public static void removeCategoryFromList(ArrayList<String> categories, String categoryInput) {
        categories.remove(categoryInput);
    }
    public static void findWinner (ObjectOutputStream p1, ObjectOutputStream p2, int p1Score, int p2Score) throws IOException {
        String tie = "Spelet blev lika!";
        String loser = "Du Förlora!";
        String winner = "Du vann!";
        if (p1Score == p2Score) {
            p1.writeObject(tie);
            p2.writeObject(tie);
        } else if (p1Score > p2Score) {
            p1.writeObject(winner);
            p2.writeObject(loser);
        } else if (p2Score > p1Score) {
            p1.writeObject(loser);
            p2.writeObject(winner);
        }

    }

}