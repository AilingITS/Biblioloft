package com.example.biblioloft.fragmentsUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private String mParam1;
    private String mParam2;

    private View vista;
    private ImageView profile_image;
    private EditText profile_mail;
    private TextView profile_user;
    private Button profile_upgrade;

    private StorageReference ImagesRef;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Declaramos la vista del fragment para retornarlo al final
        vista = inflater.inflate(R.layout.fragment_profile, container, false);

        ImagesRef = FirebaseStorage.getInstance().getReference().child("images");

        profile_image = vista.findViewById(R.id.profile_image);
        profile_user = vista.findViewById(R.id.profile_user);
        profile_mail = vista.findViewById(R.id.profile_mail);

        userInfoDisplay(profile_user, profile_mail, profile_image);

        profile_upgrade = (Button) vista.findViewById(R.id.profile_upgrade);
        profile_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        return vista;
    }

    private void userInfoDisplay(final TextView perfil_usuario, final EditText perfil_mail, final ImageView fotoperfil) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getNombre());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("Image").exists()){
                        String image = snapshot.child("Image").getValue().toString();
                        Picasso.get().load(image).into(fotoperfil);
                    }
                    String name = snapshot.child("Nombre").getValue().toString();
                    String correo = snapshot.child("Correo").getValue().toString();
                    perfil_usuario.setText(name);
                    perfil_mail.setText(correo);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //Función que sirve para cambiar de fragmento en fragmento
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body_container,fragment);
        fragmentTransaction.commit();
    }

    //Función cuando el usuario da clic en el boton actualizar datos
    private void ValidateProductData() {
        String usuario = profile_user.getText().toString();
        String mail = profile_mail.getText().toString();

        if(ImageUri == null){ //En caso que el usuario modifico datos pero no su imagen se llama a la sig función solo para actualizar datos
            SaveInfoToDatabasewithoutImage();
        } else if (TextUtils.isEmpty(usuario)){
            Toast.makeText(getActivity(), "Ingrese un usuario", Toast.LENGTH_SHORT).show();
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
        String usuario = profile_user.getText().toString();
        String mail = profile_mail.getText().toString();
        refid.child("users").child(usuario);
        infoMap.put("Nombre", usuario);
        infoMap.put("Correo", mail);

        ref.child(Prevalent.currentOnlineUser.getNombre()).updateChildren(infoMap);

        Toast.makeText(getActivity(), "Perfil Actualizado con exito", Toast.LENGTH_SHORT).show();
    }

    //Guardar información de perfil con imagen de perfil
    private void SaveInfoToDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String, Object> infoMap = new HashMap<>();
        String usuario = profile_user.getText().toString();
        String mail = profile_mail.getText().toString();
        infoMap.put("Nombre", usuario);
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
            profile_image.setImageURI(ImageUri);
        }
    }
}