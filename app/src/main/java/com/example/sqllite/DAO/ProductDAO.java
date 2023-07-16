package com.example.sqllite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sqllite.Models.Products;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM Products")
    List<Products> getAll();

    @Query("SELECT * FROM Products WHERE ProductID LIKE :productId")
    Products getByIds(int productId);

    @Query("SELECT * FROM Products WHERE ProductID LIKE :productId AND " +
            "ProductName LIKE '%' || :productName || '%' ")
    Products findByName(int productId, String productName);

    @Query("SELECT * FROM Products WHERE ProductName LIKE '%' || :productName || '%' ")
    Products findByOnlyName(String productName);

    @Query("SELECT * FROM Products WHERE ProductID LIKE :productId ")
    Products findByOnlyID(int productId);

    @Insert
    void insertAllProducts(Products... products);

    @Delete
    void deleteProduct (Products product);
}
