package client.windows;

import client.listeners.AnswerListener;
import server.entity.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;

public class QuestionWindow extends JFrame implements ActionListener {

    private String correctAnswer;
    private AnswerListener answerListener;
    private boolean hasAnswered;
    private ImageIcon crossImageIcon;
    private ImageIcon checkImageIcon;

    private JPanel scorePanel = new JPanel();
    private JButton[] scoreButtons = new JButton[3];
    private int round = 0; // Håller reda på vilken runda det är

    private JPanel questionPanel = new JPanel();
    private JTextArea question = new JTextArea();

    private JPanel answerPanel = new JPanel();
    private JButton[] answerButtons = new JButton [4];

    private JProgressBar timerBar;
    private Timer questionTimer;
    private static final int TIMER_DURATION = 15;

    public QuestionWindow() {

        // Sätter bakgrundsbilden och ställer in layouten
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setBounds(0, 0, 400, 500);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);


        // Skapar panel för att visa poäng och lägger till tre knappar som representerar varje runda
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < 3; i++) {
            JButton scoreButton = new JButton();
            scoreButton.setPreferredSize(new Dimension(30, 30));
            scoreButton.setEnabled(true);
            scoreButton.setContentAreaFilled(false); // Tar bort bakgrunden
            scoreButton.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinjen
            scoreButtons[i] = scoreButton;
            scorePanel.add(scoreButton);

        }

        // Sätter in och skalar ikonerna för korrekt och inkorrekt svar
        crossImageIcon = new ImageIcon("src/resources/images/cross.png");
        Image scaledCrossImage = crossImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        crossImageIcon = new ImageIcon(scaledCrossImage);

        checkImageIcon = new ImageIcon("src/resources/images/check.png");
        Image scaledCheckImage = checkImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        checkImageIcon = new ImageIcon(scaledCheckImage);

        scorePanel.setBackground(Color.lightGray);
        scorePanel.setPreferredSize(new Dimension(150, 50));
        scorePanel.setOpaque(false); // Gör panelen genomskinlig
        backgroundLabel.add(scorePanel, BorderLayout.NORTH);



        // Panel för frågan
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setPreferredSize(new Dimension(400, 150));
        questionPanel.setOpaque(false);

        // Ställer in egenskaper för texten som visar frågan
        question.setLineWrap(true); // Aktiverar radbrytning
        question.setWrapStyleWord(true); // Bryter vid ord
        question.setEditable(false); // Gör texten skrivskyddat
        question.setFont(new Font("Arial", Font.BOLD, 16));
        question.setOpaque(false); // Gör bakgrunden genomskinlig
        question.setFocusable(false);
        question.setMargin(new Insets(10, 10, 10, 10));
        question.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrerar
        question.setAlignmentY(Component.CENTER_ALIGNMENT); // Centrerar

        questionPanel.add(question, BorderLayout.CENTER);

        // Skapar och ställer in JProgressBar
        timerBar = new JProgressBar(0, TIMER_DURATION * 10); // JProgressBar med 10 steg per sekund
        timerBar.setValue(TIMER_DURATION * 10);
        timerBar.setStringPainted(false); // Visar ingen text
        timerBar.setForeground(Color.GREEN); // Ställer in färg för timern
        timerBar.setPreferredSize(new Dimension(400, 15));
        questionPanel.add(timerBar, BorderLayout.SOUTH);

        backgroundLabel.add(questionPanel, BorderLayout.CENTER);

        //Svar
        answerPanel.setLayout(new GridLayout(2,2,3,3));
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
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 14));
            //Lägger till knapparna i panelen
            answerPanel.add(answerButtons[i]);
        }
        JButton nextButton = new JButton("Fortsätt");
        nextButton.setFocusable(false); // Tar bort den fula kanten som kommer upp runt "fortsätt"
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.addActionListener(e -> {
            for (JButton button : answerButtons) {
                button.setBackground(null);
            }
            setHasAnswered(true);
        });

        JPanel nextButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nextButtonPanel.add(nextButton);
        nextButtonPanel.setOpaque(false);
        backgroundLabel.add(nextButtonPanel, BorderLayout.SOUTH);


        setTitle("Quizkampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String selectedAnswer = clickedButton.getText();

        handleAnswer(selectedAnswer, clickedButton);
    }

    // Metod för att hantera användarens svar
    private void handleAnswer(String selectedAnswer, JButton clickedButton) {
        stopTimer();


        // Byter färg baserat på svar
        if (selectedAnswer.equals(correctAnswer)){
            if (clickedButton != null)
                clickedButton.setBackground(Color.GREEN);
            scoreButtons[round].setIcon(checkImageIcon);
            round++;
        } else {
            if (clickedButton != null)
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
    // Metod för att starta timern för frågan
    public void startTimer() {
        timerBar.setValue(TIMER_DURATION * 10); // Återställer JProgressBar
        timerBar.setForeground(Color.GREEN); // Startar med färgen grön
        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = TIMER_DURATION * 10;

            @Override
            public void run() {
                remainingTime--; // Minskar tiden

                // Uppdaterar ProgressBar
                SwingUtilities.invokeLater(() -> {
                    timerBar.setValue(remainingTime);

                    // Ändrar färg beroende på återstående tid
                    if (remainingTime <= TIMER_DURATION * 10 * 0.25) { // Mindre än 25% kvar
                        timerBar.setForeground(Color.RED);
                    } else if (remainingTime <= TIMER_DURATION * 10 * 0.5) { // Mindre än 50% kvar
                        timerBar.setForeground(Color.ORANGE);
                    }
                });

                // Om tiden är slut, hantera timeout som ett svar
                if (remainingTime <= 0) {
                    stopTimer();
                    handleAnswer("Timeout", null);
                }
            }
        }, 0, 100); // Uppdaterar timern varje 100ms för snygghetens skull
    }

    // Stoppar timern
    public void stopTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }
    }


    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    public void updateQuestion(Question question) {
        stopTimer();
        this.question.setText(question.getQuestion());

        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            answerButtons[i].setText(options[i]);
            answerButtons[i].setEnabled(true);
        }

        this.correctAnswer = question.getCorrectAnswer();
        startTimer();
    }

    public boolean isHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public void resetRound() {
        round = 0;
        scoreButtons[0].setIcon(null);
        scoreButtons[1].setIcon(null);
        scoreButtons[2].setIcon(null);
    }
}