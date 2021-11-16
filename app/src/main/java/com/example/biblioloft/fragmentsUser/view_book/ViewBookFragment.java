package com.example.biblioloft.fragmentsUser.view_book;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biblioloft.R;
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

public class ViewBookFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private String bookID;
    private TextView viewBook_title, viewBook_author, viewBook_pages, viewBook_description;
    private ImageView img_book_info;

    private DatabaseReference dbRef;

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

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("imageLibro").exists()) {
                        String image = snapshot.child("imageLibro").getValue().toString();
                        Picasso.get().load(image).into(img_book_info);
                    }

                    String titulo = snapshot.child("nombreLibro").getValue().toString();
                    viewBook_title.setText(titulo);

                    String autor = snapshot.child("autorLibro").getValue().toString();
                    viewBook_author.setText(autor);

                    String pages = snapshot.child("paginasLibro").getValue().toString();
                    viewBook_pages.setText(pages);

                    String description = snapshot.child("descripcionLibro").getValue().toString();
                    viewBook_description.setText(description);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        return view;
    }
}