package com.example.sqllite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText edt_email, edt_password, edt_cf_password;
    private Button btnSignup;
    private ProgressBar progressBar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
        initListener();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(internetReceiver, filter);
    }

    private void initListener() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String cf_password = edt_cf_password.getText().toString().trim();
        if (!password.equals(cf_password)){
            Toast.makeText(SignUpActivity.this,getString(R.string.cf_pass_error), Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //add role user to account on firebase
                            addUserToFirebase(email);
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(SignUpActivity.this, UserActivity.class);
                                startActivity(intent);
                                finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            if (isNetworkAvailable(SignUpActivity.this)) {
                                Toast.makeText(SignUpActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.pass_constraint),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void addUserToFirebase(String email) {
        DatabaseReference myRef = database.getReference(getString(R.string.firebase_email_table));
        DatabaseReference newChild = myRef.push();
        newChild.setValue(email);
        myRef.addChildEventListener(addRoleListener(email));
    }


    private void initUi() {
        edt_email = findViewById(R.id.edt_email_signup);
        edt_password = findViewById(R.id.edt_password_signup);
        edt_cf_password = findViewById(R.id.edt_cf_password_signup);
        btnSignup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.processBar_signup);
    }

    private ChildEventListener addRoleListener(String name){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(String.class).equals(name)){
                    String id = snapshot.getKey();
                    //add vao role
                    DatabaseReference myRef2 = database.getReference(getString(R.string.firebase_role_table));
                    myRef2.child(id).setValue(getString(R.string.role_user));
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

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(SignUpActivity.this.getString(R.string.app_name))
                .setMessage(SignUpActivity.this.getString(R.string.no_internet_detect))
                .setPositiveButton(SignUpActivity.this.getString(R.string.close_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(SignUpActivity.this.getString(R.string.wait), null)
                .create();
        AlertDialog alert = builder.create();
        alert.show();
    }

    private final BroadcastReceiver internetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Context applicationContext = context.getApplicationContext();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                if (!isNetworkAvailable(context)){
                    displayAlert();
                }
            }
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
    };

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