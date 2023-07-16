package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqllite.DAO.CategoryDAO;
import com.example.sqllite.Models.Categories;

public class FirmManagement extends AppCompatActivity {
    EditText edt_firmID, edt_firmName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_management);
        edt_firmID = findViewById(R.id.edt_FirmID);
        edt_firmName = findViewById(R.id.edt_FirmName);
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name-v2").build();

            ((Button)findViewById(R.id.btn_addfirm)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CategoryDAO categoryDAO = db.categoryDAO();
                            categoryDAO.insertAllCategories(new Categories(Integer.parseInt(edt_firmID.getText().toString()),edt_firmName.getText().toString()));
                        }
                    });
                    t.start();
                }
            });
    }
}