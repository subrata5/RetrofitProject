package com.subrata.retrofitproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.activities.MainActivity;
import com.subrata.retrofitproject.api.RetrofitClient;
import com.subrata.retrofitproject.models.DefaultResponse;
import com.subrata.retrofitproject.models.LoginResponse;
import com.subrata.retrofitproject.models.User;
import com.subrata.retrofitproject.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private EditText et_name, et_email, et_school;
    private EditText et_currentpassword, et_newpassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialization of the edit texts.
        et_name = view.findViewById(R.id.et_name);
        et_email = view.findViewById(R.id.et_email);
        et_school = view.findViewById(R.id.et_school);
        et_currentpassword = view.findViewById(R.id.et_currentpass);
        et_newpassword = view.findViewById(R.id.et_newpass);

        //On click listener view initialize
        view.findViewById(R.id.btn_save).setOnClickListener(this);
        view.findViewById(R.id.btn_change).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_save:
                //Update the profile through the update api call
                updateProfile();
                break;

            case R.id.btn_change:
                //Change password
                changePassword();
                break;

            case R.id.btn_logout:
                //Clear the value from the shared preference
                SharedPrefManager.getInstance(getActivity()).clear();

                //get the user from the current view to the login/sign-up view
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //Toast for success message
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_delete:
                break;
        }

    }

    /**
     * Change password method to validate user and change password through api call
     */
    private void changePassword() {

        //check for empty fields
        String str_currentpassword = et_currentpassword.getText().toString().trim();
        String str_newpassword = et_newpassword.getText().toString().trim();

        if (str_currentpassword.isEmpty()) {
            et_currentpassword.setError("Password required");
            et_currentpassword.requestFocus();
            return;
        }

        if (str_newpassword.isEmpty()) {
            et_newpassword.setError("Enter new password");
            et_newpassword.requestFocus();
            return;
        }

        //get the existing user
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        //make the api call
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi()
                .updatePassword(str_currentpassword, str_newpassword, user.getEmail());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.e(TAG, "Response user: " + response.body().getUser());
                Log.e(TAG, "Response message: " + response.body().getMessage());
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * This method updates the user profile records
     * first check for the validations and the call for the validate api
     */
    private void updateProfile() {

        //Get the string value form the user entered from the editText
        String str_email = et_email.getText().toString().trim();
        String str_name = et_name.getText().toString().trim();
        String str_school = et_school.getText().toString().trim();


        if (str_email.isEmpty()) {
            et_email.setError("Email is required!");
            et_email.requestFocus();
            return;
        }

        //check for the email if is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            et_email.setError("Enter valid Email");
            et_email.requestFocus();
            return;
        }

        //check for empty name field
        if (str_name.isEmpty()) {
            et_name.setError("Name is required!");
            et_name.requestFocus();
            return;
        }

        //check for empty school field
        if (str_school.isEmpty()) {
            et_school.setError("School is required!");
            et_school.requestFocus();
            return;
        }

        //get the existing user
        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        //call the update user api
        Call<LoginResponse> call = RetrofitClient.getInstance()
                .getApi().updateUser(
                        user.getId(),
                        str_email,
                        str_name,
                        str_school
                );

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                //Save the edited user in the shared preference
                if (!response.body().isError()) {
                    SharedPrefManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
