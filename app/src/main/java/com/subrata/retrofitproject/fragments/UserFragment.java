package com.subrata.retrofitproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.adapters.UserAdapter;
import com.subrata.retrofitproject.api.RetrofitClient;
import com.subrata.retrofitproject.models.User;
import com.subrata.retrofitproject.models.UsersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private ImageView img_error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Initialize the error image view
        img_error = view.findViewById(R.id.img_error);


        //Call api to get the list of user through the end point
        Call<UsersResponse> call = RetrofitClient.getInstance().getApi().getUsers();
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                //the response if no error occurs. success, get the user and fill in the recycler-view
                if (response.body() != null) {

                    //make the error image invisible
                    img_error.setVisibility(View.INVISIBLE);

                    //update adapter
                    userList = response.body().getUsers();
                    userAdapter = new UserAdapter(getActivity(), userList);
                    recyclerView.setAdapter(userAdapter);

                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {

                //User authentication failed.get the proper response to the user.
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                img_error.setVisibility(View.VISIBLE);
            }
        });
    }
}
