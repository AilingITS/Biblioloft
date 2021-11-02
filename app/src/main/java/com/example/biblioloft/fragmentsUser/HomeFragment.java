package com.example.biblioloft.fragmentsUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biblioloft.R;
import com.example.biblioloft.firebase.user_home_books.user_Aventura;
import com.example.biblioloft.firebase.user_home_books.user_Cientifico;
import com.example.biblioloft.firebase.user_home_books.user_Romanticos;
import com.example.biblioloft.firebase.user_home_books.user_aventuraAdapter;
import com.example.biblioloft.firebase.user_home_books.user_cientificoAdapter;
import com.example.biblioloft.firebase.user_home_books.user_romanticosAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    //Variales libros cientificos
    RecyclerView recyclerView_cientificos;
    DatabaseReference dbRef_cientificos;
    user_cientificoAdapter myAdapter_cientificos;
    ArrayList<user_Cientifico> list_cientificos;

    //Variales libros aventuras
    RecyclerView recyclerView_aventura;
    DatabaseReference dbRef_aventura;
    user_aventuraAdapter myAdapter_aventura;
    ArrayList<user_Aventura> list_aventura;

    //Variales libros romanticos
    RecyclerView recyclerView_romanticos;
    DatabaseReference dbRef_romanticos;
    user_romanticosAdapter myAdapter_romanticos;
    ArrayList<user_Romanticos> list_romanticos;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //SCROLL HORIZONTAL CIENTIFICOS
        dbRef_cientificos = FirebaseDatabase.getInstance().getReference("books").child("cientifico");

        recyclerView_cientificos = view.findViewById(R.id.topLibros_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_cientificos.setHasFixedSize(true);
        recyclerView_cientificos.setItemAnimator(new DefaultItemAnimator());
        recyclerView_cientificos.setLayoutManager(layoutManager);

        list_cientificos = new ArrayList<>();
        myAdapter_cientificos = new user_cientificoAdapter(getContext(), list_cientificos);
        recyclerView_cientificos.setAdapter(myAdapter_cientificos);

        dbRef_cientificos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Cientifico books = dataSnapshot.getValue(user_Cientifico.class);
                    list_cientificos.add(books);
                }
                myAdapter_cientificos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL AVENTURAS
        dbRef_aventura = FirebaseDatabase.getInstance().getReference("books").child("aventura");

        recyclerView_aventura = view.findViewById(R.id.aventuraLibros_List);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_aventura.setHasFixedSize(true);
        recyclerView_aventura.setItemAnimator(new DefaultItemAnimator());
        recyclerView_aventura.setLayoutManager(layoutManager2);

        list_aventura = new ArrayList<>();
        myAdapter_aventura = new user_aventuraAdapter(getContext(), list_aventura);
        recyclerView_aventura.setAdapter(myAdapter_aventura);

        dbRef_aventura.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Aventura books = dataSnapshot.getValue(user_Aventura.class);
                    list_aventura.add(books);
                }
                myAdapter_aventura.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL ROMANTICOS
        dbRef_romanticos = FirebaseDatabase.getInstance().getReference("books").child("romanticos");

        recyclerView_romanticos = view.findViewById(R.id.romanticosLibros_List);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_romanticos.setHasFixedSize(true);
        recyclerView_romanticos.setItemAnimator(new DefaultItemAnimator());
        recyclerView_romanticos.setLayoutManager(layoutManager3);

        list_romanticos = new ArrayList<>();
        myAdapter_romanticos = new user_romanticosAdapter(getContext(), list_romanticos);
        recyclerView_romanticos.setAdapter(myAdapter_romanticos);

        dbRef_romanticos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Romanticos books = dataSnapshot.getValue(user_Romanticos.class);
                    list_romanticos.add(books);
                }
                myAdapter_romanticos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        return view;
    }
}