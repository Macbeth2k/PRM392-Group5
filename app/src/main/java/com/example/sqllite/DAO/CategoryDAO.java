package com.example.sqllite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sqllite.Models.Categories;

import java.util.List;
@Dao
public interface CategoryDAO {
    @Query("SELECT * FROM Categories")
    List<Categories> getAll();

    @Query("SELECT * FROM Categories WHERE categoryID LIKE :categoryId")
    Categories getbyIdCategory(int categoryId);

    @Insert
    void insertAllCategories(Categories... categories);

    @Delete
    void deleteCategory (Categories categories);

}
