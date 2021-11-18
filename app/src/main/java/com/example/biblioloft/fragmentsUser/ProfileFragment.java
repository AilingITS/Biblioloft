package com.example.biblioloft.fragmentsUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.biblioloft.Prevalent.Prevalent;
import com.example.biblioloft.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private String mParam1;
    private String mParam2;

    private View vista;
    private ImageView profile_image;
    private TextView profile_user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Declaramos la vista del fragment para retornarlo al final
        vista = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = vista.findViewById(R.id.profile_image);
        profile_user = vista.findViewById(R.id.profile_user);


        userInfoDisplay(profile_user, profile_image);

        return vista;
    }

    private void userInfoDisplay(final TextView perfil_usuario, final ImageView fotoperfil) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getNombre());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("Image").exists()){
                        String image = snapshot.child("Image").getValue().toString();
                        Picasso.get().load(image).into(fotoperfil);
                    }
                    String name = snapshot.child("Nombre").getValue().toString();
                    perfil_usuario.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}