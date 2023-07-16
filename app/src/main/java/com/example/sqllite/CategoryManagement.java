package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqllite.DAO.CategoryDAO;
import com.example.sqllite.Models.Categories;

public class CategoryManagement extends AppCompatActivity {

    EditText edt_cateID, edt_cateName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);
        edt_cateID = findViewById(R.id.edt_cateID);
        edt_cateName = findViewById(R.id.edt_cateName);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name-v2").build();

        ((Button)findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryDAO categoryDAO = db.categoryDAO();
                        categoryDAO.insertAllCategories(new Categories(Integer.parseInt(edt_cateID.getText().toString()),edt_cateName.getText().toString()));
                    }
                });
                t.start();
            }
        });

    }
}