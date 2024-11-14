package user;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AddAdminGUI extends JFrame implements ActionListener {
    private JTextField usernameField, emailField, fullNameField, addressField, phoneField;
    private JPasswordField passwordField;
    private JButton createAdminButton, loginButton;

    public AddAdminGUI() {
        setTitle("Admin Creation");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 2, 5, 5));

        mainPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        mainPanel.add(emailField);

        mainPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);

        mainPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        mainPanel.add(fullNameField);

        mainPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        mainPanel.add(addressField);

        mainPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        mainPanel.add(phoneField);

        createAdminButton = new JButton("Create Admin");
        createAdminButton.addActionListener(this);
        mainPanel.add(createAdminButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        mainPanel.add(loginButton);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createAdminButton) {
            createAdmin();
        } else if (e.getSource() == loginButton) {
            login();
        }
    }

    private void createAdmin() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        // Créer un objet User avec les données saisies et le rôle "admin"
        User admin = new User(username, email, password, fullName, address, phone, "admin");

        // Utiliser UserDAO pour créer l'administrateur dans la base de données
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.createUser(admin);
            System.out.println("Admin created successfully.");
        } catch (SQLException ex) {
            System.out.println("Error creating admin: " + ex.getMessage());
        }
    }

    private void login() {
        dispose();
        new LoginGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddAdminGUI::new);
    }
}
