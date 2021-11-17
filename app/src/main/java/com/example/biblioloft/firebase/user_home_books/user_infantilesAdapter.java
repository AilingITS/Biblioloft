package com.example.biblioloft.firebase.user_home_books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsUser.view_book.ViewBookFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class user_infantilesAdapter extends RecyclerView.Adapter<user_infantilesAdapter.booksHolder>{

    Context context;
    ArrayList<user_Infantiles> list;

    DatabaseReference dbRef;

    public user_infantilesAdapter(Context context, ArrayList<user_Infantiles> list) {
        this.context = context;
        this.list = list;
    }

    public user_infantilesAdapter.booksHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_book_user,parent,false);
        return new user_infantilesAdapter.booksHolder(v);
    }

    public void onBindViewHolder(@NonNull @NotNull user_infantilesAdapter.booksHolder holder, int position) {

        dbRef = FirebaseDatabase.getInstance().getReference().child("books").child("infantiles");

        user_Infantiles books = list.get(position);
        Picasso.get().load(books.getImageLibro()).into(holder.imageLibro);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment myFragment = new ViewBookFragment(books.getLibroID());
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, myFragment).addToBackStack(null).commit();
                /*AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(true);

                final CharSequence[] opciones = new CharSequence[2];
                opciones[0] = "Editar";
                opciones[1] = "Borrar";
                dialogo1.setTitle("Seleccione una opción");
                dialogo1.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(opciones[which] == "Editar"){
                            AppCompatActivity activity = (AppCompatActivity) context;
                            //Fragment myFragment = new EditarDesayunoFragment(foods.getP_ID());
                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.body_container, myFragment).addToBackStack(null).commit();
                        }
                        if(opciones[which] == "Borrar"){
                            list.clear();

                            dbRef.child(books.getLibroID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    Toast.makeText(context, "Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogo1.show();
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class booksHolder extends RecyclerView.ViewHolder{

        ImageView imageLibro;

        public booksHolder(View itemView){
            super(itemView);

            imageLibro = itemView.findViewById(R.id.item_imagen);
        }
    }
}