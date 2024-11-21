package client;

import server.entity.Game;
import server.entity.Player;
import server.network.PropertiesManager;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JFrame {

    private Player player1, player2;
    private int rounds = 0;
    private int gritLayoutRounds;
    private ImageIcon checkImageIcon;
    private ImageIcon crossImageIcon;
    private JButton nextRoundButton;
    private JPanel rondPanel;
    private Game game;


    public ScoreWindow(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rounds = PropertiesManager.totalRoundsSet();
        System.out.println(player1.getName() + " " + player2.getName());

        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        // Sätt ikoner för rätt och fel
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


        //TODO: TOP PANEL
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.setOpaque(false);


        //TODO: Avatar och spelare nr 1
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


        // TODO: Poäng
        //TODO :OBS! HÅRDKODAT POÄNGEN!!
        JLabel score = new JLabel("0 - 0", SwingConstants.CENTER);
        score.setFont(new Font("Arial", Font.BOLD, 32));


        // TODO: Avatar och spelare nr 2
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


        // Lägg till spelare och poäng i top-panelen
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
        rondPanel = new JPanel(new GridLayout(gritLayoutRounds, 1));
        rondPanel.setOpaque(false);


        for (int i = 1; i <= rounds; i++) {
            // 7 kolumner motsvarar: 3 knappar + RondNr + 3 Knappar
            JPanel rowPanel = new JPanel(new GridLayout(1, 7));
            rowPanel.setOpaque(false);


            //Knappar spelare 1:
            for (int j = 1; j <= 3; j++) {
                JButton buttonPlayer1 = new JButton();
                buttonPlayer1.setEnabled(false);
                //buttonPlayer1.setIcon(game.getPlayer1Score() == 1 ? checkImageIcon : crossImageIcon); //TODO
                rowPanel.add(buttonPlayer1);
            }

            //Rond
            JLabel rondLabel = new JLabel("Rond " + i, SwingConstants.CENTER);
            rondLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rowPanel.add(rondLabel);

            //Knappar spelare 2:
            for (int u = 1; u <= 3; u++) {
                JButton buttonPlayer2 = new JButton();
                buttonPlayer2.setEnabled(false);
                //buttonPlayer2.setIcon(game.getPlayer2Score() == 1 ? checkImageIcon : crossImageIcon); //TODO
                rowPanel.add(buttonPlayer2);
            }

            //Lägg till i panel rondPanel
            rondPanel.add(rowPanel);
        }

        //TODO: Nedre panel för knappar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        JButton giveUpButton = new JButton("Ge upp");
        nextRoundButton = new JButton("Nästa rond");
        nextRoundButton.setEnabled(false);
        bottomPanel.add(giveUpButton);
        bottomPanel.add(nextRoundButton);


        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(rondPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);


    }

    // Getter för knappen
    public JButton getNextRoundButton() {
        return nextRoundButton;
    }

    //TODO:METOD
    /*

    public void updateScores(List<Integer> player1Scores, List<Integer> player2Scores) {

        for (int i = 0; i < rounds; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 7));
            rowPanel.setOpaque(false);

            // Lägg till knappar för spelare 1
            for (int j = 0; j < 3; j++) {
                JButton buttonPlayer1 = new JButton();
                buttonPlayer1.setEnabled(false);
                buttonPlayer1.setIcon(player1Scores.get(i) == 1 ? checkImageIcon : crossImageIcon);
                rowPanel.add(buttonPlayer1);
            }

            // Lägg till rondnummer
            JLabel rondLabel = new JLabel("Rond " + (i + 1), SwingConstants.CENTER);
            rondLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rowPanel.add(rondLabel);

            // Lägg till knappar för spelare 2
            for (int j = 0; j < 3; j++) {
                JButton buttonPlayer2 = new JButton();
                buttonPlayer2.setEnabled(false);
                buttonPlayer2.setIcon(player2Scores.get(i) == 1 ? checkImageIcon : crossImageIcon);
                rowPanel.add(buttonPlayer2);
            }

            // Lägg till raden i rondPanel
            rondPanel.add(rowPanel);
        }

        // Uppdatera GUI
        rondPanel.revalidate();
        rondPanel.repaint();
        nextRoundButton.setEnabled(true); // Aktivera "Nästa rond"-knappen
    }
}


     */


   public static void main(String[] args) {
        //TODO: OBS HÅRDKODAT. Dessa ska man få från StartWindow
        Player player1 = new Player("Spelare 1",new ImageIcon("src/resources/avatars/Gengar.png"));
        Player player2 = new Player("Spelare 2",new ImageIcon("src/resources/avatars/Poliwag.png"));
        ScoreWindow scoreWindow = new ScoreWindow( player1,  player2);
    }
}
