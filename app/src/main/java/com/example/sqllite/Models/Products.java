package com.example.sqllite.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "products",
        foreignKeys = {
                @ForeignKey(entity = Firm.class, parentColumns = "firmID", childColumns = "supplierID"),
                @ForeignKey(entity = Categories.class, parentColumns = "categoryID", childColumns = "categoryID")
        })
public class Products {
    @PrimaryKey
    @ColumnInfo(name = "productID")
    private int productID;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "supplierID")
    private int supplierID;

    @ColumnInfo(name = "categoryID")
    private int categoryID;

    @ColumnInfo(name = "quantityPerUnit")
    private int quantityPerUnit;

    @ColumnInfo(name = "unitPrice")
    private double unitPrice;

    @ColumnInfo(name = "productImage")
    private String productImage;

    // Constructors
    public Products(int productID, String productName, int supplierID, int categoryID, int quantityPerUnit, double unitPrice, String productImage) {
        this.productID = productID;
        this.productName = productName;
        this.supplierID = supplierID;
        this.categoryID = categoryID;
        this.quantityPerUnit = quantityPerUnit;
        this.unitPrice = unitPrice;
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", supplierID=" + supplierID +
                ", categoryID=" + categoryID +
                ", quantityPerUnit=" + quantityPerUnit +
                ", unitPrice=" + unitPrice +
                ", productImage='" + productImage + '\'' +
                '}';
    }

    // Getters and Setters

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(int quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}