package com.example.sqllite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sqllite.DAO.CategoryDAO;
import com.example.sqllite.DAO.FirmDAO;
import com.example.sqllite.DAO.OrderDAO;
import com.example.sqllite.DAO.ProductDAO;
import com.example.sqllite.Models.Categories;
import com.example.sqllite.Models.Customer;
import com.example.sqllite.Models.DataConverter;
import com.example.sqllite.Models.Firm;
import com.example.sqllite.Models.Order;
import com.example.sqllite.Models.OrderDetail;
import com.example.sqllite.Models.Products;

import androidx.room.TypeConverters;

@Database(entities = {Firm.class, Products.class, Categories.class, Customer.class, Order.class, OrderDetail.class}, version = 2, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDAO productDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract FirmDAO firmDAO();
    public abstract OrderDAO orderDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name-v2")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
