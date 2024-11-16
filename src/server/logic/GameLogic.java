package server.logic;

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
}
