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


public class agregarRomanticosFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View vista;

    private EditText nombreLibro, descripcionLibro, paginasLibro;
    private Button btn_añadirLibro;
    private ImageView btn_agregar_img;
    private String libroID, saveCurrentDate, saveCurrentTime;

    private DatabaseReference adminRef;
    private StorageReference ImagesRef;

    private static final int GalleryPick = 1;
    private static final int RESULT_OK = -1;
    private Uri ImageUri;
    private String downloadImageUrl;

    public agregarRomanticosFragment() {
        // Required empty public constructor
    }

    public static agregarRomanticosFragment newInstance(String param1, String param2) {
        agregarRomanticosFragment fragment = new agregarRomanticosFragment();
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
        vista = inflater.inflate(R.layout.fragment_agregar_cientificos, container, false);

        adminRef = FirebaseDatabase.getInstance().getReference().child("books");
        ImagesRef = FirebaseStorage.getInstance().getReference().child("romanticos");

        nombreLibro = vista.findViewById(R.id.nombreLibro);
        descripcionLibro = vista.findViewById(R.id.descripcionLibro);
        paginasLibro = vista.findViewById(R.id.paginasLibro);
        btn_agregar_img = vista.findViewById(R.id.btn_agregar_img);

        btn_añadirLibro = (Button) vista.findViewById(R.id.btn_añadirLibro);
        btn_añadirLibro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ ValidateProductData(); }
        });
        btn_agregar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OpenGallery(); }
        });


        return vista;
    }
    //Función cuando el usuario da clic en el boton actualizar datos
    private void ValidateProductData() {
        //Obtenemos los datos que ingreso el usuario
        String nombre = nombreLibro.getText().toString();
        String descripcion = descripcionLibro.getText().toString();
        String paginas = paginasLibro.getText().toString();

        //Condiciones para verificar que los datos esten correctos
        if (ImageUri == null) { //En caso que el usuario modifico datos pero no su imagen se llama a la sig función solo para actualizar datos
            SaveInfoToDatabasewithoutImage();
        } else if (TextUtils.isEmpty(nombre)){
            nombreLibro.setError("Ingrese el nombre del libro");
            nombreLibro.requestFocus();
        } else if(TextUtils.isEmpty(descripcion)){
            descripcionLibro.setError("Ingrese la descripción del libro");
            descripcionLibro.requestFocus();
        } else if(TextUtils.isEmpty(paginas)){
            paginasLibro.setError("Ingrese las páginas del libro");
            paginasLibro.requestFocus();
        }else {
            Calendar calendar = Calendar.getInstance();
            //SimpleDateFormat currentDate = new SimpleDateFormat(" dd MM, yyyy");
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calendar.getTime());
            libroID = saveCurrentDate + saveCurrentTime;

            StorageReference fileRef = ImagesRef.child(libroID + ".jpg");
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
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = fileRef.getDownloadUrl().toString();
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                SaveInfoToDatabase(); //Función para actualizar datos e imagen de perfil
                            }
                        }
                    });
                }
            });
        }
    }

    // Actualiza los datos menos la foto de perfil
    private void SaveInfoToDatabasewithoutImage () {
        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat currentDate = new SimpleDateFormat(" dd MM, yyyy");
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());
        libroID = saveCurrentDate + saveCurrentTime;

        String nombre = nombreLibro.getText().toString();
        String descripcion = descripcionLibro.getText().toString();
        String paginas = paginasLibro.getText().toString();

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("libroID", libroID);
        infoMap.put("tipoLibro", "Romanticos");
        infoMap.put("nombreLibro", nombre);
        infoMap.put("descripcionLibro", descripcion);
        infoMap.put("paginasLibro", paginas);

        adminRef.child("romanticos").child(libroID).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    //Guardar información de perfil con imagen de perfil
    private void SaveInfoToDatabase() {
        //Obtenemos los datos que ingreso el usuario
        String nombre = nombreLibro.getText().toString();
        String descripcion = descripcionLibro.getText().toString();
        String paginas = paginasLibro.getText().toString();

        //Condiciones para verificar que los datos esten correctos
        if (TextUtils.isEmpty(nombre)){
            nombreLibro.setError("Ingrese el nombre del libro");
            nombreLibro.requestFocus();
        } else if(TextUtils.isEmpty(descripcion)){
            descripcionLibro.setError("Ingrese la descripción del libro");
            descripcionLibro.requestFocus();
        } else if(TextUtils.isEmpty(paginas)){
            paginasLibro.setError("Ingrese las páginas del libro");
            paginasLibro.requestFocus();
        }else {

            //Map para registrar a un usuario con sus datos
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("libroID", libroID);
            infoMap.put("tipoLibro", "Romanticos");
            infoMap.put("nombreLibro", nombre);
            infoMap.put("descripcionLibro", descripcion);
            infoMap.put("paginasLibro", paginas);
            infoMap.put("imageLibro", downloadImageUrl);

            adminRef.child("romanticos").child(libroID).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            btn_agregar_img.setImageURI(ImageUri);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body_container,fragment);
        fragmentTransaction.commit();
    }
}