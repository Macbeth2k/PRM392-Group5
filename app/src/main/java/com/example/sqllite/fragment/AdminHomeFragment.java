package com.example.sqllite.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sqllite.AppDatabase;
import com.example.sqllite.CategoryManagement;
import com.example.sqllite.FirmManagement;
import com.example.sqllite.OrderManagement;
import com.example.sqllite.ProductManagement;
import com.example.sqllite.R;

public class AdminHomeFragment extends Fragment {
    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        return view;
    }
}