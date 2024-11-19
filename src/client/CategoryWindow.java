package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CategoryWindow extends JFrame {

    private JPanel categoryPanel = new JPanel();
    private JLabel topLabel = new JLabel("Välj en kategori:");
    private HashMap<String, String> categoryImages;
    private ArrayList<String> categories;
    private ArrayList<String> selectedCategories;
    private String selectedCategory;

    public CategoryWindow(ArrayList<String> categories) {
        this.selectedCategories = categories; // Tar emot kategorier från klienten
        initializeCategoryData();

        // Slumpar kategorierna och väljer de tre första efter slumpandet
        //Collections.shuffle(this.categories);
        //selectedCategories = new ArrayList<>(this.categories.subList(0, Math.min(3, this.categories.size())));

        setTitle("QuizKampen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);
        setLayout(null);

        // Lägger till bakgrundsbilden
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/categoryImages/unknownAura.jpg"));
        backgroundLabel.setBounds(0, 0, 400, 500);
        add(backgroundLabel);

        // Lägger till de andra komponenterna ovanpå bakgrundsbilden
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topLabel.setBounds(50, 20, 300, 40);
        backgroundLabel.add(topLabel);

        categoryPanel.setLayout(new GridLayout(3, 1, 20, 20));
        categoryPanel.setOpaque(false); // Gör panelen genomskinlig
        categoryPanel.setBounds(50, 80, 300, 360);
        backgroundLabel.add(categoryPanel);

        // Lägger till valda kategorier i panelen
        for (String category : selectedCategories) {
            addCategoryToPanel(category);
        }

        setVisible(true);
    }

    // Metod för att initialisera kategoridatan
    private void initializeCategoryData() {
        categoryImages = new HashMap<>();
        categories = new ArrayList<>();

        // Lägger till kategorier med rätt sökväg
        categoryImages.put("Geografi", "src/resources/categoryImages/Geografi.jpg");
        categoryImages.put("Vetenskap", "src/resources/categoryImages/Vetenskap.jpg");
        categoryImages.put("Historia", "src/resources/categoryImages/Historia.jpg");
        categoryImages.put("Nöje", "src/resources/categoryImages/Nöje.jpg");
        categoryImages.put("TV-serier", "src/resources/categoryImages/TV-serier.jpg");
        categoryImages.put("TV-spel", "src/resources/categoryImages/TV-spel.jpg");
        categoryImages.put("Mat", "src/resources/categoryImages/Mat.jpg");
        categoryImages.put("Litteratur", "src/resources/categoryImages/Litteratur.jpg");
        categoryImages.put("Sport", "src/resources/categoryImages/Sport.jpg");

        categories.addAll(categoryImages.keySet());
    }

    // Metod för att lägga till en kategori till panelen
    private void addCategoryToPanel(String category) {
        JPanel categoryContainer = new JPanel();
        categoryContainer.setLayout(new BorderLayout());
        categoryContainer.setOpaque(false); // Gör panelen genomskinlig

        // Hämtar sökvägen för kategorin
        String imagePath = categoryImages.get(category);
        ImageIcon icon = new ImageIcon(imagePath);

        // Ändrar bildens storlek för att passa i fönstret
        Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        // MouseListener för att hantera klick på bilden
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCategory = category; // Sparar vald kategori
                dispose(); // Stäng fönstret
            }
        });

        // Label för kategorinamnet
        JLabel nameLabel = new JLabel(category);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        categoryContainer.add(imageLabel, BorderLayout.CENTER);
        categoryContainer.add(nameLabel, BorderLayout.SOUTH);

        categoryPanel.add(categoryContainer);
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }
}
