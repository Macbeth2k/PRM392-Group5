package com.example.sqllite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sqllite.Models.Firm;

import java.util.List;
@Dao
public interface FirmDAO {
    @Query("SELECT * FROM firms")
    List<Firm> getAll();

    @Query("SELECT * FROM firms WHERE firmID LIKE :firmId")
    Firm getByIds(int firmId);

    @Insert
    void insertAllFirms(Firm... firms);

    @Delete
    void deleteFirm (Firm firm);
}
