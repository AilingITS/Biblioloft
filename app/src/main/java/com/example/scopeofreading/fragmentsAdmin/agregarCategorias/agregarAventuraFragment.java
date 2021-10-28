package com.example.scopeofreading.fragmentsAdmin.agregarCategorias;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.scopeofreading.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class agregarAventuraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View vista;
    private String userID;
    private FirebaseAuth mAuth;

    Button btn_añadirLibro;
    private EditText nombreLibro, descripcionLibro, paginasLibro;
    private String libroID, saveCurrentDate, saveCurrentTime;

    private DatabaseReference dbRef;
    private StorageReference ImagesRef;

    private ImageView btn_agregar_libro;
    private static final int GalleryPick = 1;
    private static final int RESULT_OK = -1;
    private Uri ImageUri;
    private String downloadImageUrl;



    public agregarAventuraFragment() {
        // Required empty public constructor
    }

    public static agregarAventuraFragment newInstance(String param1, String param2) {
        agregarAventuraFragment fragment = new agregarAventuraFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_agregar_aventura, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("cenas");
        ImagesRef = FirebaseStorage.getInstance().getReference().child("cenas");

        return vista;
    }
}