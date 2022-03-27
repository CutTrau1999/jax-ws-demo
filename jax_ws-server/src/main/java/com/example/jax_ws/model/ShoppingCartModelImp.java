package com.example.jax_ws.model;

import com.example.jax_ws.entity.CartItem;
import com.example.jax_ws.entity.Product;
import com.example.jax_ws.entity.ShoppingCart;
import com.example.jax_ws.utils.ConnectionHelper;

import java.sql.*;

public class ShoppingCartModelImp implements ShoppingCartModel{

    private Connection conn;

    public ShoppingCartModelImp() {
        conn = ConnectionHelper.getConnection();
    }

    @Override
    public ShoppingCart get(int userId) throws SQLException {
        return null;
    }

    public ShoppingCart save(ShoppingCart shoppingCart) throws SQLException {
        conn.setAutoCommit(false);// begin transaction
        try {

            if (shoppingCart == null || shoppingCart.getCartItems().size() == 0) {
                throw new Error("Shopping's null or empty.");
            }
            PreparedStatement stmtShoppingCart = conn.prepareStatement("insert into shopping_carts (userId, shipName, shipAddress, shipPhone, totalPrice) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmtShoppingCart.setInt(1, shoppingCart.getUserId());
            stmtShoppingCart.setString(2, shoppingCart.getShipName());
            stmtShoppingCart.setString(3, shoppingCart.getShipAddress());
            stmtShoppingCart.setString(4, shoppingCart.getShipPhone());
            stmtShoppingCart.setDouble(5, shoppingCart.getTotalPrice());
            int affectedRows = stmtShoppingCart.executeUpdate();
            if (affectedRows > 0) {
                ResultSet resultSetGeneratedKeys = stmtShoppingCart.getGeneratedKeys();
                if (resultSetGeneratedKeys.next()) {
                    int id = resultSetGeneratedKeys.getInt(1);
                    shoppingCart.setId(id);
                }
            }
            if (shoppingCart.getId() == 0) {
                throw new Error("Can't insert shopping cart.");
            }
            // insert cart items;
            for (CartItem item :
                    shoppingCart.getCartItems()) {
                PreparedStatement stmtCartItem = conn.prepareStatement("insert into cart_items (shoppingCartId, productId, productName, unitPrice, quantity) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmtCartItem.setInt(1, shoppingCart.getId());
                stmtCartItem.setInt(2, item.getProductId());
                stmtCartItem.setString(3, item.getProductName());
                stmtCartItem.setDouble(4, item.getUnitPrice());
                stmtCartItem.setInt(5, item.getQuantity());
                int affectedCartItemRows = stmtCartItem.executeUpdate();
                if (affectedCartItemRows == 0) { // lỗi
                    throw new Error("Insert cart item fails.");
                }
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            shoppingCart = null;
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return shoppingCart;
    }

    public ShoppingCart update(int id, ShoppingCart updateObject) throws SQLException {
        conn.setAutoCommit(false);
        try {

            if (updateObject == null || updateObject.getCartItems().size() == 0) {
                throw new Error("Shopping's null or empty.");
            }
            PreparedStatement stmtShoppingCart = conn.prepareStatement("update shopping_carts set shipName = ?, shipAddress = ?, shipPhone = ?, totalPrice = ? where id = ?", Statement.RETURN_GENERATED_KEYS);
            stmtShoppingCart.setString(1, updateObject.getShipName());
            stmtShoppingCart.setString(2, updateObject.getShipAddress());
            stmtShoppingCart.setString(3, updateObject.getShipPhone());
            stmtShoppingCart.setDouble(4, updateObject.getTotalPrice());
            stmtShoppingCart.setInt(5, id);
            int affectedRows = stmtShoppingCart.executeUpdate();
            if (affectedRows <= 0) {
                throw new Error("Can't update shopping cart.");
            }
            // delete old cart items.
            PreparedStatement stmtDeleteCartItem = conn.prepareStatement("delete from cart_items where shoppingCartId = ?", Statement.RETURN_GENERATED_KEYS);
            stmtDeleteCartItem.setInt(1, id);
            int affectedDeleteCartItemRows = stmtDeleteCartItem.executeUpdate();
            if (affectedDeleteCartItemRows == 0) { // lỗi
                throw new Error("Insert cart item fails.");
            }

            for (CartItem item :
                    updateObject.getCartItems()) {
                PreparedStatement stmtCartItem = conn.prepareStatement("insert into cart_items (shoppingCartId, productId, productName, unitPrice, quantity) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmtCartItem.setInt(1, id);
                stmtCartItem.setInt(2, item.getProductId());
                stmtCartItem.setString(3, item.getProductName());
                stmtCartItem.setDouble(4, item.getUnitPrice());
                stmtCartItem.setInt(5, item.getQuantity());
                int affectedCartItemRows = stmtCartItem.executeUpdate();
                if (affectedCartItemRows == 0) { // lỗi
                    throw new Error("Insert cart item fails.");
                }
            }
            conn.commit();
        } catch (Exception ex) {
            updateObject = null;
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return updateObject;
    }

    public boolean delete(int id) throws SQLException {
        conn.setAutoCommit(false);// begin transaction
        try {
            PreparedStatement stmtDeleteCartItem = conn.prepareStatement("delete from cart_items where shoppingCartId = ?");
            stmtDeleteCartItem.setInt(1, id);
            int affectedCartItemRows = stmtDeleteCartItem.executeUpdate();
            if (affectedCartItemRows <= 0) {
                return false;
            }
            PreparedStatement stmtDelete = conn.prepareStatement("delete from shopping_carts where id = ?");
            stmtDelete.setInt(1, id);
            int affectedRows = stmtDelete.executeUpdate();
            if (affectedRows <= 0) {
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return false;
    }
}
