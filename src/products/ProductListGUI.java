package products;

import javax.swing.*;
import javax.swing.table.*;

import user.User;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.module.FindException;
import java.sql.*;

public class ProductListGUI extends JFrame {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryComboBox;
    private JButton filterButton;
    private JButton viewOrdersButton; // Bouton pour consulter les commandes de l'utilisateur
    private User user;

    public ProductListGUI(User user) {
        this.user = user;
        setTitle("Liste de Produits");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        categoryComboBox = new JComboBox<>();
        filterPanel.add(categoryComboBox);

        filterButton = new JButton("Filtrer");
        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchProductsFromDatabase();
            }
        });
        filterPanel.add(filterButton);

        getContentPane().add(filterPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Seule la dernière colonne contenant les boutons est éditable
                return column == getColumnCount() - 1;
            }
        };
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Description");
        tableModel.addColumn("Prix");
        tableModel.addColumn("Quantité");
        tableModel.addColumn("Catégorie");
        tableModel.addColumn("Action");

        // Bouton d'édition des commandes
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), tableModel);

        productTable = new JTable(tableModel);
        productTable.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setCellEditor(buttonEditor);

        JScrollPane scrollPane = new JScrollPane(productTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Ajout du bouton pour consulter les commandes
        JPanel bottomPanel = new JPanel(new BorderLayout());
        viewOrdersButton = new JButton("Consulter les commandes");
        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action à effectuer lors du clic sur le bouton
                viewOrders();
            }
        });
        bottomPanel.add(viewOrdersButton, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        fetchCategoriesFromDatabase(); // Charger les catégories disponibles
        fetchProductsFromDatabase(); // Afficher tous les produits par défaut

        setVisible(true);
    }
    
    
    private int orderId = 0; // Déclaration des variables en tant qu'attributs de classe
    private double totalAmount = 0;
    private Timestamp orderDate = null;

    private void viewOrders() {
        try {
            // Connexion à la base de données (remplacez ces valeurs par les vôtres)
            String url = "jdbc:mysql://localhost:3306/products";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Requête pour récupérer les commandes de l'utilisateur depuis la base de données
            String query = "SELECT * FROM orders WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, 2);
            ResultSet resultSet = statement.executeQuery();

            // Afficher les commandes de l'utilisateur dans une boîte de dialogue
            StringBuilder ordersText = new StringBuilder();
            while (resultSet.next()) {
                orderId = resultSet.getInt("id");
                totalAmount = resultSet.getDouble("total_amount");
                orderDate = resultSet.getTimestamp("order_date");

                ordersText.append("Commande ID: ").append(orderId).append(", Montant total: ").append(totalAmount)
                        .append(", Date de commande: ").append(orderDate).append("\n");
            }

            if (ordersText.length() == 0) {
                JOptionPane.showMessageDialog(this, "L'utilisateur n'a aucune commande.", "Aucune commande",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JPanel dialogPanel = new JPanel(new BorderLayout());
                JTextArea textArea = new JTextArea(ordersText.toString());
                textArea.setEditable(false);
                dialogPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

                // Ajout du bouton pour exporter la facture
                JButton exportInvoiceButton = new JButton("Exporter Facture");
                exportInvoiceButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        exportInvoice(orderId, totalAmount, orderDate);
                    }
                });
                dialogPanel.add(exportInvoiceButton, BorderLayout.SOUTH);

                JOptionPane.showMessageDialog(this, dialogPanel, "Commandes de l'utilisateur",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des commandes de l'utilisateur.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportInvoice(int orderId, double totalAmount, Timestamp orderDate) {
        try {
            // Génération du contenu HTML de la facture
            String invoiceContent = generateInvoiceHTML(orderId, totalAmount, orderDate);

            // Écriture du contenu dans un fichier HTML
            String fileName = "invoice.html";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(invoiceContent);
            writer.close();

            JOptionPane.showMessageDialog(this, "La facture a été exportée avec succès.", "Exportation Réussie",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation de la facture.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateInvoiceHTML(int orderId, double totalAmount, Timestamp orderDate) {
        // Génération du contenu HTML de la facture
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Facture</title></head><body>");
        htmlContent.append("<h1>Facture</h1>");
        // Ajoutez ici les détails de la facture en HTML
        htmlContent.append("<p>Commande ID: ").append(orderId).append(", Montant total: ").append(totalAmount)
                .append(", Date de commande: ").append(orderDate).append("</p>");
        htmlContent.append("</body></html>");
        return htmlContent.toString();
    }





    private void fetchCategoriesFromDatabase() {
        try {
            // Connexion à la base de données (remplacez ces valeurs par les vôtres)
            String url = "jdbc:mysql://localhost:3306/products";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Requête pour récupérer les catégories depuis la base de données
            String query = "SELECT DISTINCT category FROM products";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Ajouter les catégories à la liste déroulante
            categoryComboBox.addItem("Toutes"); // Ajoutez "Toutes" en premier
            while (resultSet.next()) {
                String category = resultSet.getString("category");
                categoryComboBox.addItem(category);
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des catégories depuis la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void fetchProductsFromDatabase() {
        // Effacer le tableau existant
        tableModel.setRowCount(0);

        try {
            // Connexion à la base de données (remplacez ces valeurs par les vôtres)
            String url = "jdbc:mysql://localhost:3306/products";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);

            // Requête pour récupérer les produits depuis la base de données avec ou sans filtre par catégorie
            String categoryFilter = (String) categoryComboBox.getSelectedItem();
            String query = "SELECT id, name, description, price, quantity, category FROM products";
            if (!categoryFilter.equals("Toutes")) {
                query += " WHERE category = ?";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            if (!categoryFilter.equals("Toutes")) {
                statement.setString(1, categoryFilter);
            }
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les données des produits au tableau
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String category = resultSet.getString("category");
                tableModel.addRow(new Object[]{id, name, description, price, quantity, category, "Acheter"});
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des produits depuis la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Les classes ButtonRenderer et ButtonEditor restent inchangées
}


    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int rowIndex;
        private DefaultTableModel tableModel; // Ajouter cette ligne

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel) {
            super(checkBox);
            this.tableModel = tableModel; // Assigner le modèle de tableau
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }


        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            rowIndex = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                try {
                    int productId = (int) tableModel.getValueAt(rowIndex, 0);
                    int currentQuantity = (int) tableModel.getValueAt(rowIndex, 4);
                    if (currentQuantity > 0) {
                        int userId = 2; // Exemple d'identifiant d'utilisateur
                        ProductDAO productDAO = new ProductDAO();
                        
                        // Mettre à jour la quantité dans la base de données
                        productDAO.updateProductQuantity(productId, currentQuantity - 1);
                        
                        // Créer une nouvelle commande dans la table orders
                        OrderDAO orderDAO = new OrderDAO();
                        
                        // Récupérer le prix du produit depuis le modèle de tableau (colonne 3)
                        double unitPrice = (Double) tableModel.getValueAt(rowIndex, 3);
                        
                        // Créer une nouvelle commande en utilisant l'identifiant de l'utilisateur, la quantité actuelle et le prix unitaire du produit
                        int orderId = orderDAO.createOrder(userId, currentQuantity, productId, unitPrice);
                        
                        if (orderId != -1) {
                            // Mettre à jour la quantité dans la table
                            tableModel.setValueAt(currentQuantity - 1, rowIndex, 4);
                            JOptionPane.showMessageDialog(button, "Commande passée avec succès !");
                        } else {
                            JOptionPane.showMessageDialog(button, "Erreur lors de la création de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(button, "La quantité de ce produit est épuisée.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(button, "Erreur lors de la mise à jour de la quantité du produit ou de la création de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
