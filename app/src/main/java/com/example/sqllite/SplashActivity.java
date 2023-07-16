package com.example.sqllite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);

    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || isNetworkAvailable(SplashActivity.this)){
            //haven't logged in
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            this.finish();
        }else {
            //have logged in
            String email = user.getEmail();
            getHomeActivity(email);
        }

    }

    private void getHomeActivity(String email) {
        DatabaseReference myRef = database.getReference(getString(R.string.firebase_email_table));
        myRef.addChildEventListener(getRoleFromName(email));
    }

    private ChildEventListener getRoleFromId(String id){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals(id)){
                    String role = snapshot.getValue(String.class);
                    if (role.equals(R.string.role_admin)){
                        Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        Intent intent = new Intent(SplashActivity.this, UserActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    //move
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
        };
    }

    private ChildEventListener getRoleFromName(String name){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(String.class).equals(name)){
                    String id = snapshot.getKey();
                    DatabaseReference myRef = database.getReference(getString(R.string.firebase_role_table));
                    myRef.addChildEventListener(getRoleFromId(id));
               }//else {
//                    Toast.makeText(SplashActivity.this,"please signin",Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
//                    startActivity(intent);
//                }
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
        };
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Network network = manager.getActiveNetwork();
            if (network == null){
                return false;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }
}