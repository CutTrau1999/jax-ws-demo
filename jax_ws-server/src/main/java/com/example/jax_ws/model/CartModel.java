package com.example.jax_ws.model;

import com.example.jax_ws.entity.Cart;
import com.example.jax_ws.utils.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartModel {

    private Connection conn;

    public CartModel() {
        conn = ConnectionHelper.getConnection();
    }

    public List<Cart> findAll() throws SQLException {
        List<Cart> carts = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("select * from carts");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int product_Id = rs.getInt("product_Id");
            double price = rs.getInt("totalPrice");
            int quantity = rs.getInt("quantity");
            carts.add(new Cart(id, product_Id, price, quantity));
        }
        return carts;
    }


    public Cart save(Cart cart) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("insert into carts (product_Id, totalPrice , quantity) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, cart.getProduct_Id());
        stmt.setDouble(2, cart.getTotalPrice());
        stmt.setInt(3, cart.getQuantity());
        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) {
            ResultSet resultSetGeneratedKeys = stmt.getGeneratedKeys();
            if (resultSetGeneratedKeys.next()) {
                int id = resultSetGeneratedKeys.getInt(1);
                cart.setId(id);
                return cart;
            }
        }
        return null;
    }
}