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

    public CategoryWindow() {

        initializeCategoryData();

        // Slumpar kategorierna och väljer de tre första efter slumpandet
        Collections.shuffle(categories);
        selectedCategories = new ArrayList<>(categories.subList(0, 3));

        setTitle("Kategori");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(topLabel, BorderLayout.NORTH);

        categoryPanel.setLayout(new GridLayout(3, 1, 10, 10));
        add(categoryPanel, BorderLayout.CENTER);

        // Lägger till valda kategorier i panel
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

        // Hämtar sökvägen för kategorin
        String imagePath = categoryImages.get(category);
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        // MouseListener för att hantera klick på bilden
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new GameWindow();
                dispose();
            }
        });

        // Label för kategorinamnet
        JLabel nameLabel = new JLabel(category);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        categoryContainer.add(imageLabel, BorderLayout.CENTER);
        categoryContainer.add(nameLabel, BorderLayout.SOUTH);

        categoryPanel.add(categoryContainer);
    }

    public static void main(String[] args) {
        new CategoryWindow();
    }
}
