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
    private ImageIcon crossImageIcon;
    private ImageIcon checkImageIcon;

    JPanel scorePanel = new JPanel();
    JButton[] scoreButtons = new JButton[3];
    int round = 0;
    
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


//        Poängrutan
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < 3; i++) {
            JButton scoreButton = new JButton();
            scoreButton.setPreferredSize(new Dimension(30, 30));
            scoreButton.setEnabled(true);
            scoreButton.setContentAreaFilled(false);
            scoreButton.setBorder(BorderFactory.createEmptyBorder());
            scoreButtons[i] = scoreButton;
            scorePanel.add(scoreButton);
            
        }

        crossImageIcon = new ImageIcon("src/resources/images/cross.png");
        Image scaledCrossImage = crossImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        crossImageIcon = new ImageIcon(scaledCrossImage);

        checkImageIcon = new ImageIcon("src/resources/images/check.png");
        Image scaledCheckImage = checkImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        checkImageIcon = new ImageIcon(scaledCheckImage);

        

        
        /*avatar2.setContentAreaFilled(false); // Tar bort bakgrund
        avatar2.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje
        avatar2.setFocusable(false);
        avatar2.setOpaque(false);*/

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


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String selectedAnswer = clickedButton.getText();

        // Byter färg baserat på svar
        if (selectedAnswer.equals(correctAnswer)){
            clickedButton.setBackground(Color.GREEN);
            scoreButtons[round].setIcon(checkImageIcon);
            round++;
        } else {
            clickedButton.setBackground(Color.RED);
            scoreButtons[round].setIcon(crossImageIcon);
            round++;
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