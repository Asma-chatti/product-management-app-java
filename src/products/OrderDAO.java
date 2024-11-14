package products;

import java.sql.*;
import java.util.List;

import user.DatabaseConnection;

public class OrderDAO {
    private Connection connection;

    // Constructeur prenant une connexion à la base de données
    public OrderDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }

    public void placeOrder(int userId, List<OrderItem> orderItems) throws SQLException {
        // 1. Insérer une nouvelle ligne dans la table orders
        String insertOrderQuery = "INSERT INTO orders (user_id, order_date, total_amount, status) VALUES (?, CURRENT_TIMESTAMP(), ?, 'Pending')";
        PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
        orderStatement.setInt(1, userId);

        // Calculer le montant total de la commande
        double totalAmount = 0.0;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getQuantity() * orderItem.getUnitPrice();
        }
        orderStatement.setDouble(2, totalAmount);

        orderStatement.executeUpdate();

        // Récupérer l'identifiant de la commande nouvellement insérée
        ResultSet generatedKeys = orderStatement.getGeneratedKeys();
        int orderId = -1;
        if (generatedKeys.next()) {
            orderId = generatedKeys.getInt(1);
        }

        // 2. Pour chaque produit acheté, insérer une nouvelle ligne dans la table order_items
        String insertOrderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemQuery);
        for (OrderItem orderItem : orderItems) {
            orderItemStatement.setInt(1, orderId);
            orderItemStatement.setInt(2, orderItem.getProductId());
            orderItemStatement.setInt(3, orderItem.getQuantity());
            orderItemStatement.setDouble(4, orderItem.getUnitPrice());
            orderItemStatement.addBatch(); // Ajouter l'instruction à un lot pour un traitement plus efficace
        }
        orderItemStatement.executeBatch();

    }
    
    public int createOrder(int userId, int quantity, int productId,double price) throws SQLException {
        int orderId = -1;
        try {
            // 1. Créer une nouvelle commande dans la table orders
            String insertOrderQuery = "INSERT INTO orders (user_id, order_date, total_amount, status) VALUES (?, CURRENT_TIMESTAMP(), ?, 'Pending')";
            PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            orderStatement.setInt(1, userId);
            orderStatement.setDouble(2, price);
            orderStatement.executeUpdate();

            // Récupérer l'identifiant de la commande nouvellement insérée
            ResultSet generatedKeys = orderStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                // Si la récupération de l'identifiant échoue, annuler la transaction
                connection.rollback();
                return -1;
            }

         // 2. Insérer le produit dans la table order_items
            String insertOrderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemQuery);
            orderItemStatement.setInt(1, orderId);
            orderItemStatement.setInt(2, productId);
            orderItemStatement.setInt(3, 1);
            orderItemStatement.setDouble(4, price);
            orderItemStatement.executeUpdate();


            // Fin de la transaction (valider)
        } catch (SQLException e) {
            throw e;
        } 
        return orderId;
    }
}

