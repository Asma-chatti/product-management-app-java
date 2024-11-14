package categorie;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddCategoryGUI extends JFrame implements ActionListener {
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton;

    public AddCategoryGUI() {
        setTitle("Add Category");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        addButton = new JButton("Add");

        addButton.addActionListener(this);

        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(new JLabel()); // Placeholder for alignment
        mainPanel.add(addButton);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addCategory();
        }
    }

    private void addCategory() {
        String name = nameField.getText();

        // Validation du nom de la catégorie
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Créer un objet Category avec le nom saisi
        Categories category = new Categories(name);

        // Utiliser CategoryDAO pour ajouter la catégorie dans la base de données
        CategoryDAO categoryDAO = new CategoryDAO();
        try {
            categoryDAO.addCategory(category);
            JOptionPane.showMessageDialog(this, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            new ViewCategoriesGUI();
            dispose(); // Fermer la fenêtre après l'ajout de la catégorie
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

