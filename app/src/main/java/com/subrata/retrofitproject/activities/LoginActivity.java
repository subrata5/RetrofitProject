package com.subrata.retrofitproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.api.RetrofitClient;
import com.subrata.retrofitproject.models.LoginResponse;
import com.subrata.retrofitproject.storage.SharedPrefManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText editText_email, editText_password;
    private TextView text_register;
    private Button btn_logIn;

    private void init() {
        editText_email = findViewById(R.id.et_email);
        editText_password = findViewById(R.id.et_password);
        text_register = findViewById(R.id.tv_register);
        btn_logIn = findViewById(R.id.btn_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initiate the views
        init();

        //set on click listeners for the button and text-view
        text_register.setOnClickListener(this);
        btn_logIn.setOnClickListener(this);

    }


    /**
     * OnClick listeners for the views
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userLogin();
                break;

            case R.id.tv_register:
                navigateToRegisterPage();
                break;
        }
    }


    /**
     * Check if the user is logged in.. proceed to Profile page
     */
    @Override
    protected void onStart() {
        super.onStart();

        //check if the user is already logged in.
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //Start profile activity and close background activities
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    /**
     * Takes the user to the register page
     */
    private void navigateToRegisterPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Create user method
     */
    private void userLogin() {

        //Get the string value form the user entered from the editText
        String str_email = editText_email.getText().toString().trim();
        String str_password = editText_password.getText().toString().trim();

        //Check for email is empty then give error message
        if (str_email.isEmpty()) {
            editText_email.setError("Email is required!");
            editText_email.requestFocus();
            return;
        }

        //check for the email if is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            editText_email.setError("Enter valid Email");
            editText_email.requestFocus();
            return;
        }

        //check for the password field for empty
        if (str_password.isEmpty()) {
            editText_password.setError("Password is required!");
            editText_password.requestFocus();
            return;
        }

        //check for a valid password.
        if (str_password.length() < 6) {
            editText_password.setError("Password must be atleast 6 characters");
            editText_password.requestFocus();
            return;
        }

        //Call api for login with the required fields
        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(str_email, str_password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (!loginResponse.isError()) {
                    //not error
                    //save user
                    //open profile

                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveUser(loginResponse.getUser());

                    //Start profile activity and close background activities
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    //error occourd
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }
}
