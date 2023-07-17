package com.example.sqllite;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sqllite.fragment.CartFragment;
import com.example.sqllite.fragment.ChangePasswordFragment;
import com.example.sqllite.fragment.HistoryFragment;
import com.example.sqllite.fragment.HomeFragment;
import com.example.sqllite.fragment.MyProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int MY_REQUEST_CODE = 10;
    private DrawerLayout drawerLayout;
    private static int FRAGMENT_HOME = 0;
    private static int FRAGMENT_CART = 1;
    private static int FRAGMENT_HISTORY = 2;
    private static int FRAGMENT_MY_PROFILE = 3;
    private static int FRAGMENT_CHANGE_PASSWORD = 4;
    private int currentFragment = FRAGMENT_HOME;
    private ImageView img_avatar;
    private TextView tvName, tvEmail;
    private NavigationView navigationView;
    final private MyProfileFragment fragment = new MyProfileFragment();
    final private ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        if (intent == null){
                            return;
                        }
                        Uri uri = intent.getData();
                        fragment.setUri(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), uri);
                            fragment.setBitmapImageView(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //------------------------------------------------

        setContentView(R.layout.activity_user);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        initUi();

        drawerLayout = findViewById(R.id.dwawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_dwawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        showUserInformation();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(internetReceiver, filter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            if (currentFragment != FRAGMENT_HOME){
                replaceFragment(new HomeFragment());
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_fav) {
            if (currentFragment != FRAGMENT_CART){
                replaceFragment(new CartFragment());
                currentFragment = FRAGMENT_CART;
            }
        } else if (id == R.id.nav_history) {
            if (currentFragment != FRAGMENT_HISTORY){
                replaceFragment(new HistoryFragment());
                currentFragment = FRAGMENT_HISTORY;
            }
        } else if (id == R.id.nav_profile) {
            if (currentFragment != FRAGMENT_MY_PROFILE){
                replaceFragment(fragment);
                currentFragment = FRAGMENT_MY_PROFILE;
            }
        } else if (id == R.id.nav_change_pass) {
            if (currentFragment != FRAGMENT_CHANGE_PASSWORD){
                replaceFragment(new ChangePasswordFragment());
                currentFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void initUi(){
        navigationView = findViewById(R.id.navigation_view);
        img_avatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
    }

    public void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if (name == null){
            tvName.setVisibility(View.GONE);
        }else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tvEmail.setText(email);
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_ava_default).into(img_avatar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(Intent.createChooser(intent,getString(R.string.gallery)));
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(UserActivity.this.getString(R.string.app_name))
                .setMessage(UserActivity.this.getString(R.string.no_internet_detect))
                .setPositiveButton(UserActivity.this.getString(R.string.close_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(UserActivity.this.getString(R.string.wait), null)
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
}