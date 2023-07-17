package com.example.sqllite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllite.AppDatabase;
import com.example.sqllite.Models.Cart;
import com.example.sqllite.Models.Products;
import com.example.sqllite.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private List<Products> productsList;

    private IClickProductAdapter iClickProductAdapter;

    public void setData(List<Products> productsList){
        this.productsList = productsList;
        notifyDataSetChanged();
    }

    public ProductAdapter() {
    }

    public ProductAdapter(IClickProductAdapter iClickProductAdapter) {
        this.iClickProductAdapter = iClickProductAdapter;
    }

    public interface IClickProductAdapter {
        void addToCart(Products products);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productsList.get(position);
        if (product == null){
            return;
        }
        //if (product.getProductImage() != null) holder.product_image.setImageResource(Integer.parseInt(product.getProductImage()));
        holder.product_name.setText(product.getProductName());

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickProductAdapter.addToCart(product);
                // check xem lieu product co trong database ko

//                Cart newCart = AppDatabase.getInstance(v.getContext()).cartDAO().checkIdCart(product.getProductID());
//                if (newCart != null) {
//                    // neu da ton tai tang gia tri len 1
//                    newCart.setQuantity(newCart.getQuantity() + 1);
//                    Toast.makeText(v.getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();
//                }
//                // neu ko co
//                Cart newCartToAdd = new Cart(product.getProductID(), product.getProductName(), 1);
//                AppDatabase.getInstance(v.getContext()).cartDAO().insertCart(newCartToAdd);
//                Toast.makeText(v.getContext(), "Add to cart sucessfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (productsList != null){
            return productsList.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_image;
        private TextView product_name;
        private Button btn_add_to_cart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            btn_add_to_cart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
