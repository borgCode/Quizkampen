package client.gui.windows;

import client.gui.WindowManager;
import server.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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
    private List<JLabel> categories = new ArrayList<>();
    private int currentRound = 1;
    private JLabel score;
    private boolean hasClickedPlay;
    private JButton playButton;
    private JLabel categoryLabel;
    private WindowManager windowManager;
    private JPanel cardPanel;
    private JButton playAgainButton;

    public ScoreWindow(WindowManager windowManager) {
        this.windowManager = windowManager;
    }


    public void initScoreWindow() {
        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);
        setFocusable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

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
        JPanel player1Panel = getPlayerPanel(player1);

        // Poäng
        score = new JLabel(playerCurrentScore + " - " + playerOpponentScore, SwingConstants.CENTER);
        score.setFont(new Font("Arial", Font.BOLD, 32));

        //Avatar och spelare nr 2
        JPanel player2Panel = getPlayerPanel(player2);

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
        JPanel rondPanel = getRoundPanel();


        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(rondPanel, BorderLayout.CENTER);

        

        
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(getBottomPanel(), "bottomPanel");
        cardPanel.add(getBottomPanelEnd(), "bottomEndPanel");

        panel.add(cardPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    private JPanel getPlayerPanel(Player player) {
        JButton avatar1 = new JButton(player.getAvatar(player.getAvatarPath()));
        avatar1.setContentAreaFilled(false); // Tar bort bakgrund
        avatar1.setBorder(BorderFactory.createEmptyBorder()); // Tar bort kantlinje
        avatar1.setFocusable(false);
        avatar1.setOpaque(false);

        JLabel name1 = new JLabel(player.getName(), SwingConstants.CENTER);
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setOpaque(false);
        playerPanel.add(avatar1, BorderLayout.NORTH);
        playerPanel.add(name1, BorderLayout.CENTER);
        return playerPanel;
    }


    private JPanel getRoundPanel() {
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

            JPanel rondLabelPanel = new JPanel();

            rondLabelPanel.setLayout(new BoxLayout(rondLabelPanel, BoxLayout.Y_AXIS));

            rondLabelPanel.setOpaque(false); // Gör bakgrunden genomskinlig

            // Första JLabel
            JLabel rondLabel = new JLabel("Rond " + i, SwingConstants.CENTER);
            rondLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rondLabelPanel.add(rondLabel);

            //Kategori JLabel
            categoryLabel = new JLabel(" ", SwingConstants.CENTER);
            categoryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            rondLabelPanel.add(categoryLabel);
            categories.add(categoryLabel);

            // Lägg till panelen i radpanelen
            rowPanel.add(rondLabelPanel);


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
        return rondPanel;
    }

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        JButton giveUpButton = new JButton("Ge upp");
        giveUpButton.addActionListener(e -> this.hasUserGivenUp = true);
        playButton = new JButton("Spela");
        playButton.addActionListener(e -> {
            this.hasClickedPlay = true;
            playButton.setEnabled(false);
        });
        playButton.setEnabled(true);
        bottomPanel.add(giveUpButton);
        bottomPanel.add(playButton);
        return bottomPanel;
    }

    private JPanel getBottomPanelEnd() {

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        JButton menuButton = new JButton("Meny");
        menuButton.addActionListener(e -> {
            windowManager.getNetworkHandler().sendPlayAgainDenied();
            windowManager.backToMenu();
            dispose();
        });
        
        playAgainButton = new JButton("Spela igen");
        playAgainButton.addActionListener(e -> {
            windowManager.getNetworkHandler().sendPlayAgainSignal();
            playAgainButton.setEnabled(false);
        });
        
        playAgainButton.setEnabled(true);
        bottomPanel.add(menuButton);
        bottomPanel.add(playAgainButton);
        return bottomPanel;
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
        List<JButton> player1Row = player1buttons.get(currentRound - 1);
        int player1Score = 0;

        for (int i = 0; i < 3; i++) {
            JButton button = player1Row.get(i);
            button.setIcon(scoreList.get(i) == 1 ? checkImageIcon : crossImageIcon);
            button.setEnabled(true);
            player1Score += scoreList.get(i);
        }
        this.playerCurrentScore += player1Score;
        updateRounds();
        hasUpdatedPlayerScore = true;
    }

    public void updateOpponentScore(List<Integer> opponentScore) {
        List<JButton> player2Row = player2buttons.get(currentRound - 1);
        int player2Score = 0;

        for (int i = 0; i < 3; i++) {
            JButton button = player2Row.get(i);
            button.setIcon(opponentScore.get(i) == 1 ? checkImageIcon : crossImageIcon);
            button.setEnabled(true);
            player2Score += opponentScore.get(i);
        }
        this.playerOpponentScore += player2Score;
        updateRounds();
        hasUpdatedOpponentScore = true;
    }


    public void nextRound() {
        if (hasUpdatedPlayerScore && hasUpdatedOpponentScore) {
            currentRound++;
            hasUpdatedPlayerScore = false;
            hasUpdatedOpponentScore = false;
        }
    }

    public void setHasClickedPlay(boolean hasClickedPlay) {
        this.hasClickedPlay = hasClickedPlay;
    }

    public void updateRounds() {
        score.setText(playerCurrentScore + " - " + playerOpponentScore);
        score.setFont(new Font("Arial", Font.BOLD, 32));
        score.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void resetScoreList() {
        this.playerCurrentScore = 0;
        this.playerOpponentScore = 0;
        this.currentRound = 1;

        for (List<JButton> buttonRow : player1buttons) {
            for (JButton button : buttonRow) {
                button.setIcon(null);
                button.setEnabled(true);
            }
        }

        for (List<JButton> buttonRow : player2buttons) {
            for (JButton button : buttonRow) {
                button.setIcon(null);
                button.setEnabled(true);
            }
        }
        
        for (JLabel categoryLabel : categories) {
            categoryLabel.setText("");
        }
        
        playAgainButton.setEnabled(true);
        
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "bottomPanel");

        updateRounds();
    }

    public void setPlayButtonIsEnabled(boolean isEnabled) {
        if (isEnabled) {
            playButton.setText("Spela");
            playButton.setEnabled(true);
        } else {
            playButton.setText("Väntar");
            playButton.setEnabled(false);
        }
        
    }


    public void updateCategory(String currentCategory) {
        categories.get(currentRound - 1).setText(currentCategory);

    }

    public void switchBottomPanel() {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, "bottomEndPanel");
    }
    

    public void setHasUserGivenUp(boolean hasUserGivenUp) {
        this.hasUserGivenUp = hasUserGivenUp;
    }
}
