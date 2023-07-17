package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.sqllite.DAO.OrderDAO;
import com.example.sqllite.Models.Order;
import com.google.gson.Gson;

import java.util.List;

public class OrderManagement extends AppCompatActivity {

    EditText edt_orderId, edt_customerId, edt_orderDate, edt_Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        edt_orderId = findViewById(R.id.txt_orderID);
        edt_customerId = findViewById(R.id.txt_CustomerID);
        edt_orderDate = findViewById(R.id.date_OrderDate);
        edt_Address = findViewById(R.id.txt_orderAddress);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name-v2").build();

        ((Button)findViewById(R.id.btn_orderList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OrderDAO orderDAO = db.orderDAO();
                        List<Order> orders = orderDAO.getAllOrder();
                        ((TextView)findViewById(R.id.tvshow_order)).setText(orders.toString());
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_deleteOrder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OrderDAO orderDAO = db.orderDAO();
                        int orderIdToDelete = Integer.parseInt(edt_orderId.getText().toString());
                        Order order = orderDAO.getOrderByIds(orderIdToDelete);
                        orderDAO.deleteOrder(order);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_orderHome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(OrderManagement.this, AdminPage.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }
}