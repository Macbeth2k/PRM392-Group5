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
import com.example.sqllite.Models.Cart;
import com.example.sqllite.Models.Products;
import com.example.sqllite.R;
import com.example.sqllite.UserActivity;
import com.example.sqllite.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView rcv_cart;
    private CartAdapter cartAdapter;
    private List<Cart> listCart;

    private Button btn_purchase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);
        rcv_cart = view.findViewById(R.id.rcv_cart);

        cartAdapter = new CartAdapter(new CartAdapter.IClickItemCart() {
            @Override
            public void deleteFromCart(Cart cart) {
                deleteFromCartNow(cart);
            }
        });
        //listCart = AppDatabase.getInstance(getActivity()).cartDAO().getListCart();
        listCart = getListCart();
        cartAdapter.setData(listCart);

        btn_purchase = view.findViewById(R.id.btn_purchase);
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new PurchaseFragment());
                transaction.commit();

            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_cart.setLayoutManager(manager);
        rcv_cart.setAdapter(cartAdapter);
        return view;
    }

    private void deleteFromCartNow(Cart cart) {
        //Cart new_cart = AppDatabase.getInstance(getActivity()).cartDAO().getOneCart(cart.getCartId());
        for (int i = 0; i < listCart.size(); i++) {
            Cart new_cart = listCart.get(i);
            if (new_cart != null ) {
                listCart.remove(new_cart);
                Toast.makeText(this.getContext(), "Delete from cart successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this.getContext(), "This cart is not exist anymore", Toast.LENGTH_SHORT).show();
            }
        }

    }



    List<Cart> getListCart() {
        List<Cart> cartList = new ArrayList<>();
        cartList.add(new Cart(2, "Book 1", 1));
        cartList.add(new Cart(3, "Book 2", 2));
        cartList.add(new Cart(6, "Book 3", 3));
        cartList.add(new Cart(11, "Book 4", 4));
        cartList.add(new Cart(55, "Book 6", 90));
        cartList.add(new Cart(4, "Book 9", 10));
        cartList.add(new Cart(5, "Book 11", 20));
        return cartList;
    }
}
