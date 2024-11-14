package categorie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class EditCategoryGUI extends JFrame implements ActionListener {
    private JTextField nameField;
    private JButton updateButton;
    private Categories category;

    public EditCategoryGUI(Categories category) {
        this.category = category;

        setTitle("Edit Category");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField(category.getName());
        inputPanel.add(nameField);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        mainPanel.add(updateButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            String newName = nameField.getText();
            category.setName(newName);

            CategoryDAO categoryDAO = new CategoryDAO();
            try {
                categoryDAO.updateCategory(category);
                JOptionPane.showMessageDialog(this, "Category updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                new ViewCategoriesGUI();
                dispose();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating category", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
