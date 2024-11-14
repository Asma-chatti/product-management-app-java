package categorie;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;

public class ViewCategoriesGUI extends JFrame implements ActionListener{
    private JTable categoriesTable;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public ViewCategoriesGUI() {
        setTitle("List of Categories");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer le modèle de tableau pour les catégories
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");

        // Récupérer la liste des catégories depuis la base de données
        List<categorie.Categories> categories = null;
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categories = categoryDAO.getAllCategories();
            for (categorie.Categories category : categories) {
                tableModel.addRow(new Object[]{category.getId(), category.getName()});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching categories list", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Fermer la fenêtre en cas d'erreur
            return;
        }

        // Créer le tableau graphique avec les données des catégories
        categoriesTable = new JTable(tableModel);

        // Ajouter le tableau dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(categoriesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Ajouter le panneau de CRUD
        JPanel crudPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);
        mainPanel.add(crudPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        setVisible(true);
    }


    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            new AddCategoryGUI();
        } else if (e.getSource() == editButton) {
            int selectedRow = categoriesTable.getSelectedRow(); // Récupérer l'index de la ligne sélectionnée dans le tableau

            if (selectedRow != -1) { // Vérifier si une ligne est sélectionnée
                int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0); // Récupérer l'ID de la catégorie à partir du tableau
                String categoryName = (String) categoriesTable.getValueAt(selectedRow, 1); // Récupérer le nom de la catégorie à partir du tableau
                Categories category = new Categories(categoryName);
                new EditCategoryGUI(category); // Ouvrir la fenêtre d'édition avec la catégorie sélectionnée
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to edit", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = categoriesTable.getSelectedRow(); // Récupérer l'index de la ligne sélectionnée dans le tableau

            if (selectedRow != -1) { // Vérifier si une ligne est sélectionnée
                int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0); // Récupérer l'ID de la catégorie à partir du tableau
                    DefaultTableModel model = (DefaultTableModel) categoriesTable.getModel();
                    model.removeRow(selectedRow); // Supprimer la ligne du tableau graphique

                    // Implémentez la logique de suppression ici
                    // Exemple : categoryDAO.deleteCategory(categoryId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }



        


    }


