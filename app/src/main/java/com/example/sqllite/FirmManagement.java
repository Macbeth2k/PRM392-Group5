package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                            firmDAO.insertAllFirms(new Firm(Integer.parseInt(edt_firmID.getText().toString()),edt_firmName.getText().toString()));
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
                        Fragment fragment = new AdminHomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.adminhome_frame, fragment);
                        fragmentTransaction.commit();
                    }
                });
                t.start();
            }
        });
    }
}