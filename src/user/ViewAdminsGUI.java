package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ViewAdminsGUI extends JFrame {
    private JTable adminsTable;

    public ViewAdminsGUI() {
        setTitle("List of Admins");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Récupérer la liste des administrateurs depuis la base de données
        List<User> admins = null;
        try {
            UserDAO userDAO = new UserDAO();
            admins = userDAO.getUsersByRole("admin");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching admins list", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); // Fermer la fenêtre en cas d'erreur
            return;
        }

        // Créer un tableau de données pour stocker les administrateurs
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Username");
        tableModel.addColumn("Email");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Phone");

        for (User admin : admins) {
            tableModel.addRow(new Object[]{
                    admin.getUsername(),
                    admin.getEmail(),
                    admin.getFullName(),
                    admin.getAddress(),
                    admin.getPhone()
            });
        }

        // Créer le tableau graphique avec les données
        adminsTable = new JTable(tableModel);

        // Ajouter le tableau dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(adminsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

}
