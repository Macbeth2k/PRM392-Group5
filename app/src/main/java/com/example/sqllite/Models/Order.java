package com.example.sqllite.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "orders",
        foreignKeys = {
                @ForeignKey(entity = Customer.class, parentColumns = "customerID", childColumns = "customerID")
        })
public class Order {
    @PrimaryKey
    @ColumnInfo(name = "orderID")
    private int orderID;

    @ColumnInfo(name = "customerID")
    private int customerID;

    @ColumnInfo(name = "orderDate")
    private Date orderDate;

    @ColumnInfo(name = "shippingAddress")
    private String shippingAddress;

    // Constructors
    public Order(int orderID, int customerID, Date orderDate, String shippingAddress) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
