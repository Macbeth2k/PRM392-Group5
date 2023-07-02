package com.example.sqllite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sqllite.fragment.AdminHomeFragment;
import com.example.sqllite.fragment.ChangePasswordFragment;
import com.example.sqllite.fragment.FavouriteFragment;
import com.example.sqllite.fragment.HistoryFragment;
import com.example.sqllite.fragment.HomeFragment;
import com.example.sqllite.fragment.MyProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static int FRAGMENT_HOME = 0;
    private static int FRAGMENT_FAVOURITE = 1;
    private static int FRAGMENT_HISTORY = 2;
    private static int FRAGMENT_MY_PROFILE = 3;
    private static int FRAGMENT_CHANGE_PASSWORD = 4;
    private int currentFragment = FRAGMENT_HOME;
    private ImageView img_avatar;
    private TextView tvName, tvEmail;
    private NavigationView navigationView;
    final private MyProfileFragment fragment = new MyProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        initUi();

        drawerLayout = findViewById(R.id.dwawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_dwawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new AdminHomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        showUserInformation();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            if (currentFragment != FRAGMENT_HOME){
                replaceFragment(new AdminHomeFragment());
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_fav) {
            if (currentFragment != FRAGMENT_FAVOURITE){
                replaceFragment(new FavouriteFragment());
                currentFragment = FRAGMENT_FAVOURITE;
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
        Glide.with(this).load(photoUrl).error(R.drawable.ic_ava_default).into(img_avatar);
    }
}