package user;

import javax.swing.*;

import products.ProductListGUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginGUI extends JFrame implements ActionListener {
	
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginGUI() {
        setTitle("User Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 5, 5));

        mainPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        mainPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        mainPanel.add(registerButton);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            loginUser();
        } else if (e.getSource() == registerButton) {
            openRegistrationForm();
        }
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Utiliser UserDAO pour vérifier les informations de connexion
        UserDAO userDAO = new UserDAO();
        if (userDAO.login(username, password)) {
            User loggedInUser = null;
            try {
                loggedInUser = userDAO.getUserByUsername(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Vérifier le rôle de l'utilisateur
            String role = loggedInUser.getRole();
            if ("admin".equals(role)) { // Utilisez equals() pour comparer les chaînes de caractères
                // Rediriger l'utilisateur vers le tableau de bord de l'administrateur
                // Insérez ici le code pour rediriger l'utilisateur vers l'autre endroit, par exemple :
                new AdminDashboardGUI(loggedInUser);
            } else {
                // Rediriger l'utilisateur vers la liste des produits
                new ProductListGUI(loggedInUser);
            }

            // Fermer la fenêtre de connexion actuelle
            dispose();
        } else {
            System.out.println("Login failed. Please try again.");
            // Afficher un message d'erreur à l'utilisateur, par exemple :
            // JOptionPane.showMessageDialog(this, "Login failed. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void openRegistrationForm() {
        // Insert code to open registration form
        dispose();
        new creer();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }

}
