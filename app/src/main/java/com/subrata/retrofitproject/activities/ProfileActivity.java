package com.subrata.retrofitproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.models.User;
import com.subrata.retrofitproject.storage.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private TextView tv_Name;
    private Button btn_logout;

    private void init() {
        tv_Name = findViewById(R.id.tv_Name);
        btn_logout = findViewById(R.id.btn_logout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initiate the views
        init();

        User user = SharedPrefManager.getInstance(this).getUser();
        tv_Name.setText("Welcome "+user.getName());

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(ProfileActivity.this).clear();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
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
}
