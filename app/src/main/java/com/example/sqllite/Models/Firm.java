package com.example.sqllite.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "firms")
public class Firm {
    @PrimaryKey
    @ColumnInfo(name = "firmID")
    private int firmID;

    @ColumnInfo(name = "firmName")
    private String firmName;

    // Constructors
    public Firm(int firmID, String firmName) {
        this.firmID = firmID;
        this.firmName = firmName;
    }

    @Override
    public String toString() {
        return "Firm{" +
                "firmID=" + firmID +
                ", firmName='" + firmName + '\'' +
                '}';
    }

    // Getters and Setters

    public int getFirmID() {
        return firmID;
    }

    public void setFirmID(int firmID) {
        this.firmID = firmID;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
}


