package com.example.sqllite.fragment;

import static com.example.sqllite.UserActivity.MY_REQUEST_CODE;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sqllite.UserActivity;
import com.example.sqllite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfileFragment extends Fragment {
    private View view;
    private ImageView img_ava;
    private EditText edtFullname, edtEmail;
    private Button btnUpdateProfile;
    private ProgressBar progressBar;
    private Uri uri;
    private UserActivity activity;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        activity = (UserActivity) getActivity();
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
        progressBar = view.findViewById(R.id.processBar_update_profile);
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
    }

    private void onClickUpdateProfile() {
        progressBar.setVisibility(View.VISIBLE);
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
//                            Toast.makeText(getActivity(),"update profile successful", Toast.LENGTH_SHORT);
                            String email = edtEmail.getText().toString().trim();
                            //update email realtime
                            updateEmailRealtimeDatabase(user.getEmail(),email);
                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                                Toast.makeText(getActivity(),"update email successful", Toast.LENGTH_SHORT).show();
                                                activity.showUserInformation();
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(),getString(R.string.update_profile_successful), Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getActivity(),getString(R.string.update_profile_fail), Toast.LENGTH_SHORT).show();
//                                                Toast.makeText(getActivity(),"update email failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),getString(R.string.update_profile_fail), Toast.LENGTH_SHORT);
                        }
                    }
                });
    }


    public void setUri(Uri uri) {
        this.uri = uri;
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
        Uri uri1 = user.getPhotoUrl();
        if (user == null){
            return;
        }
        edtFullname.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.ic_ava_default).dontAnimate().into(img_ava);
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
//        img_ava.setImageBitmap(bitmapImageView);
        Glide.with(getActivity()).load(uri).error(R.drawable.ic_ava_default).into(img_ava);
    }

    public void updateEmailRealtimeDatabase(String old_email, String new_email){
        DatabaseReference myRef = database.getReference("account");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(String.class).equals(old_email)){
                    String id = snapshot.getKey();
                    myRef.child(id).setValue(new_email);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
