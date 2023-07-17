package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqllite.DAO.ProductDAO;
import com.example.sqllite.Models.Products;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ProductDetailManagement extends AppCompatActivity {
    private Products proDetail;
    private TextView txt_ID, txt_Name, txt_SupID, txt_CateID, txt_Quantity, txt_Price;
    private ImageView imageView;
    private AppDatabase db;
    private ProductDAO productDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_management);
        txt_ID = findViewById(R.id.txt_ID);
        txt_Name = findViewById(R.id.txt_name);
        txt_SupID = findViewById(R.id.txt_supid);
        txt_CateID = findViewById(R.id.txt_cateid);
        txt_Quantity = findViewById(R.id.txt_quantity);
        txt_Price = findViewById(R.id.txt_price);
        imageView = findViewById(R.id.img_pro);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name-v2").build();
        productDao = db.productDAO();

        String proDetailJson = getIntent().getStringExtra("productDetailJson");

        // Chuyển đổi chuỗi JSON thành đối tượng Products
        Gson gson = new Gson();
        proDetail = gson.fromJson(proDetailJson, Products.class);

        // Tiếp tục xử lý với đối tượng proDetail
        if (proDetail != null) {
            txt_Name.setText(proDetail.getProductName());
            txt_SupID.setText(String.valueOf(proDetail.getSupplierID()));
            txt_CateID.setText(String.valueOf(proDetail.getCategoryID()));
            txt_Quantity.setText(String.valueOf(proDetail.getQuantityPerUnit()));
            txt_Price.setText(String.valueOf(proDetail.getUnitPrice()));
            Picasso.get().load(proDetail.getProductImage()).resize(600, 400).into(imageView);
        }

        ((Button)findViewById(R.id.btn_homeProductDetail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProductDetailManagement.this, ProductManagement.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }

}
