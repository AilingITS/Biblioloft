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
import com.example.biblioloft.firebase.admin_registro_books.Cientifico;
import com.example.biblioloft.firebase.admin_registro_books.cientificoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class registroCientificosFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;

    RecyclerView recyclerView;
    DatabaseReference dbRef;
    cientificoAdapter myAdapter;
    ArrayList<Cientifico> list;


    public registroCientificosFragment() {
        // Required empty public constructor
    }

    public static registroCientificosFragment newInstance(String param1, String param2) {
        registroCientificosFragment fragment = new registroCientificosFragment();
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
        view = inflater.inflate(R.layout.fragment_registro_cientificos, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("books").child("cientifico");

        recyclerView = view.findViewById(R.id.cientificosList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new cientificoAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Cientifico books = dataSnapshot.getValue(Cientifico.class);
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