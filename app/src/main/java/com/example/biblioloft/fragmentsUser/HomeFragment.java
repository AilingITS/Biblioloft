package com.example.biblioloft.fragmentsUser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import com.example.biblioloft.firebase.user_home_books.user_Ficcion;
import com.example.biblioloft.firebase.user_home_books.user_Infantiles;
import com.example.biblioloft.firebase.user_home_books.user_Literatura;
import com.example.biblioloft.firebase.user_home_books.user_Misterio;
import com.example.biblioloft.firebase.user_home_books.user_Poeticos;
import com.example.biblioloft.firebase.user_home_books.user_Romanticos;
import com.example.biblioloft.firebase.user_home_books.user_Terror;
import com.example.biblioloft.firebase.user_home_books.user_aventuraAdapter;
import com.example.biblioloft.firebase.user_home_books.user_cientificoAdapter;
import com.example.biblioloft.firebase.user_home_books.user_ficcionAdapter;
import com.example.biblioloft.firebase.user_home_books.user_infantilesAdapter;
import com.example.biblioloft.firebase.user_home_books.user_literaturaAdapter;
import com.example.biblioloft.firebase.user_home_books.user_misterioAdapter;
import com.example.biblioloft.firebase.user_home_books.user_poeticosAdapter;
import com.example.biblioloft.firebase.user_home_books.user_romanticosAdapter;
import com.example.biblioloft.firebase.user_home_books.user_terrorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;

    //Variales libros aventuras
    RecyclerView recyclerView_aventura;
    DatabaseReference dbRef_aventura;
    user_aventuraAdapter myAdapter_aventura;
    ArrayList<user_Aventura> list_aventura;

    //Variales libros cientificos
    RecyclerView recyclerView_cientificos;
    DatabaseReference dbRef_cientificos;
    user_cientificoAdapter myAdapter_cientificos;
    ArrayList<user_Cientifico> list_cientificos;

    //Variales libros ficcion
    RecyclerView recyclerView_ficcion;
    DatabaseReference dbRef_ficcion;
    user_ficcionAdapter myAdapter_ficcion;
    ArrayList<user_Ficcion> list_ficcion;

    //Variales libros infantiles
    RecyclerView recyclerView_infantiles;
    DatabaseReference dbRef_infantiles;
    user_infantilesAdapter myAdapter_infantiles;
    ArrayList<user_Infantiles> list_infantiles;

    //Variales libros literatura
    RecyclerView recyclerView_literatura;
    DatabaseReference dbRef_literatura;
    user_literaturaAdapter myAdapter_literatura;
    ArrayList<user_Literatura> list_literatura;

    //Variales libros misterio
    RecyclerView recyclerView_misterio;
    DatabaseReference dbRef_misterio;
    user_misterioAdapter myAdapter_misterio;
    ArrayList<user_Misterio> list_misterio;

    //Variales libros poeticos
    RecyclerView recyclerView_poeticos;
    DatabaseReference dbRef_poeticos;
    user_poeticosAdapter myAdapter_poeticos;
    ArrayList<user_Poeticos> list_poeticos;

    //Variales libros romanticos
    RecyclerView recyclerView_romanticos;
    DatabaseReference dbRef_romanticos;
    user_romanticosAdapter myAdapter_romanticos;
    ArrayList<user_Romanticos> list_romanticos;

    //Variales libros terror
    RecyclerView recyclerView_terror;
    DatabaseReference dbRef_terror;
    user_terrorAdapter myAdapter_terror;
    ArrayList<user_Terror> list_terror;

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

        //SCROLL HORIZONTAL FICCION
        dbRef_ficcion = FirebaseDatabase.getInstance().getReference("books").child("ficcion");

        recyclerView_ficcion = view.findViewById(R.id.ficcionLibros_List);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_ficcion.setHasFixedSize(true);
        recyclerView_ficcion.setItemAnimator(new DefaultItemAnimator());
        recyclerView_ficcion.setLayoutManager(layoutManager3);

        list_ficcion = new ArrayList<>();
        myAdapter_ficcion = new user_ficcionAdapter(getContext(), list_ficcion);
        recyclerView_ficcion.setAdapter(myAdapter_ficcion);

        dbRef_ficcion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Ficcion books = dataSnapshot.getValue(user_Ficcion.class);
                    list_ficcion.add(books);
                }
                myAdapter_ficcion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL INFANTILES
        dbRef_infantiles = FirebaseDatabase.getInstance().getReference("books").child("infantiles");

        recyclerView_infantiles = view.findViewById(R.id.infantilesLibros_List);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_infantiles.setHasFixedSize(true);
        recyclerView_infantiles.setItemAnimator(new DefaultItemAnimator());
        recyclerView_infantiles.setLayoutManager(layoutManager4);

        list_infantiles = new ArrayList<>();
        myAdapter_infantiles = new user_infantilesAdapter(getContext(), list_infantiles);
        recyclerView_infantiles.setAdapter(myAdapter_infantiles);

        dbRef_infantiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Infantiles books = dataSnapshot.getValue(user_Infantiles.class);
                    list_infantiles.add(books);
                }
                myAdapter_ficcion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL LITERATURA
        dbRef_literatura = FirebaseDatabase.getInstance().getReference("books").child("literatura");

        recyclerView_literatura = view.findViewById(R.id.literaturaLibros_List);

        LinearLayoutManager layoutManager5 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_literatura.setHasFixedSize(true);
        recyclerView_literatura.setItemAnimator(new DefaultItemAnimator());
        recyclerView_literatura.setLayoutManager(layoutManager5);

        list_literatura = new ArrayList<>();
        myAdapter_literatura = new user_literaturaAdapter(getContext(), list_literatura);
        recyclerView_literatura.setAdapter(myAdapter_literatura);

        dbRef_literatura.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Literatura books = dataSnapshot.getValue(user_Literatura.class);
                    list_literatura.add(books);
                }
                myAdapter_ficcion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL MISTERIOS
        dbRef_misterio = FirebaseDatabase.getInstance().getReference("books").child("misterios");

        recyclerView_misterio = view.findViewById(R.id.misteriosLibros_List);

        LinearLayoutManager layoutManager6 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_misterio.setHasFixedSize(true);
        recyclerView_misterio.setItemAnimator(new DefaultItemAnimator());
        recyclerView_misterio.setLayoutManager(layoutManager6);

        list_misterio = new ArrayList<>();
        myAdapter_misterio = new user_misterioAdapter(getContext(), list_misterio);
        recyclerView_misterio.setAdapter(myAdapter_misterio);

        dbRef_misterio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Misterio books = dataSnapshot.getValue(user_Misterio.class);
                    list_misterio.add(books);
                }
                myAdapter_misterio.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL POETICOS
        dbRef_poeticos = FirebaseDatabase.getInstance().getReference("books").child("poeticos");

        recyclerView_poeticos = view.findViewById(R.id.poeticosLibros_List);

        LinearLayoutManager layoutManager7 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_poeticos.setHasFixedSize(true);
        recyclerView_poeticos.setItemAnimator(new DefaultItemAnimator());
        recyclerView_poeticos.setLayoutManager(layoutManager7);

        list_poeticos = new ArrayList<>();
        myAdapter_poeticos = new user_poeticosAdapter(getContext(), list_poeticos);
        recyclerView_poeticos.setAdapter(myAdapter_poeticos);

        dbRef_poeticos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Poeticos books = dataSnapshot.getValue(user_Poeticos.class);
                    list_poeticos.add(books);
                }
                myAdapter_misterio.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        //SCROLL HORIZONTAL ROMANTICOS
        dbRef_romanticos = FirebaseDatabase.getInstance().getReference("books").child("romanticos");

        recyclerView_romanticos = view.findViewById(R.id.romanticosLibros_List);

        LinearLayoutManager layoutManager8 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_romanticos.setHasFixedSize(true);
        recyclerView_romanticos.setItemAnimator(new DefaultItemAnimator());
        recyclerView_romanticos.setLayoutManager(layoutManager8);

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

        //SCROLL HORIZONTAL TERROR
        dbRef_terror = FirebaseDatabase.getInstance().getReference("books").child("terror");

        recyclerView_terror = view.findViewById(R.id.terrorLibros_List);

        LinearLayoutManager layoutManager9 = new LinearLayoutManager(
                view.getContext(), LinearLayoutManager.HORIZONTAL, false
        );

        recyclerView_terror.setHasFixedSize(true);
        recyclerView_terror.setItemAnimator(new DefaultItemAnimator());
        recyclerView_terror.setLayoutManager(layoutManager9);

        list_terror = new ArrayList<>();
        myAdapter_terror = new user_terrorAdapter(getContext(), list_terror);
        recyclerView_terror.setAdapter(myAdapter_terror);

        dbRef_terror.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_Terror books = dataSnapshot.getValue(user_Terror.class);
                    list_terror.add(books);
                }
                myAdapter_misterio.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}