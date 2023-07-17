package com.example.sqllite.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(tableName = "orderDetails",
        foreignKeys = {
                @ForeignKey(entity = Order.class, parentColumns = "orderID", childColumns = "orderID"),
                @ForeignKey(entity = Products.class, parentColumns = "productID", childColumns = "productID")
        })
public class OrderDetail {
    @PrimaryKey
    @ColumnInfo(name = "orderID")
    private int orderID;

    @ColumnInfo(name = "productID")
    private int productID;

    @ColumnInfo(name = "unitPrice")
    private double unitPrice;

    @ColumnInfo(name = "quantity")
    private int quantity;

    // Constructors
    public OrderDetail(int orderID, int productID, double unitPrice, int quantity) {
        this.orderID = orderID;
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderID=" + orderID +
                ", productID=" + productID +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }

    // Getters and Setters

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
