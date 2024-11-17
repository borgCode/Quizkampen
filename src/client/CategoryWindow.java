package client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryWindow extends JFrame implements ActionListener {
    JPanel topPanel = new JPanel();
    JLabel topLabel = new JLabel("Välj en katergori:");
    JPanel categoryPanel = new JPanel();
    JButton category1 = new JButton("TEXT");
    JButton category2 = new JButton("TEXT");
    JButton category3 = new JButton("TEXT");

    CategoryWindow() {

        topPanel.setLayout(new BorderLayout());
        topPanel.setSize(200,200);
        add(topPanel, BorderLayout.NORTH);
        topPanel.add(topLabel, BorderLayout.CENTER);


        setTitle("Kategori");
        categoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(categoryPanel,BorderLayout.CENTER);
        categoryPanel.add(category1);
        categoryPanel.add(category2);
        categoryPanel.add(category3);

        category1.setPreferredSize(new Dimension(200, 100));
        category2.setPreferredSize(new Dimension(200, 100));
        category3.setPreferredSize(new Dimension(200, 100));


        category1.addActionListener(this);
        category2.addActionListener(this);
        category3.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(400, 400);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == category1) {
            new GameWindow();
        } else if (e.getSource() == category2) {
            new GameWindow();
        } else if (e.getSource() == category3) {
            new GameWindow();
        }
        this.dispose();
    }

    public static void main(String[] args) {
        CategoryWindow window = new CategoryWindow();
    }

}