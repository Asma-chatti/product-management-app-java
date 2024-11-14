package products;

import javax.swing.*;

import categorie.Categories;
import categorie.CategoryDAO;
import user.User;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class AddProductGUI extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField quantityField;
    private JComboBox<Categories> categoryComboBox;
    private JButton addButton;

    public AddProductGUI() {
        setTitle("Add Product");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 5, 5));

        mainPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        mainPanel.add(descriptionField);

        mainPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        mainPanel.add(priceField);

        mainPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        mainPanel.add(quantityField);

        mainPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        mainPanel.add(categoryComboBox);

        addButton = new JButton("Add Product");
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        add(mainPanel);
        setVisible(true);

        // Charger les catégories depuis la base de données
        loadCategories();
    }

    private void loadCategories() {
        // Récupérer les catégories depuis la base de données
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Categories> categories = null;
        try {
            categories = categoryDAO.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ajouter les noms de catégories à la liste déroulante
        for (Categories category : categories) {
            categoryComboBox.addItem(category);
        }
    }


    

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Récupérer les valeurs des champs
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            Categories selectedCategory = (Categories) categoryComboBox.getSelectedItem();

            // Créer un nouvel objet Product
            Product product = new Product(name, description, price, quantity, selectedCategory.getName());

            // Insérer le produit dans la base de données
            ProductDAO productDAO = new ProductDAO();
            try {
                productDAO.addProduct(product);
                JOptionPane.showMessageDialog(this, "Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
            }
            User user=null;
            new ManageProductsGUI(user);
            dispose();
        }
    }

}
