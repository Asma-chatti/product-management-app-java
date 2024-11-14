package products;

import javax.swing.*;

import user.User;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ManageProductsGUI extends JFrame implements ActionListener {
    private User loggedInUser;
    private JPanel productPanel;
    private JButton addButton;

    public ManageProductsGUI(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        setTitle("Manage Products");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Panel pour afficher la liste des produits
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bouton pour ajouter un produit
        addButton = new JButton("Add Product");
        addButton.addActionListener(this);
        mainPanel.add(addButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Charger et afficher les produits
        displayProducts();
    }

    private void displayProducts() {
        // Récupérer la liste des produits depuis la base de données
        ProductDAO productDAO = new ProductDAO();
        List<Product> productList = null;
        try {
            productList = productDAO.getAllProducts();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Nettoyer le panneau des produits
        productPanel.removeAll();

        // Ajouter chaque produit au panneau des produits
        for (Product product : productList) {
            JPanel productEntryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            productEntryPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            productEntryPanel.setPreferredSize(new Dimension(350, 50));

            JLabel nameLabel = new JLabel("Name: " + product.getName());
            JLabel priceLabel = new JLabel("Price: $" + product.getPrice());
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");

            editButton.addActionListener(e -> {
                // Insérer ici le code pour éditer le produit
                // Par exemple :
                new EditProductGUI(product);
            });

            deleteButton.addActionListener(e -> {
                // Insérer ici le code pour supprimer le produit
                // Par exemple :
                try {
                    int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce produit ?", "Supprimer le produit", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        productDAO.deleteProduct(product.getId());
                        // Actualiser la liste des produits
                        displayProducts();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du produit", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });


            productEntryPanel.add(nameLabel);
            productEntryPanel.add(priceLabel);
            productEntryPanel.add(editButton);
            productEntryPanel.add(deleteButton);

            productPanel.add(productEntryPanel);
        }

        // Rafraîchir l'affichage
        productPanel.revalidate();
        productPanel.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Insérer ici le code pour ajouter un produit
            // Par exemple :
             new AddProductGUI();
            // Actualiser la liste des produits
            displayProducts();
        }
    }

}

