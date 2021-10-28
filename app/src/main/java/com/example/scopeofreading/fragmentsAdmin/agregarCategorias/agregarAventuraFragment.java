package com.example.scopeofreading.fragmentsAdmin.agregarCategorias;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scopeofreading.R;
import com.example.scopeofreading.fragmentsAdmin.HomeAdminFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class agregarAventuraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View vista;
    private String adminID;
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
        adminID = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(adminID).child("aventura");
        ImagesRef = FirebaseStorage.getInstance().getReference().child("aventura");

        nombreLibro = vista.findViewById(R.id.nombreLibro);
        descripcionLibro = vista.findViewById(R.id.descripcionLibro);
        paginasLibro = vista.findViewById(R.id.paginasLibro);
        btn_agregar_libro = vista.findViewById(R.id.btn_agregar_libro);

        btn_añadirLibro = (Button) vista.findViewById(R.id.btn_añadirLibro);
        btn_añadirLibro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){createLibro(); }
        });
        btn_agregar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OpenGallery(); }
        });

        return vista;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body_container,fragment);
        fragmentTransaction.commit();
    }
    public void createLibro(){
        adminID = mAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat currentDate = new SimpleDateFormat(" dd MM, yyyy");
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());
        libroID = saveCurrentDate + saveCurrentTime;

        StorageReference fileRef = ImagesRef.child(adminID).child(libroID + ".jpg");
        final UploadTask uploadTask = fileRef.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                String message = e.toString();
                Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = fileRef.getDownloadUrl().toString();
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            SaveInfoToDatabase(); //Función para actualizar datos e imagen de perfil
                        }
                    }
                });
            }
        });
    }

    //Guardar información de perfil con imagen de perfil
    private void SaveInfoToDatabase() {
        //Obtenemos los datos que ingreso el usuario
        String nombre = nombreLibro.getText().toString();
        String descripcion = descripcionLibro.getText().toString();
        String paginas = paginasLibro.getText().toString();

        //Condiciones para verificar que los datos esten correctos
        if (TextUtils.isEmpty(nombre)){
            nombreLibro.setError("Ingrese el nombre de la comida");
            nombreLibro.requestFocus();
        } else if(TextUtils.isEmpty(descripcion)){
            descripcionLibro.setError("Ingrese los ingredientes de la comida");
            descripcionLibro.requestFocus();
        } else if(TextUtils.isEmpty(paginas)){
            paginasLibro.setError("Ingrese las calorias de la comida");
            paginasLibro.requestFocus();
        }else {
            Calendar calendar = Calendar.getInstance();
            //SimpleDateFormat currentDate = new SimpleDateFormat(" dd MM, yyyy");
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calendar.getTime());
            libroID = saveCurrentDate + saveCurrentTime;

            //Map para registrar a un usuario con sus datos
            Map<String, Object> comida = new HashMap<>();
            comida.put("p_ID", libroID);
            comida.put("f_tipo", "Cena");
            comida.put("f_nombrecomida", nombre);
            comida.put("f_ingredientes", descripcion);
            comida.put("f_calorias", paginas);
            comida.put("f_image", downloadImageUrl);

            dbRef.child(libroID).updateChildren(comida).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), R.string.stringCambiosGuardadosCorrectamente, Toast.LENGTH_SHORT).show();
                        replaceFragment(new HomeAdminFragment());
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Función para abrir la galeria cuando da clic en la imagen
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            btn_agregar_libro.setImageURI(ImageUri);
        }
    }
}