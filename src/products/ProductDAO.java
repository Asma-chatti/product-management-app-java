package products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import user.DatabaseConnection;

public class ProductDAO {
    private Connection connection;

    // Constructeur prenant une connexion à la base de données
    public ProductDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }

    // Méthode pour ajouter un produit
    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (name, description, price, quantity, category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, product.getCategory());
            statement.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les produits
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setCategory(resultSet.getString("category"));
                products.add(product);
            }
        }
        return products;
    }

    // Méthode pour mettre à jour un produit
    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, description = ?, price = ?, quantity = ?, category = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, product.getCategory());
            statement.setInt(6, product.getId());
            statement.executeUpdate();
        }
    }

    // Méthode pour supprimer un produit
    public void deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }
    
    // Méthode pour mettre à jour la quantité du produit dans la base de données
    void updateProductQuantity(int productId, int newQuantity) throws SQLException {
        String query = "UPDATE products SET quantity = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newQuantity);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
    }
}
