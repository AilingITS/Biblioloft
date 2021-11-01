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
import com.example.biblioloft.firebase.fbRegistroLibros.Literatura;
import com.example.biblioloft.firebase.fbRegistroLibros.Misterio;
import com.example.biblioloft.firebase.fbRegistroLibros.Poeticos;
import com.example.biblioloft.firebase.fbRegistroLibros.literaturaAdapter;
import com.example.biblioloft.firebase.fbRegistroLibros.misterioAdapter;
import com.example.biblioloft.firebase.fbRegistroLibros.poeticosAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class registroPoeticosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;

    RecyclerView recyclerView;
    DatabaseReference dbRef;
    poeticosAdapter myAdapter;
    ArrayList<Poeticos> list;

    public registroPoeticosFragment() {
        // Required empty public constructor
    }

    public static registroPoeticosFragment newInstance(String param1, String param2) {
        registroPoeticosFragment fragment = new registroPoeticosFragment();
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
        view = inflater.inflate(R.layout.fragment_registro_poeticos, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("books").child("poeticos");

        recyclerView = view.findViewById(R.id.poeticosList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new poeticosAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Poeticos books = dataSnapshot.getValue(Poeticos.class);
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