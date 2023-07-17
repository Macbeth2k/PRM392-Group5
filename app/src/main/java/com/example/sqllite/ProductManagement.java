package com.example.sqllite;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqllite.AppDatabase;
import com.example.sqllite.DAO.CategoryDAO;
import com.example.sqllite.DAO.FirmDAO;
import com.example.sqllite.DAO.ProductDAO;
import com.example.sqllite.Models.Categories;
import com.example.sqllite.Models.Firm;
import com.example.sqllite.Models.Products;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductManagement extends AppCompatActivity {

    EditText edt_ProductId, edt_Productname, edt_QuantityPerUnit, edt_UnitPrice, edt_ProductImage;

    List<Categories> categoryList;
    List<Firm> firmList;
    ArrayAdapter<Categories> categoryAdapter;
    ArrayAdapter<Firm> firmAdapter;
    Spinner spinner_CategoryId, spinner_SupplierId;

    private Uri selectedImageUri;
    private String productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        edt_ProductId = findViewById(R.id.Proid_txt);
        edt_Productname = findViewById(R.id.Proname_txt);
        edt_QuantityPerUnit = findViewById(R.id.Proquantity_txt2);
        edt_UnitPrice = findViewById(R.id.Proprice_txt3);
        spinner_CategoryId = findViewById(R.id.spinner_category);
        spinner_SupplierId = findViewById(R.id.spinner_supplier);
        edt_ProductImage = findViewById(R.id.edt_ProductImage);
        categoryList = new ArrayList<>(); // Danh sách Category
        firmList = new ArrayList<>(); // Danh sách Firm
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        firmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, firmList);

        // Gán adapter cho Spinner CategoryId và SupplierId
        spinner_CategoryId.setAdapter(categoryAdapter);
        spinner_SupplierId.setAdapter(firmAdapter);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name-v2").build();

        CategoryDAO categoryDAO = db.categoryDAO();
        FirmDAO firmDAO = db.firmDAO();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Lấy danh sách Category từ cơ sở dữ liệu
                List<Categories> categories = categoryDAO.getAll();
                categoryList.addAll(categories);

                // Lấy danh sách Firm từ cơ sở dữ liệu
                List<Firm> firms = firmDAO.getAll();
                firmList.addAll(firms);

                // Cập nhật giao diện sau khi có dữ liệu
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        categoryAdapter.notifyDataSetChanged();
                        firmAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        t.start();

        ((Button) findViewById(R.id.btn_addImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở hộp thoại chọn tệp
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        ((Button) findViewById(R.id.btn_addCate)).setText("ADD");
        ((Button) findViewById(R.id.btn_addCate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường dữ liệu
                int productId = Integer.parseInt(edt_ProductId.getText().toString());
                String productName = edt_Productname.getText().toString();
                int categoryId = ((Categories) spinner_CategoryId.getSelectedItem()).getCategoryID();
                int supplierId = ((Firm) spinner_SupplierId.getSelectedItem()).getFirmID();
                int quantityPerUnit = Integer.parseInt(edt_QuantityPerUnit.getText().toString());
                double unitPrice = Double.parseDouble(edt_UnitPrice.getText().toString());

                // Kiểm tra và xử lý nếu tệp hình ảnh đã được chọn
                if (selectedImageUri != null) {
                    productImage = getRealPathFromUri(selectedImageUri);

                    // Tạo một đối tượng AsyncTask để thêm sản phẩm vào cơ sở dữ liệu
                    InsertProductAsyncTask insertProductAsyncTask = new InsertProductAsyncTask(productId, productName,
                            categoryId, supplierId, quantityPerUnit, unitPrice, productImage);
                    insertProductAsyncTask.execute();
                } else {
                    // Hiển thị thông báo lỗi nếu không có tệp hình ảnh được chọn
                    displayToast("Vui lòng chọn tệp hình ảnh");
                }
            }
        });

        ((Button) findViewById(R.id.btn_list)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDAO customerDao = db.productDAO();
                        if (edt_ProductId.getText().toString().isEmpty() && edt_Productname.getText().toString().isEmpty()) {
                            List<Products> products = customerDao.getAll();
                            ((TextView) findViewById(R.id.tv_show)).setText(products.toString());
                        } else if (edt_ProductId.getText().toString().isEmpty() && !edt_Productname.getText().toString().isEmpty()) {
                            Products products = customerDao.findByOnlyName(edt_Productname.getText().toString());
                            ((TextView) findViewById(R.id.tv_show)).setText(products.toString());
                        } else if (!edt_ProductId.getText().toString().isEmpty() && edt_Productname.getText().toString().isEmpty()) {
                            Products products = customerDao.findByOnlyID(Integer.parseInt(edt_ProductId.getText().toString()));
                            ((TextView) findViewById(R.id.tv_show)).setText(products.toString());
                        } else {
                            Products customers = customerDao.findByName(Integer.parseInt(edt_ProductId.getText().toString()), edt_Productname.getText().toString());
                            ((TextView) findViewById(R.id.tv_show)).setText(customers.toString());
                        }
                    }
                });
                t.start();
            }
        });

        ((Button) findViewById(R.id.btn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDAO productDao = db.productDAO();
                        int productIdToDelete = Integer.parseInt(edt_ProductId.getText().toString());
                        Products pro = productDao.getByIds(productIdToDelete);
                        productDao.deleteProduct(pro);
                    }
                });
                t.start();
            }
        });

        ((Button) findViewById(R.id.btn_update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDAO productDao = db.productDAO();
                        int productIdUpdate = Integer.parseInt(edt_ProductId.getText().toString());
                        Products pro = productDao.getByIds(productIdUpdate);

                        if (pro != null) {
                            // Cập nhật thông tin sản phẩm
                            pro.setProductName(edt_Productname.getText().toString());
                            pro.setSupplierID(((Firm) spinner_SupplierId.getSelectedItem()).getFirmID());
                            pro.setCategoryID(((Categories) spinner_CategoryId.getSelectedItem()).getCategoryID());
                            pro.setQuantityPerUnit(Integer.parseInt(edt_QuantityPerUnit.getText().toString()));
                            pro.setUnitPrice(Double.parseDouble(edt_UnitPrice.getText().toString()));
                            pro.setProductImage(edt_ProductImage.getText().toString());

                            // Thực hiện cập nhật vào cơ sở dữ liệu
                            productDao.updateProduct(pro);
                        } else {
                            displayToast("Không tìm thấy sản phẩm có ID " + productIdUpdate);
                        }
                    }
                });
                t.start();
            }
        });

        ((Button) findViewById(R.id.btn_DetailProduct)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductDAO productDao = db.productDAO();
                        Products proDetail = productDao.getByIds(Integer.parseInt(edt_ProductId.getText().toString()));

                        Gson gson = new Gson();
                        String proDetailJson = gson.toJson(proDetail);

                        Intent intent = new Intent(ProductManagement.this, ProductDetailManagement.class);
                        intent.putExtra("productDetailJson", proDetailJson);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });

        ((Button) findViewById(R.id.btn_HomeDetailPro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ProductManagement.this, AdminPage.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                // Hiển thị tên tệp hình ảnh đã chọn
                String imageName = getFileNameFromUri(selectedImageUri);
                edt_ProductImage.setText(imageName);
                productImage = getRealPathFromUri(selectedImageUri);
            }
        }
    }

    private class InsertProductAsyncTask extends AsyncTask<Void, Void, Void> {
        private int productId;
        private String productName;
        private int categoryId;
        private int supplierId;
        private int quantityPerUnit;
        private double unitPrice;
        private String productImage;

        public InsertProductAsyncTask(int productId, String productName, int categoryId, int supplierId,
                                      int quantityPerUnit, double unitPrice, String productImage) {
            this.productId = productId;
            this.productName = productName;
            this.categoryId = categoryId;
            this.supplierId = supplierId;
            this.quantityPerUnit = quantityPerUnit;
            this.unitPrice = unitPrice;
            this.productImage = productImage;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name-v2").build();
            ProductDAO productDao = db.productDAO();
            Products existingProduct = productDao.getByIds(productId);
            if (existingProduct != null) {
                displayToast("Product ID đã tồn tại");
                return null;
            }

            productDao.insertAllProducts(new Products(productId, productName,
                    categoryId, supplierId,
                    quantityPerUnit, unitPrice,
                    productImage));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayToast("Thêm sản phẩm thành công");
        }
    }

    private void displayToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromUri(Uri uri) {
        String filePath;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            filePath = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}