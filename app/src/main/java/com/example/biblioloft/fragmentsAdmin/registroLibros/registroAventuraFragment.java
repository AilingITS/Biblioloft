package com.example.biblioloft.fragmentsAdmin.registroLibros;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biblioloft.R;
import com.example.biblioloft.firebase.fbRegistroLibros.Aventura;
import com.example.biblioloft.firebase.fbRegistroLibros.Cientifico;
import com.example.biblioloft.firebase.fbRegistroLibros.aventuraAdapter;
import com.example.biblioloft.firebase.fbRegistroLibros.cientificoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class registroAventuraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;

    RecyclerView recyclerView;
    DatabaseReference dbRef;
    aventuraAdapter myAdapter;
    ArrayList<Aventura> list;

    public registroAventuraFragment() {
        // Required empty public constructor
    }

    public static registroAventuraFragment newInstance(String param1, String param2) {
        registroAventuraFragment fragment = new registroAventuraFragment();
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
        view = inflater.inflate(R.layout.fragment_registro_aventura, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("books").child("aventura");

        recyclerView = view.findViewById(R.id.aventuraList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new aventuraAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Aventura books = dataSnapshot.getValue(Aventura.class);
                    list.add(books);
                }
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
        return view;
    }
}