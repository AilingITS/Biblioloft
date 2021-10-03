package com.example.scopeofreading.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.scopeofreading.LoginActivity;
import com.example.scopeofreading.MainActivity;
import com.example.scopeofreading.R;
import com.example.scopeofreading.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View vista;
    Button btncerrarSesion;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Paper.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_settings, container, false);

        btncerrarSesion = (Button) vista.findViewById(R.id.btncerrarSesion);
        btncerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent (getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            }
        });
        return vista;
    }
}