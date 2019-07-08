package com.subrata.retrofitproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.subrata.retrofitproject.R;
import com.subrata.retrofitproject.storage.SharedPrefManager;

public class HomeFragment extends Fragment {


    private TextView textViewEmail, textViewName, textViewSchool;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewSchool = view.findViewById(R.id.textViewSchool);


        //set the text views taking the values form the shared preferences
        textViewName.setText(SharedPrefManager.getInstance(getContext()).getUser().getName());
        textViewEmail.setText(SharedPrefManager.getInstance(getContext()).getUser().getEmail());
        textViewSchool.setText(SharedPrefManager.getInstance(getContext()).getUser().getSchool());

    }
}
