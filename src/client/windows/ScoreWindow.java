package client.windows;

import server.entity.Player;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JFrame {

    private Player player1, player2;
    private int rounds;
    private boolean hasUserGivenUp;

   
    public void initScoreWindow() {
        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

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


        //Mitt panelen med ronderna
        JPanel rondPanel = new JPanel(new GridLayout(this.rounds, 1)); //TODO: 6 ändra till rounds
        rondPanel.setOpaque(false);
        for (int i = 1; i <= this.rounds; i++) { //TODO: 6 ändra till rounds
            // 7 kolumner motsvarar: 3 knappar + RondNr + 3 Knappar
            JPanel rowPanel = new JPanel(new GridLayout(1, 7));
            rowPanel.setOpaque(false);


            //Knappar spelare 1:
            for (int j = 1; j <= 3; j++) {
                JButton buttonPlayer1 = new JButton();
                buttonPlayer1.setEnabled(false);
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
                rowPanel.add(buttonPlayer2);
            }

            //Lägg till i panel rondPanel
            rondPanel.add(rowPanel);
        }

        //TODO: Nedre panel för knappar
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        JButton giveUpButton = new JButton("Ge upp");
        giveUpButton.addActionListener(e -> this.hasUserGivenUp = true);
        JButton nextRoundButton = new JButton("Nästa rond");
        nextRoundButton.setEnabled(false);
        bottomPanel.add(giveUpButton);
        bottomPanel.add(nextRoundButton);


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

    public static void main(String[] args) {
        //TODO: OBS HÅRDKODAT. Dessa ska man få från StartWindow
        Player player1 = new Player("Spelare 1",new ImageIcon("src/resources/avatars/Gengar.png"));
        Player player2 = new Player("Spelare 2",new ImageIcon("src/resources/avatars/Poliwag.png"));
        int rounds = 6;
        ScoreWindow scoreWindow = new ScoreWindow();
    }

    
}
