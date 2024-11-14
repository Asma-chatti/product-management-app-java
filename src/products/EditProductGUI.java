package products;

import javax.swing.*;

import user.User;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class EditProductGUI extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField quantityField;
    private JButton saveButton;
    private Product product;

    public EditProductGUI(Product product) {
        this.product = product;

        setTitle("Edit Product");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 5, 5));

        mainPanel.add(new JLabel("Name:"));
        nameField = new JTextField(product.getName());
        mainPanel.add(nameField);

        mainPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField(product.getDescription());
        mainPanel.add(descriptionField);

        mainPanel.add(new JLabel("Price:"));
        priceField = new JTextField(String.valueOf(product.getPrice()));
        mainPanel.add(priceField);

        mainPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(String.valueOf(product.getQuantity()));
        mainPanel.add(quantityField);

        saveButton = new JButton("Save Changes");
        saveButton.addActionListener(this);
        mainPanel.add(saveButton);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Mettre à jour les valeurs du produit
            product.setName(nameField.getText());
            product.setDescription(descriptionField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setQuantity(Integer.parseInt(quantityField.getText()));

            // Mettre à jour le produit dans la base de données
            ProductDAO productDAO = new ProductDAO();
            try {
                productDAO.updateProduct(product);
                JOptionPane.showMessageDialog(this, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                User user=null;
                new ManageProductsGUI(user);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
