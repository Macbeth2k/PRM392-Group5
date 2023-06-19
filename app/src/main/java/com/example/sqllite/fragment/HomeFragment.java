package com.example.sqllite.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllite.R;
import com.example.sqllite.book.Book;
import com.example.sqllite.category.Category;
import com.example.sqllite.category.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rcv_cate);
        adapter = new CategoryAdapter(getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter.setData(getListCategory());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<Category> getListCategory() {
        List<Category> categories = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        books.add(new Book(R.drawable.a80, "Book 1"));
        books.add(new Book(R.drawable.f65_2, "Book 2"));
        books.add(new Book(R.drawable.f65, "Book 3"));
        books.add(new Book(R.drawable.f96, "Book 4"));
        books.add(new Book(R.drawable.og80, "Book 5"));
        books.add(new Book(R.drawable.f97, "Book 6"));
        books.add(new Book(R.drawable.l80, "Book 7"));
        books.add(new Book(R.mipmap.ic_launcher, "Book 8"));

        categories.add(new Category("cate 1", books));
        categories.add(new Category("cate 2", books));
        categories.add(new Category("cate 3", books));
        categories.add(new Category("cate 4", books));
        categories.add(new Category("cate 5", books));
        categories.add(new Category("cate 6", books));
        categories.add(new Category("cate 7", books));
        categories.add(new Category("cate 8", books));
        categories.add(new Category("cate 9", books));


        return categories;
    }


}
