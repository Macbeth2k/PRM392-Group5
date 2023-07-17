package com.example.sqllite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sqllite.Models.Cart;

import java.util.List;

@Dao
public interface CartDAO {
    @Delete
    void deleteCart (Cart cart);

    @Insert
    void insertCart (Cart cart);

    @Query("SELECT * FROM cart")
    List<Cart> getListCart();

    @Query("SELECT * FROM cart WHERE productId= :productId ")
    Cart checkIdCart(int productId);

    @Query("SELECT * FROM cart WHERE cartId= :cartId ")
    Cart getOneCart(int cartId);
}
