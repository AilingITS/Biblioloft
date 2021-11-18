package com.example.biblioloft.fragmentsUser.view_book;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblioloft.LoginActivity;
import com.example.biblioloft.Prevalent.Prevalent;
import com.example.biblioloft.R;
import com.example.biblioloft.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ViewBookFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private String bookID, image, titulo, pages, autor, description, libroID, tipoLibro;

    private TextView viewBook_title, viewBook_author, viewBook_pages, viewBook_description;
    private ImageView img_book_info;
    private Button btn_empezar_leer;

    private DatabaseReference dbRef, dbRef_libroLeyendo;

    public ViewBookFragment(String bookID) {
        this.bookID = bookID;
    }

    public ViewBookFragment() {
        // Required empty public constructor
    }

    public static ViewBookFragment newInstance(String param1, String param2) {
        ViewBookFragment fragment = new ViewBookFragment();
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
        view = inflater.inflate(R.layout.fragment_view_book, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("books").child("registro").child(bookID);

        img_book_info = (ImageView) view.findViewById(R.id.img_book_info);
        viewBook_title = (TextView) view.findViewById(R.id.viewBook_title);
        viewBook_author = (TextView) view.findViewById(R.id.viewBook_author);
        viewBook_pages = (TextView) view.findViewById(R.id.viewBook_pages);
        viewBook_description = (TextView) view.findViewById(R.id.viewBook_description);
        btn_empezar_leer = (Button) view.findViewById(R.id.btn_empezar_leer);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("imageLibro").exists()) {
                        image = snapshot.child("imageLibro").getValue().toString();
                        Picasso.get().load(image).into(img_book_info);
                    }

                    titulo = snapshot.child("nombreLibro").getValue().toString();
                    viewBook_title.setText(titulo);

                    autor = snapshot.child("autorLibro").getValue().toString();
                    viewBook_author.setText(autor);

                    pages = snapshot.child("paginasLibro").getValue().toString();
                    viewBook_pages.setText(pages);

                    description = snapshot.child("descripcionLibro").getValue().toString();
                    viewBook_description.setText(description);

                    libroID = snapshot.child("libroID").getValue().toString();
                    tipoLibro = snapshot.child("tipoLibro").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        btn_empezar_leer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRef_libroLeyendo = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getNombre()).child("libroleyendo");

                dbRef_libroLeyendo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!(snapshot.exists())){

                            HashMap<String, Object> infoMap = new HashMap<>();
                            infoMap.put("libroID", libroID);
                            infoMap.put("tipoLibro", tipoLibro);
                            infoMap.put("nombreLibro", titulo);
                            infoMap.put("autorLibro", autor);
                            infoMap.put("descripcionLibro", description);
                            infoMap.put("paginasLibro", pages);
                            infoMap.put("imageLibro", image);

                            dbRef_libroLeyendo.updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Agregado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Para agregar otro libro, ocupas terminar primero el libro que estas leyendo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                });
            }
        });

        return view;
    }
}