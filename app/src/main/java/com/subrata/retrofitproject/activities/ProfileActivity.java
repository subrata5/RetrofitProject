package com.subrata.retrofitproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.fragments.AboutFragment;
import com.subrata.retrofitproject.fragments.HomeFragment;
import com.subrata.retrofitproject.fragments.SettingsFragment;
import com.subrata.retrofitproject.fragments.UserFragment;
import com.subrata.retrofitproject.storage.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private void init() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initiate the views
        init();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        //When activity loads display the home fragment
        displayFragment(new HomeFragment());
    }

    /**
     * If the user is not logged in then go to the signup/login screen
     */
    @Override
    protected void onStart() {
        super.onStart();

        //check if the user is already logged in.
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            //Start profile activity and close background activities
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }


    /**
     * Display faragment depending on the user choice
     */
    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relative_container, fragment)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        //When the user clicks from the buttom navigation view,
        //change the view to the desired one
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_user:
                fragment = new UserFragment();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.menu_about:
                fragment = new AboutFragment();
                break;
        }

        //check if the fragment value is null
        if (fragment != null) {
            displayFragment(fragment);
        }

        return false;
    }
}

