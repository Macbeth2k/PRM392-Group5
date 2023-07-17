package com.example.sqllite.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllite.AppDatabase;
import com.example.sqllite.DAO.CartDAO;
import com.example.sqllite.Models.Cart;
import com.example.sqllite.R;
import com.example.sqllite.adapter.CartAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment {

    private RecyclerView rcv_cart;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;

    private Button btn_purchase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        rcv_cart = view.findViewById(R.id.rcv_cart);

        cartAdapter = new CartAdapter(new CartAdapter.IClickItemCart() {
            @Override
            public void deleteFromCart(Cart cart) {
                deleteFromCartNow(cart);
            }
            @Override
            public void updateAmount(Cart cart) {
                updateAmountNow(cart);
            }

        });
        loadCartList();

        btn_purchase = view.findViewById(R.id.btn_purchase);
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new PurchaseFragment());
                transaction.commit();

            }
        });
        return view;
    }

    private void updateAmountNow(Cart cart) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CartDAO cartDAO = AppDatabase.getInstance(getActivity()).cartDAO();
                cartDAO.updateCartQuantityWithProductId(cart.getProductId(), cart.getQuantity() );
            }
        });
    }


    private void deleteFromCartNow(Cart cart) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CartDAO cartDAO = AppDatabase.getInstance(getActivity()).cartDAO();
                Cart new_cart = cartDAO.getOneCart(cart.getCartId());
                if (new_cart != null) {
                    cartDAO.deleteCart(new_cart);
                    loadCartList();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (new_cart != null) {

                            Toast.makeText(getContext(), "Delete from cart successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "This cart is not exist anymore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }

    private void loadCartList() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                CartDAO cartDAO = AppDatabase.getInstance(getActivity()).cartDAO();
                cartList = cartDAO.getListCart();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cartAdapter.setData(cartList);
                        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        rcv_cart.setLayoutManager(manager);
                        rcv_cart.setAdapter(cartAdapter);
                    }
                });
            }
        });
    }
}
