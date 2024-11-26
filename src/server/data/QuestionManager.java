package server.data;

import server.entity.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class QuestionManager {

    private static QuestionManager instance;
    private HashMap<String, ArrayList<Question>> questionMap;
    
    
    private QuestionManager() {
        loadData();
    }

    

    public static synchronized QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    private void loadData() {
        questionMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/server/data/questions.csv"))) {
            //Skippa första raden
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String category = parts[0];
                Question question = new Question(parts[1], new String[]{parts[2],parts[3], parts[4], parts[5]}, parts[6]);
                
                if (questionMap.containsKey(category)) {
                    questionMap.get(category).add(question);
                } else {
                    ArrayList<Question> questionList = new ArrayList<>();
                    questionList.add(question);
                    questionMap.put(category, questionList);
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Question> getRandomQuestionsByCategory(String category) {
        ArrayList<Question> questions = new ArrayList<>(questionMap.get(category));

        if (questions.isEmpty()) {
            System.out.println("No such category exists or there are no questions in the category");
            return new ArrayList<>();
        }

        //blanda frågorna och returnera en lista med 3 frågor
        Collections.shuffle(questions);
        return new ArrayList<>(questions.subList(0, 3));

    }

}
