package com.example.biblioloft.fragmentsAdmin.editarLibros;

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

import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsAdmin.registroLibros.registroAventuraFragment;
import com.example.biblioloft.fragmentsAdmin.registroLibros.registroCientificosFragment;
import com.example.biblioloft.fragmentsAdmin.registroLibros.registroPoeticosFragment;
import com.example.biblioloft.fragmentsAdmin.registroLibros.registroTerrorFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarTerrorFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private String libroID;

    private DatabaseReference dbRef;
    private StorageReference ImagesRef;

    Button editar_btn_guardarCambios;
    private EditText editar_nombreLibro, editar_descripcionLibro, editar_paginasLibro, editar_autorLibro;

    private ImageView editar_img;
    private static final int GalleryPick = 1;
    private static final int RESULT_OK = -1;
    private Uri ImageUri;
    private String downloadImageUrl;

    public EditarTerrorFragment(String libroID) {
        this.libroID = libroID;
    }

    public EditarTerrorFragment() {
        // Required empty public constructor
    }

    public static EditarTerrorFragment newInstance(String param1, String param2) {
        EditarTerrorFragment fragment = new EditarTerrorFragment();
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
        view = inflater.inflate(R.layout.fragment_editar_terror, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("books");
        ImagesRef = FirebaseStorage.getInstance().getReference().child("terror");

        editar_nombreLibro = view.findViewById(R.id.editar_nombreLibro);
        editar_autorLibro = view.findViewById(R.id.editar_autorLibro);
        editar_descripcionLibro = view.findViewById(R.id.editar_descripcionLibro);
        editar_paginasLibro = view.findViewById(R.id.editar_paginasLibro);
        editar_img = view.findViewById(R.id.editar_img);

        dbRef.child("terror").child(libroID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("imageLibro").exists()) {
                        String image = snapshot.child("imageLibro").getValue().toString();
                        Picasso.get().load(image).into(editar_img);
                    }

                    String nombre = snapshot.child("nombreLibro").getValue().toString();
                    String autor = snapshot.child("autorLibro").getValue().toString();
                    String descripcion = snapshot.child("descripcionLibro").getValue().toString();
                    String paginas = snapshot.child("paginasLibro").getValue().toString();

                    editar_nombreLibro.setText(nombre);
                    editar_autorLibro.setText(autor);
                    editar_descripcionLibro.setText(descripcion);
                    editar_paginasLibro.setText(paginas);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        editar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        editar_btn_guardarCambios = (Button) view.findViewById(R.id.editar_btn_guardarCambios);
        editar_btn_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        return view;
    }

    //Función cuando el usuario da clic en el boton actualizar datos
    private void ValidateProductData() {
        String nombre = editar_nombreLibro.getText().toString();
        String autor = editar_autorLibro.getText().toString();
        String descripcion = editar_descripcionLibro.getText().toString();
        String paginas = editar_paginasLibro.getText().toString();

        if (ImageUri == null) { //En caso que el usuario modifico datos pero no su imagen se llama a la sig función solo para actualizar datos
            SaveInfoToDatabasewithoutImage();
        } else if (TextUtils.isEmpty(nombre)) {
            editar_nombreLibro.setError("Ingrese el nombre del libro");
            editar_nombreLibro.requestFocus();
        } else if (TextUtils.isEmpty(autor)) {
            editar_nombreLibro.setError("Ingrese el nombre del autor");
            editar_nombreLibro.requestFocus();
        } else if (TextUtils.isEmpty(descripcion)) {
            editar_descripcionLibro.setError("Ingrese la descripción del libro");
            editar_descripcionLibro.requestFocus();
        } else if (TextUtils.isEmpty(paginas)) {
            editar_paginasLibro.setError("Ingrese las páginas del libro");
            editar_paginasLibro.requestFocus();
        } else { //Si el usuario si agrego una imagen de perfil entra en este else

            Calendar calendar = Calendar.getInstance();
            //SimpleDateFormat currentDate = new SimpleDateFormat(" dd MM, yyyy");
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            String saveCurrentTime = currentTime.format(calendar.getTime());
            String imgID = saveCurrentDate + saveCurrentTime;

            StorageReference fileRef = ImagesRef.child("terror").child(imgID + ".jpg");
            final UploadTask uploadTask = fileRef.putFile(ImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(getActivity(), getString(R.string.stringError) + message, Toast.LENGTH_SHORT).show();
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
        HashMap<String, Object> infoMap = new HashMap<>();
        String nombre = editar_nombreLibro.getText().toString();
        String autor = editar_autorLibro.getText().toString();
        String descripcion = editar_descripcionLibro.getText().toString();
        String paginas = editar_paginasLibro.getText().toString();
        infoMap.put("nombreLibro", nombre);
        infoMap.put("autorLibro", autor);
        infoMap.put("descripcionLibro", descripcion);
        infoMap.put("paginasLibro", paginas);

        dbRef.child("terror").child(libroID).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dbRef.child("registro").child(libroID).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), R.string.stringCambiosGuardadosCorrectamente, Toast.LENGTH_SHORT).show();
                                replaceFragment(new registroPoeticosFragment());
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SaveInfoToDatabase () {
        //Obtenemos los datos que ingreso el usuario

        String nombre = editar_nombreLibro.getText().toString();
        String autor = editar_autorLibro.getText().toString();
        String descripcion = editar_descripcionLibro.getText().toString();
        String paginas = editar_paginasLibro.getText().toString();

        //Condiciones para verificar que los datos esten correctos
        if (TextUtils.isEmpty(nombre)) {
            editar_nombreLibro.setError("Ingrese el nombre del libro");
            editar_nombreLibro.requestFocus();
        } else if (TextUtils.isEmpty(autor)) {
            editar_descripcionLibro.setError("Ingrese el nombre del autor");
            editar_descripcionLibro.requestFocus();
        } else if (TextUtils.isEmpty(descripcion)) {
            editar_descripcionLibro.setError("Ingrese la descripción del libro");
            editar_descripcionLibro.requestFocus();
        } else if (TextUtils.isEmpty(paginas)) {
            editar_paginasLibro.setError("Ingrese las páginas");
            editar_paginasLibro.requestFocus();
        } else {
            //Map para registrar a un usuario con sus datos
            Map<String, Object> desayunoMap = new HashMap<>();
            desayunoMap.put("nombreLibro", nombre);
            desayunoMap.put("autorLibro", autor);
            desayunoMap.put("descripcionLibro", descripcion);
            desayunoMap.put("paginasLibro", paginas);
            desayunoMap.put("imageLibro", downloadImageUrl);

            dbRef.child("terror").child(libroID).updateChildren(desayunoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dbRef.child("registro").child(libroID).updateChildren(desayunoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.stringCambiosGuardadosCorrectamente, Toast.LENGTH_SHORT).show();
                                    replaceFragment(new registroPoeticosFragment());
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(), R.string.stringError + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body_container, fragment);
        fragmentTransaction.commit();
    }

    //Función para abrir la galeria cuando da clic en la imagen
    private void OpenGallery () {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode,
                                   @Nullable @org.jetbrains.annotations.Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            editar_img.setImageURI(ImageUri);
        }
    }
}