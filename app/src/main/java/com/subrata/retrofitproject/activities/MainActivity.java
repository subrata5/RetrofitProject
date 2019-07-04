package com.subrata.retrofitproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.subrata.retrofitproject.models.DefaultResponse;
import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.api.RetrofitClient;
import com.subrata.retrofitproject.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private EditText editText_email, editText_password, editText_name, editText_school;
    private TextView textView_login;
    private Button btn_signUp;

    /**
     * Initiate the views
     */
    private void init() {
        editText_email = findViewById(R.id.et_email);
        editText_password = findViewById(R.id.et_password);
        editText_name = findViewById(R.id.et_name);
        editText_school = findViewById(R.id.et_school);
        textView_login = findViewById(R.id.tv_login);
        btn_signUp = findViewById(R.id.btn_signUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling the init method to initiate the views
        init();

        //Setting the onClick listeners for the views here
        textView_login.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
    }

    /**
     * onClick for the views goes here.. saperated by the views
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:
                //case for login text view click
                navigateToLoginPage();
                break;

            case R.id.btn_signUp:
                //case for the button signUp click
                userSignUp();
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
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    /**
     * On Click take the user to the login page
     */
    private void navigateToLoginPage() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method to get the user credentials
     * and call the api to create new user
     */
    private void userSignUp() {

        //Get the string value form the user entered from the editText
        String str_email = editText_email.getText().toString().trim();
        String str_password = editText_password.getText().toString().trim();
        String str_name = editText_name.getText().toString().trim();
        String str_school = editText_school.getText().toString().trim();

        //Check for email is empty then give error messgae
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


        //check for empty name field
        if (str_name.isEmpty()) {
            editText_name.setError("Name is required!");
            editText_name.requestFocus();
            return;
        }

        //check for empty school field
        if (str_school.isEmpty()) {
            editText_school.setError("School is required!");
            editText_school.requestFocus();
            return;
        }


        /* Do register the user using the api call */
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(str_email, str_password, str_name, str_school);

//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                String stored_reponse = null;
//
//                try {
//                    //If success
//                    if (response.code() == 201) {
//                        stored_reponse = response.body().string();
//
//                    } else {
//                        //On error
//                        stored_reponse = response.errorBody().string();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//                //Parse the JSON Object
//                if (stored_reponse != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(stored_reponse);
//                        Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                try {
                    if (response.code() == 201) {
                        DefaultResponse defaultResponse = response.body();
                        Toast.makeText(MainActivity.this, defaultResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 422) {
                        Toast.makeText(MainActivity.this, "Something's went wrong!", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        Toast.makeText(MainActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

    }
}
