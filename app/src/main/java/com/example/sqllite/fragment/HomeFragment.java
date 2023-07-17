package com.example.sqllite.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllite.AppDatabase;
import com.example.sqllite.DAO.CartDAO;
import com.example.sqllite.DAO.ProductDAO;
import com.example.sqllite.Models.Cart;
import com.example.sqllite.Models.Products;
import com.example.sqllite.R;
import com.example.sqllite.UserActivity;
import com.example.sqllite.adapter.ProductAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Products> productsList;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rcv_cate);
        productAdapter = new ProductAdapter(new ProductAdapter.IClickProductAdapter() {
            @Override
            public void addToCart(Products products) {
                addToCartHomeFragment(products);
            }
        });
        loadProductList();
        return view;

    }

    private void addToCartHomeFragment(Products product) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int check = 0;
                CartDAO cartDAO = AppDatabase.getInstance(getActivity()).cartDAO();
                Cart newCart = cartDAO.checkIdCart(product.getProductID());
                if (newCart != null) {
                    // neu da ton tai tang gia tri len 1
                    newCart.setQuantity(newCart.getQuantity() + 1);
                    Toast.makeText(getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();
                }
                // neu ko co
                Cart newCartToAdd = new Cart(product.getProductID(), product.getProductName(), 1);
                Toast.makeText(getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cartDAO.insertCart(newCartToAdd);
                    }
                });
            }
        });
//        Cart newCart = AppDatabase.getInstance(getActivity()).cartDAO().checkIdCart(product.getProductID());
//        if (newCart != null) {
//            // neu da ton tai tang gia tri len 1
//            newCart.setQuantity(newCart.getQuantity() + 1);
//            Toast.makeText(getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();
//        }
//        // neu ko co
//        Cart newCartToAdd = new Cart(product.getProductID(), product.getProductName(), 1);
//        AppDatabase.getInstance(getContext()).cartDAO().insertCart(newCartToAdd);
//        Toast.makeText(getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();
    }

    private void loadProductList() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ProductDAO productDAO = AppDatabase.getInstance(getActivity()).productDAO();
                productsList = productDAO.getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productAdapter.setData(productsList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(productAdapter);
                    }
                });
            }
        });
    }


}
