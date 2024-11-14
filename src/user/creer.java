package user;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class creer extends JFrame implements ActionListener {
    private JTextField usernameField, emailField, fullNameField, addressField, phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton createUserButton, loginButton;

    public creer() {
        setTitle("User and Admin Creation");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2, 5, 5));

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

        mainPanel.add(new JLabel("Role:"));
        String[] roles = {"user", "admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setEnabled(false);
        mainPanel.add(roleComboBox);

        createUserButton = new JButton("Create User");
        createUserButton.addActionListener(this);
        mainPanel.add(createUserButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        mainPanel.add(loginButton);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createUserButton) {
            createUser();
        } else if (e.getSource() == loginButton) {
        	loginuser();
        }
    }

    private void createUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String role = (String) roleComboBox.getSelectedItem();

        // Créer un objet User avec les données saisies
        User user = new User(username, email, password, fullName, address, phone, role);

        // Utiliser UserDAO pour créer l'utilisateur dans la base de données
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.createUser(user);
            System.out.println("User created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }


    private void loginuser() {
        dispose();
    	new LoginGUI();
    }


}
