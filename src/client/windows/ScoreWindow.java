package client.windows;

import server.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreWindow extends JFrame {

    private Player player1, player2;
    private int rounds;
    private ImageIcon checkImageIcon;
    private ImageIcon crossImageIcon;
    private boolean hasUserGivenUp;
    private int gritLayoutRounds;
    private int playerCurrentScore = 0;
    private int playerOpponentScore = 0;
    private boolean hasUpdatedPlayerScore;
    private boolean hasUpdatedOpponentScore;
    private List<List<JButton>> player1buttons = new ArrayList<>();
    private List<List<JButton>> player2buttons = new ArrayList<>();
    private int currentRound = 1;
    private boolean hasClickedPlay;
    

   
    public void initScoreWindow() {
        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        //TODO: KONTROLLERA STORLEK!!!!!!
        //Sätter ikoner för rätt och fel
        checkImageIcon = new ImageIcon("src/resources/images/check.png");
        Image scaledCheckImage = checkImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        checkImageIcon = new ImageIcon(scaledCheckImage);

        crossImageIcon = new ImageIcon("src/resources/images/cross.png");
        Image scaledCrossImage = crossImageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        crossImageIcon = new ImageIcon(scaledCrossImage);

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        //För att lägga alla komponenter ovanför
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        backgroundLabel.add(panel, BorderLayout.CENTER);

        //topPanel
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.setOpaque(false);


        //Avatar och spelare nr 1
        JButton avatar1 = new JButton(player1.getAvatar());
        avatar1.setContentAreaFilled(false); // Tar bort bakgrund
        avatar1.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje
        avatar1.setFocusable(false);
        avatar1.setOpaque(false);

        JLabel name1 = new JLabel(player1.getName(), SwingConstants.CENTER);
        JPanel player1Panel = new JPanel(new BorderLayout());
        player1Panel.setOpaque(false);
        player1Panel.add(avatar1, BorderLayout.NORTH);
        player1Panel.add(name1, BorderLayout.CENTER);

        // Poäng
        //TODO :OBS! HÅRDKODAT POÄNGEN!!
        JLabel score = new JLabel(playerCurrentScore + " - " + playerOpponentScore, SwingConstants.CENTER);
        score.setFont(new Font("Arial", Font.BOLD, 32));

        //Avatar och spelare nr 2
        JButton avatar2 = new JButton(player2.getAvatar());
        avatar2.setContentAreaFilled(false); // Tar bort bakgrund
        avatar2.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje
        avatar2.setFocusable(false);
        avatar2.setOpaque(false);

        JLabel name2 = new JLabel(player2.getName(), SwingConstants.CENTER);
        JPanel player2Panel = new JPanel(new BorderLayout());
        player2Panel.setOpaque(false);
        player2Panel.add(avatar2, BorderLayout.NORTH);
        player2Panel.add(name2, BorderLayout.CENTER);

        //Lägg till spelare och poäng i top-panelen
        topPanel.add(player1Panel);
        topPanel.add(score);
        topPanel.add(player2Panel);
        topPanel.setOpaque(false);
        panel.add(topPanel, BorderLayout.NORTH);

        //Metod som kontrollerar storleken på rutorna
        if (rounds > 6) {
            gritLayoutRounds = rounds;
        } else
            gritLayoutRounds = 6;



        //Mitt panelen med ronderna
        JPanel rondPanel = new JPanel(new GridLayout(gritLayoutRounds, 1));
        rondPanel.setOpaque(false);
        for (int i = 1; i <= this.rounds; i++) {
            //7 kolumner motsvarar: 3 knappar + RondNr + 3 Knappar
            JPanel rowPanel = new JPanel(new GridLayout(1, 7));
            rowPanel.setOpaque(false);

            //Lista för att lägga in resultat i 3 knappar
            List<JButton> rowPlayer1 = new ArrayList<>();
            List<JButton> rowPlayer2 = new ArrayList<>();


            //Knappar spelare 1:
            for (int j = 1; j <= 3; j++) {
                JButton buttonPlayer1 = new JButton();
                buttonPlayer1.setEnabled(false);
                rowPanel.add(buttonPlayer1);
                rowPlayer1.add(buttonPlayer1);

            }
            
            player1buttons.add(rowPlayer1);

            //Rond
            JLabel rondLabel = new JLabel("Rond " + i, SwingConstants.CENTER);
            rondLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rowPanel.add(rondLabel);

            //Knappar spelare 2:
            for (int u = 1; u <= 3; u++) {
                JButton buttonPlayer2 = new JButton();
                buttonPlayer2.setEnabled(false);
                rowPanel.add(buttonPlayer2);
                rowPlayer2.add(buttonPlayer2);
            }
            player2buttons.add(rowPlayer2);

            //Lägg till i panel rondPanel
            rondPanel.add(rowPanel);
        }

        //Nedre panel för knappar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        JButton giveUpButton = new JButton("Ge upp");
        giveUpButton.addActionListener(e -> this.hasUserGivenUp = true);
        JButton playButton = new JButton("Spela");
        playButton.addActionListener(e -> this.hasClickedPlay = true);
        playButton.setEnabled(true);
        bottomPanel.add(giveUpButton);
        bottomPanel.add(playButton);


        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(rondPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public boolean hasUserGivenUp() {
        return hasUserGivenUp;
    }
    
    public boolean hasClickedPlay() {
        return hasClickedPlay;
    }


    public void updatePlayerScore(List<Integer> scoreList) {
            List<JButton> player1Row = player1buttons.get(currentRound -1);
        for (int i = 0; i < 3; i++) {
            JButton button = player1Row.get(i);
            button.setIcon(scoreList.get(i) == 1 ?  checkImageIcon : crossImageIcon);
            button.setEnabled(true);
        }
        hasUpdatedPlayerScore = true;
    }

    public void updateOpponentScore(List<Integer> opponentScore) {
        List<JButton> player2Row = player2buttons.get(currentRound -1);

        for (int i = 0; i < 3; i++) {
            JButton button = player2Row.get(i);
            button.setIcon(opponentScore.get(i) == 1 ?  checkImageIcon : crossImageIcon);
            button.setEnabled(true);
        }
        hasUpdatedOpponentScore = true;
    }
    

    public void nextRound() {
        if (hasUpdatedPlayerScore && hasUpdatedOpponentScore) {
            currentRound++;
            hasUpdatedPlayerScore = false;
            hasUpdatedOpponentScore = false;
        }
    }
}
