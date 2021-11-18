package com.example.biblioloft.fragmentsUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biblioloft.Prevalent.Prevalent;
import com.example.biblioloft.R;
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

import java.util.HashMap;


public class EditarPerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private ImageView profile_img;
    private TextView editar_nombreCompleto;
    private EditText editar_paciente_correo;
    private Button editar_btn_editarPacientes;

    private StorageReference ImagesRef;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;
    private static final int RESULT_OK = -1;

    public EditarPerfilFragment() {
        // Required empty public constructor
    }

    public static EditarPerfilFragment newInstance(String param1, String param2) {
        EditarPerfilFragment fragment = new EditarPerfilFragment();
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
        view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        ImagesRef = FirebaseStorage.getInstance().getReference().child("images");

        profile_img = view.findViewById(R.id.profile_img);
        editar_paciente_correo = view.findViewById(R.id.editar_paciente_correo);
        editar_btn_editarPacientes = view.findViewById(R.id.editar_btn_editarPacientes);
        editar_nombreCompleto = view.findViewById(R.id.editar_nombreCompleto);

        userInfoDisplay(editar_nombreCompleto, editar_paciente_correo, profile_img);

        editar_btn_editarPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        return view;
    }

    private void userInfoDisplay(final TextView editar_nombreCompleto, final EditText editar_paciente_correo, final ImageView profile_img) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getNombre());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("Image").exists()){
                        String image = snapshot.child("Image").getValue().toString();
                        Picasso.get().load(image).into(profile_img);
                    }
                    String name = snapshot.child("Nombre").getValue().toString();
                    String correo = snapshot.child("Correo").getValue().toString();
                    editar_nombreCompleto.setText(name);
                    editar_paciente_correo.setText(correo);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //Función cuando el usuario da clic en el boton actualizar datos
    private void ValidateProductData() {
        String mail = editar_paciente_correo.getText().toString();

        if(ImageUri == null){ //En caso que el usuario modifico datos pero no su imagen se llama a la sig función solo para actualizar datos
            SaveInfoToDatabasewithoutImage();
        } else if (TextUtils.isEmpty(mail)){
            Toast.makeText(getActivity(), "Ingrese un correo", Toast.LENGTH_SHORT).show();
        } else { //Si el usuario si agrego una imagen de perfil entra en este else

            StorageReference fileRef = ImagesRef.child(Prevalent.currentOnlineUser.getNombre() + ".jpg");
            final UploadTask uploadTask = fileRef.putFile(ImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
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
    }

    // Actualiza los datos menos la foto de perfil
    private void SaveInfoToDatabasewithoutImage() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference refid = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> infoMap = new HashMap<>();
        String usuario = editar_nombreCompleto.getText().toString();
        String mail = editar_paciente_correo.getText().toString();
        refid.child("users").child(usuario);
        infoMap.put("Correo", mail);

        ref.child(Prevalent.currentOnlineUser.getNombre()).updateChildren(infoMap);

        Toast.makeText(getActivity(), "Perfil Actualizado con exito", Toast.LENGTH_SHORT).show();
    }

    //Guardar información de perfil con imagen de perfil
    private void SaveInfoToDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String, Object> infoMap = new HashMap<>();
        String mail = editar_paciente_correo.getText().toString();
        infoMap.put("Correo", mail);
        infoMap.put("Image", downloadImageUrl);

        ref.child(Prevalent.currentOnlineUser.getNombre()).updateChildren(infoMap);
        Toast.makeText(getActivity(), "Perfil Actualizado con exito", Toast.LENGTH_SHORT).show();

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
            profile_img.setImageURI(ImageUri);
        }
    }
}