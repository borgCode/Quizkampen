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
    public static int getTurn(int index, int counter){

        return index % counter;
    }

}