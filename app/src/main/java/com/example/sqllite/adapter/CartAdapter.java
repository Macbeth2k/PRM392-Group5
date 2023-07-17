package com.example.sqllite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllite.Models.Cart;
import com.example.sqllite.R;
import com.example.sqllite.UserActivity;
import com.example.sqllite.fragment.PurchaseFragment;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> listCart;
    private IClickItemCart iClickItemCart;

    public CartAdapter(IClickItemCart iClickItemCart) {
        this.iClickItemCart = iClickItemCart;
    }
    public void setData (List<Cart> listCart) {
        this.listCart = listCart;
        notifyDataSetChanged();
    }

    public interface IClickItemCart {
        void deleteFromCart(Cart cart);
        void updateAmount(Cart cart);

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if (cart == null){
            return;
        }
        holder.edt_product_id.setText("Product ID: " + cart.getProductId());
        holder.edt_product_name.setText("Product Name: " + cart.getProductName());
        holder.edt_quantity.setText("Quantity: " + cart.getQuantity());

        holder.btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.setQuantity(cart.getQuantity() + 1);
                holder.edt_quantity.setText("Quantity: " + cart.getQuantity());
                Toast.makeText(v.getContext(), "Add one amount sucessfully", Toast.LENGTH_SHORT).show();
                iClickItemCart.updateAmount(cart);
            }
        });

        holder.btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.getQuantity() >= 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    holder.edt_quantity.setText("Quantity: " + cart.getQuantity());
                    Toast.makeText(v.getContext(), "Delete one amount sucessfully", Toast.LENGTH_SHORT).show();
                    iClickItemCart.updateAmount(cart);
                }
                else {
                    Toast.makeText(v.getContext(), "Amount can't lower than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemCart.deleteFromCart(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listCart != null) {
            return listCart.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private TextView edt_product_id;
        private TextView edt_product_name;
        private TextView edt_quantity;
        private Button btn_increase;
        private Button btn_decrease;
        private Button btn_delete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_product_id = itemView.findViewById(R.id.edt_product_id);
            edt_product_name = itemView.findViewById(R.id.edt_product_name);
            edt_quantity = itemView.findViewById(R.id.edt_quantity);
            btn_increase = itemView.findViewById(R.id.btn_increase);
            btn_decrease = itemView.findViewById(R.id.btn_decrease);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
