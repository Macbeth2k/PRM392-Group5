package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.sqllite.DAO.FirmDAO;
import com.example.sqllite.Models.Firm;
import com.example.sqllite.fragment.AdminHomeFragment;

import java.util.List;

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
                        FirmDAO firmDAO = db.firmDAO();

                        String firmIDText = edt_firmID.getText().toString().trim();
                        String firmName = edt_firmName.getText().toString().trim();

                        // Kiểm tra tất cả các điều kiện nhập
                        if (firmIDText.isEmpty()) {
                            displayToast("Vui lòng nhập firmID");
                            return;
                        }

                        if (firmName.isEmpty()) {
                            displayToast("Vui lòng nhập firmName");
                            return;
                        }

                        // Kiểm tra Firm ID đã tồn tại hay chưa
                        int firmID = Integer.parseInt(firmIDText);
                        Firm existingFirm = firmDAO.getByIds(firmID);
                        if (existingFirm != null) {
                            displayToast("Firm ID đã tồn tại");
                            return;
                        }

                        // Nếu tất cả điều kiện nhập đều hợp lệ, thực hiện thêm vào cơ sở dữ liệu
                        firmDAO.insertAllFirms(new Firm(firmID, firmName));
                    }
                });
                t.start();
            }
        });
        ((Button)findViewById(R.id.btn_ListFirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FirmDAO firmDAO = db.firmDAO();
                        List<Firm> firms = firmDAO.getAll();
                        ((TextView)findViewById(R.id.tvshow_firm)).setText(firms.toString());
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_deletetFirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FirmDAO firmDAO = db.firmDAO();
                        int firmIdToDelete = Integer.parseInt(edt_firmID.getText().toString());
                        Firm firm = firmDAO.getByIds(firmIdToDelete);
                        firmDAO.deleteFirm(firm);
                    }
                });
                t.start();
            }
        });

        ((Button)findViewById(R.id.btn_homefirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FirmManagement.this, AdminPage.class);
                        startActivity(intent);
                    }
                });
                t.start();
            }
        });
    }
    private void displayToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}