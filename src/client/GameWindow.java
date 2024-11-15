package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {

    JPanel scorePanel = new JPanel();

    JPanel questionPanel = new JPanel();
    JLabel question = new JLabel("Fråga?");

    JPanel answerPanel = new JPanel();
    JButton[] answerButtons = new JButton [4];

    GameWindow() {
        //TODO: POÄNG
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBackground(Color.lightGray);
        scorePanel.setPreferredSize(new Dimension(150, 50));
        add(scorePanel, BorderLayout.NORTH);

        //TODO:FRÅGAN
        questionPanel.setLayout(new FlowLayout());
        questionPanel.add(question);
        questionPanel.setBackground(Color.WHITE);
        add(questionPanel, BorderLayout.CENTER);

        //TODO: SVAR
        answerPanel.setLayout(new GridLayout(2,2));
        add(answerPanel, BorderLayout.SOUTH);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton("" + (i + 1));
            answerButtons[i].addActionListener(this);
            answerButtons[i].setPreferredSize(new Dimension(150, 100));
            //Lägger till knapparna i panelen
            answerPanel.add(answerButtons[i]);
        }

       setTitle("Quiz Kampen");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    //TODO: Panel TIMER?

    //TODO: KNAPP LYSSNARE
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow();
    }

}
