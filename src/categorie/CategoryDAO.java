package categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import user.DatabaseConnection;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(Categories category) throws SQLException {
        String query = "INSERT INTO categories (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
        }
    }

    public List<Categories> getAllCategories() throws SQLException {
        List<Categories> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
            	Categories category = new Categories();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // ou logger l'erreur
            throw e; // Rejeter l'exception pour la gérer à un niveau supérieur
        }
        return categories;
    }

    public void updateCategory(Categories category) throws SQLException {
        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        }
    }

    // Méthode pour obtenir une catégorie par son identifiant
    public Categories getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	Categories category = new Categories();
                    category.setId(resultSet.getInt("id"));
                    category.setName(resultSet.getString("name"));
                    return category;
                }
            }
        }
        return null;
    }
}

