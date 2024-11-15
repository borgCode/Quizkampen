package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame implements ActionListener {


    JPanel scorePanel = new JPanel();
    JButton scoreButton1 = new JButton();
    JButton scoreButton2 = new JButton();
    JButton scoreButton3 = new JButton();

    JPanel questionPanel = new JPanel();
    JLabel question = new JLabel("Fråga?");

    JPanel answerPanel = new JPanel();
    JButton[] answerButtons = new JButton [4];

    GameWindow() {
        //TODO: POÄNG
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scorePanel.add(scoreButton1);
        scorePanel.add(scoreButton2);
        scorePanel.add(scoreButton3);
        scoreButton1.setPreferredSize(new Dimension(30, 30));
        scoreButton2.setPreferredSize(new Dimension(30, 30));
        scoreButton3.setPreferredSize(new Dimension(30, 30));

        //Inte kunna klicka på knapparna
        scoreButton1.setEnabled(false);
        scoreButton2.setEnabled(false);
        scoreButton3.setEnabled(false);

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

    //TODO: Kategorier
    CategoryWindow(){
        JPanel categoryPanel = new JPanel();
        JButton category1 = new JButton("TEXT");
        JButton category2 = new JButton("TEXT");
        JButton category3 = new JButton("TEXT");

        setTitle("Kategori");
        categoryPanel.setLayout(new FlowLayout());
        categoryPanel.add(category1);
        categoryPanel.add(category2);
        categoryPanel.add(category3);
        add(categoryPanel);

        category1.addActionListener(this);
        category2.addActionListener(this);
        category3.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == category1) {
                new GameWindow("Kategori 1");
            } else if (e.getSource() == category2) {
                new GameWindow("Kategori 2");
            } else if (e.getSource() == category3) {
                new GameWindow("Kategori 3");
            }
        }


    }

    public static void main(String[] args) {
        GameWindow gameWindow = new GameWindow();
    }

}
