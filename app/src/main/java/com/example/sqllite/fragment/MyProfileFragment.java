package com.example.sqllite.fragment;

import static com.example.sqllite.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sqllite.MainActivity;
import com.example.sqllite.R;
import com.example.sqllite.book.Book;
import com.example.sqllite.category.Category;
import com.example.sqllite.category.CategoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment {
    private View view;
    private ImageView img_ava;
    private EditText edtFullname, edtEmail;
    private Button btnUpdateProfile, getBtnUpdateEmail;
    private Uri uri;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        activity = (MainActivity) getActivity();
        initUi();
        setUserInformation();
        initListener();
        return view;
    }

    private void initUi(){
        img_ava = view.findViewById(R.id.image_avatar);
        edtFullname = view.findViewById(R.id.edt_full_name);
        edtEmail = view.findViewById(R.id.edt_email_profile);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        getBtnUpdateEmail = view.findViewById(R.id.btn_update_email);
    }

    private void initListener() {
        img_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
        getBtnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickUpdateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = edtEmail.getText().toString().trim();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"update email successful", Toast.LENGTH_SHORT).show();
                            activity.showUserInformation();
                        } else {
                            Toast.makeText(getActivity(),"update email failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String fullname = edtFullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullname)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"update profile successful", Toast.LENGTH_SHORT);
                            activity.showUserInformation();
                        } else {
                            Toast.makeText(getActivity(),"update profile failed", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void onClickRequestPermission() {
        if (activity == null){
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            activity.openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            activity.openGallery();
        }else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        edtFullname.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.ic_ava_default).into(img_ava);
        img_ava.getImageAlpha();
    }


    public void setBitmapImageView(Bitmap bitmapImageView){
        img_ava.setImageBitmap(bitmapImageView);
    }

}
