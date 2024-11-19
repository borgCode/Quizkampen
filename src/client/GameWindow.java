package client;

import client.listener.AnswerListener;
import server.entity.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {

    private String correctAnswer;
    private AnswerListener answerListener;
    private boolean hasAnswered;

    JPanel scorePanel = new JPanel();
    JButton scoreButton1 = new JButton();
    JButton scoreButton2 = new JButton();
    JButton scoreButton3 = new JButton();

    JPanel questionPanel = new JPanel();
    JLabel question = new JLabel("Fråga?");

    JPanel answerPanel = new JPanel();
    JButton[] answerButtons = new JButton [4];

    GameWindow() {

        //backgroundLabel
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setBounds(0, 0, 400, 500);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);


        //Poängrutan
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.add(scoreButton1);
        scorePanel.add(scoreButton2);
        scorePanel.add(scoreButton2);
        scorePanel.add(scoreButton3);
        scoreButton1.setPreferredSize(new Dimension(30, 30));
        scoreButton2.setPreferredSize(new Dimension(30, 30));
        scoreButton3.setPreferredSize(new Dimension(30, 30));

        //Inte kunna klicka på knapparna (Poängrutan)
        scoreButton1.setEnabled(false);
        scoreButton2.setEnabled(false);
        scoreButton3.setEnabled(false);


        scorePanel.setBackground(Color.lightGray);
        scorePanel.setPreferredSize(new Dimension(150, 50));
        scorePanel.setOpaque(false);
        backgroundLabel.add(scorePanel, BorderLayout.NORTH);


        
        //Frågan
        questionPanel.setLayout(new FlowLayout());
        questionPanel.setPreferredSize(new Dimension(150,100));
        questionPanel.add(question);
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setOpaque(false);


        //Svar
        answerPanel.setLayout(new GridLayout(2,2));
        answerPanel.setOpaque(false);


        JPanel questionAndAnswerPanel = new JPanel(new BorderLayout());
        questionAndAnswerPanel.setOpaque(false);
        questionAndAnswerPanel.add(questionPanel, BorderLayout.NORTH);
        questionAndAnswerPanel.add(answerPanel, BorderLayout.CENTER);
        backgroundLabel.add(questionAndAnswerPanel, BorderLayout.CENTER);



       // TODO: getSelectedCategory för att få fram rätt frågor + Lägg in frågorna i rätt button + Kontrollera svaret
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton("" + (i + 1));
            answerButtons[i].addActionListener(this);
            answerButtons[i].setPreferredSize(new Dimension(150, 100));
            //Lägger till knapparna i panelen
            answerPanel.add(answerButtons[i]);
        }
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            for (JButton button : answerButtons) {
                button.setBackground(null);
            }
            setHasAnswered(true);
        });

        JPanel nextButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButtonPanel.add(nextButton);
        nextButtonPanel.setOpaque(false);
        backgroundLabel.add(nextButtonPanel, BorderLayout.SOUTH);


        setTitle("Quiz Kampen");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String selectedAnswer = clickedButton.getText();

        // Byter färg baserat på svar
        if (selectedAnswer.equals(correctAnswer)){
            clickedButton.setBackground(Color.GREEN);
        } else {
            clickedButton.setBackground(Color.RED);
            // Markerar rätt svar efter man valt knapp
            for (JButton button : answerButtons) {
                if (button.getText().equals(correctAnswer)) {
                    button.setBackground(Color.GREEN);
                }
            }
        }
        // Inaktiverar knappar efter val
        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }

        if (answerListener != null) {
            answerListener.onAnswerSelected(selectedAnswer);
        }
    }
    //TODO:TILLFÄLLIGT FÖR TEST
    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow();
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    public void updateQuestion(Question question) {
        this.question.setText(question.getQuestion());

        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            answerButtons[i].setText(options[i]);
            answerButtons[i].setEnabled(true);
        }

        this.correctAnswer = question.getCorrectAnswer();
        
    }

    public boolean isHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        this.hasAnswered = hasAnswered;
    }
}