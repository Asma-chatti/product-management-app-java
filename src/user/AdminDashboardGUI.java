package user;

import javax.swing.*;

import categorie.ViewCategoriesGUI;
import products.ManageProductsGUI;

import java.awt.*;
import java.awt.event.*;

public class AdminDashboardGUI extends JFrame implements ActionListener {
    private JButton manageProductsButton;
    private JButton addAdminButton;
    private JButton viewAdminsButton; // Nouveau bouton pour consulter la liste des administrateurs
    private JButton viewCategoriesButton; // Bouton pour consulter la liste des catégories
    private User loggedInUser;

    public AdminDashboardGUI(User loggedInUser) {
        this.loggedInUser = loggedInUser;

        setTitle("Admin Dashboard");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 5, 5)); // Modification de la disposition pour ajouter le nouveau bouton

        manageProductsButton = new JButton("Manage Products");
        manageProductsButton.addActionListener(this);
        mainPanel.add(manageProductsButton);

        addAdminButton = new JButton("Add Admin");
        addAdminButton.addActionListener(this);
        mainPanel.add(addAdminButton);

        viewAdminsButton = new JButton("View Admins"); // Ajout du bouton "View Admins"
        viewAdminsButton.addActionListener(this);
        mainPanel.add(viewAdminsButton); // Ajout du bouton à la fenêtre

        viewCategoriesButton = new JButton("View Categories"); // Bouton pour consulter la liste des catégories
        viewCategoriesButton.addActionListener(this);
        mainPanel.add(viewCategoriesButton); // Ajout du bouton à la fenêtre

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageProductsButton) {
            openManageProductsPage();
        } else if (e.getSource() == addAdminButton) {
            openAddAdminPage();
        } else if (e.getSource() == viewAdminsButton) { // Gestion de l'action du bouton "View Admins"
            openViewAdminsPage();
        } else if (e.getSource() == viewCategoriesButton) { // Gestion de l'action du bouton "View Categories"
            openViewCategoriesPage();
        }
    }

    private void openManageProductsPage() {
        // Insérer ici le code pour ouvrir la page de gestion des produits
        // Par exemple :
        new ManageProductsGUI(loggedInUser);
    }

    private void openAddAdminPage() {
        // Insérer ici le code pour ouvrir la page d'ajout d'administrateur
        // Par exemple :
        new AddAdminGUI();
    }

    private void openViewAdminsPage() {
        // Insérer ici le code pour ouvrir la page pour consulter la liste des administrateurs
        // Par exemple :
        new ViewAdminsGUI();
    }

    private void openViewCategoriesPage() {
        // Insérer ici le code pour ouvrir la page pour consulter la liste des catégories
        // Par exemple :
        new ViewCategoriesGUI();
    }
}
