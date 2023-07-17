package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqllite.Models.OrderDetail;
import com.google.gson.Gson;

public class OrderDetailManagement extends AppCompatActivity {

    private OrderDetail orderDetail;
    private TextView txt_orderID, txt_CustomerID, txt_oderPrice, txt_Address, txt_TotalMoney;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_management);
        txt_orderID = findViewById(R.id.txt_OrderIDDetail);
        txt_CustomerID = findViewById(R.id.txt_ProductIDDetail);
        txt_oderPrice = findViewById(R.id.txt_PriceOrderDetail);
        txt_Address = findViewById(R.id.txt_QuantityOrderDetail);
        txt_TotalMoney = findViewById(R.id.txt_TotalMoney);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name-v2").build();
        String orDetailJson = getIntent().getStringExtra("orderDetailJson");

        // Chuyển đổi chuỗi JSON thành đối tượng Products
        Gson gson = new Gson();
        orderDetail = gson.fromJson(orDetailJson, OrderDetail.class);

        if (orderDetail != null) {
            txt_orderID.setText(orderDetail.getOrderID());
            txt_CustomerID.setText(String.valueOf(orderDetail.getProductID()));
            txt_oderPrice.setText(String.valueOf(orderDetail));
            txt_Address.setText(String.valueOf(orderDetail.getUnitPrice()));
            txt_TotalMoney.setText(String.valueOf(orderDetail.getQuantity()*orderDetail.getUnitPrice()));
        }
    }
}