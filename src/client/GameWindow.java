package client;

import client.listener.AnswerListener;
import server.entity.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends JFrame implements ActionListener {

    private String correctAnswer;
    private AnswerListener answerListener;
    private boolean hasAnswered;
    private ImageIcon crossImageIcon;
    private ImageIcon checkImageIcon;

    // Panel och knappar för att visa poäng
    private JPanel scorePanel = new JPanel();
    private JButton[] scoreButtons = new JButton[3];
    private int round = 0; // Håller reda på vilken runda det är

    private JPanel questionPanel = new JPanel();
    private JTextArea questionArea = new JTextArea();

    // Panel och knappar för att visa svarsalternativ
    private JPanel answerPanel = new JPanel();
    private JButton[] answerButtons = new JButton[4];

    private JProgressBar timerBar;
    private Timer questionTimer;
    private static final int TIMER_DURATION = 15;

    GameWindow() {
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

        // Ställer in bakgrundsfärgen och gör panelen genomskinlig
        scorePanel.setBackground(Color.lightGray);
        scorePanel.setPreferredSize(new Dimension(150, 50));
        scorePanel.setOpaque(false);
        backgroundLabel.add(scorePanel, BorderLayout.NORTH);

        // Skapar panel för frågan och ställer in layout och storlek
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setPreferredSize(new Dimension(400, 150));
        questionPanel.setOpaque(false);

        // Ställer in egenskaper för texten som visar frågan
        questionArea.setLineWrap(true); // Aktiverar radbrytning
        questionArea.setWrapStyleWord(true); // Bryter vid ord
        questionArea.setEditable(false); // Gör texten skrivskyddat
        questionArea.setFont(new Font("Arial", Font.BOLD, 16));
        questionArea.setOpaque(false); // Gör bakgrunden genomskinlig
        questionArea.setFocusable(false);
        questionArea.setMargin(new Insets(10, 10, 10, 10));
        questionArea.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrerar
        questionArea.setAlignmentY(Component.CENTER_ALIGNMENT); // Centrerar

        questionPanel.add(questionArea, BorderLayout.CENTER);

        // Skapar och ställer in JProgressBar
        timerBar = new JProgressBar(0, TIMER_DURATION * 10); // JProgressBar med 10 steg per sekund
        timerBar.setValue(TIMER_DURATION * 10);
        timerBar.setStringPainted(false); // Visar ingen text
        timerBar.setForeground(Color.GREEN); // Ställer in färg för timern
        timerBar.setPreferredSize(new Dimension(400, 15));
        questionPanel.add(timerBar, BorderLayout.SOUTH);

        backgroundLabel.add(questionPanel, BorderLayout.CENTER);

        // Panel för svarsknappar och ställer in layouten
        answerPanel.setLayout(new GridLayout(2, 2));
        answerPanel.setOpaque(false);

        JPanel answerContainer = new JPanel(new BorderLayout());
        answerContainer.setOpaque(false);
        answerContainer.add(answerPanel, BorderLayout.CENTER);

        // Nästa-knappen
        JButton nextButton = new JButton("Nästa");
        nextButton.setFocusable(false); // Tar bort den fula kanten som kommer upp runt "nästa"
        nextButton.setFont(new Font("Arial", Font.BOLD, 12));
        nextButton.addActionListener(e -> {
            for (JButton button : answerButtons) {
                button.setBackground(null); // Återställer knappens färg
                button.setEnabled(true); // Aktiverar knapparna igen
            }
            setHasAnswered(true); // Visar att användaren är redo för nästa fråga
        });
        JPanel nextButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButtonPanel.add(nextButton);
        nextButtonPanel.setOpaque(false);

        answerContainer.add(nextButtonPanel, BorderLayout.SOUTH);
        backgroundLabel.add(answerContainer, BorderLayout.SOUTH);

        // Svarsknapparna
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i] = new JButton("" + (i + 1)); // Sätter knappens text
            answerButtons[i].addActionListener(this); // Lägger till ActionListener
            answerButtons[i].setPreferredSize(new Dimension(150, 100));
            answerPanel.add(answerButtons[i]);
        }

        // Ser till att layouten uppdateras korrekt
        backgroundLabel.revalidate();
        backgroundLabel.repaint();

        setTitle("QuizKampen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Gör så att fönstret inte ändrar storlek
    }

    // ActionListener som hanterar knapptryckningar från användaren
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String selectedAnswer = clickedButton.getText();

        handleAnswer(selectedAnswer, clickedButton);
    }

    // Metod för att hantera användarens svar
    private void handleAnswer(String selectedAnswer, JButton clickedButton) {
        stopTimer();

        // Ändrar knappens färg beroende på om svaret är rätt eller fel
        if (selectedAnswer.equals(correctAnswer)) {
            if (clickedButton != null) clickedButton.setBackground(Color.GREEN);
            scoreButtons[round].setIcon(checkImageIcon); // Sätter ikon för rätt svar
        } else {
            if (clickedButton != null) clickedButton.setBackground(Color.RED);
            scoreButtons[round].setIcon(crossImageIcon); // Sätter ikon för fel svar

            // Markerar rätt svar med grön färg
            for (JButton button : answerButtons) {
                if (button.getText().equals(correctAnswer)) {
                    button.setBackground(new Color(144, 238, 144));
                }
            }
        }

        round++; // Går vidare till nästa runda

        // Inaktiverar svarsknapparna efter att användaren har svarat
        for (JButton button : answerButtons) {
            button.setEnabled(false);
        }
        // Anropar lyssnaren om det finns en
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
                    if (remainingTime <= TIMER_DURATION * 10 * 0.15) { // Mindre än 15% kvar
                        timerBar.setForeground(Color.RED);
                    } else if (remainingTime <= TIMER_DURATION * 10 * 0.33) { // Mindre än 33% kvar
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

    public void updateQuestion(Question question) {
        stopTimer();
        this.questionArea.setText(question.getQuestion());

        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            answerButtons[i].setText(options[i]); // Ställer in texten för varje svarsknapp
            answerButtons[i].setEnabled(true); // Aktiverar svarsknapparna
        }

        this.correctAnswer = question.getCorrectAnswer(); // Sparar det korrekta svaret
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
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }
}
