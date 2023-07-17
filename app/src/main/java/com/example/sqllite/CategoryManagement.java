package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqllite.DAO.CategoryDAO;
import com.example.sqllite.Models.Categories;

import java.util.List;

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

        ((Button)findViewById(R.id.btn_addCate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryDAO categoryDAO = db.categoryDAO();

                        String categoryIDText = edt_cateID.getText().toString().trim();
                        String categoryName = edt_cateName.getText().toString().trim();

                        // Kiểm tra tất cả các điều kiện nhập
                        if (categoryIDText.isEmpty()) {
                            displayToast("Vui lòng nhập categoryID");
                            return;
                        }

                        if (categoryName.isEmpty()) {
                            displayToast("Vui lòng nhập categoryName");
                            return;
                        }

                        int categoryID = Integer.parseInt(categoryIDText);
                        Categories existingCategory = categoryDAO.getbyIdCategory(categoryID);
                        if (existingCategory != null) {
                            displayToast("Category ID đã tồn tại");
                            return;
                        }

                        // Nếu tất cả điều kiện nhập đều hợp lệ, thực hiện thêm vào cơ sở dữ liệu
                        categoryDAO.insertAllCategories(new Categories(categoryID, categoryName));
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_listCate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryDAO categoryDao = db.categoryDAO();
                        List<Categories> categories = categoryDao.getAll();
                        ((TextView)findViewById(R.id.tvshow_category)).setText(categories.toString());
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_deleteCate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CategoryDAO categoryDao = db.categoryDAO();
                        int categoryIdToDelete = Integer.parseInt(edt_cateID.getText().toString());
                        Categories cate = categoryDao.getbyIdCategory(categoryIdToDelete);
                        categoryDao.deleteCategory(cate);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_homeCate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CategoryManagement.this, AdminPage.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }
    private void displayToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}