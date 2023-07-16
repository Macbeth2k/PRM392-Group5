package com.example.sqllite.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sqllite.Models.Order;
import com.example.sqllite.Models.OrderDetail;

import java.util.List;
@Dao
public interface OrderDAO {
    @Query("SELECT * FROM orders")
    List<Order> getAllOrder();

    @Query("SELECT * FROM orders WHERE orderID LIKE :orderID")
    Order getOrderByIds(int orderID);

    @Query("SELECT * FROM ORDERDETAILS WHERE orderID LIKE :orderID")
    OrderDetail getOrderDetailByIds(int orderID);

    @Insert
    void insertAllOrders(Order... orders);

    @Delete
    void deleteOrder (Order order);
}

