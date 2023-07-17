package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sqllite.Models.Order;

public class AdminPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        ((Button)findViewById(R.id.btn_productManagement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminPage.this, ProductManagement.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_CategoryManagement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminPage.this, CategoryManagement.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_FirmManagement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminPage.this, FirmManagement.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_OrderManagement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminPage.this, OrderManagement.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }

}