package com.example.biblioloft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biblioloft.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    private EditText txtUser, txtMail, txtPassword, txtConfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        txtUser = findViewById(R.id.usuario_etxt);
        txtMail = findViewById(R.id.correo_etxt);
        txtPassword = findViewById(R.id.password_etxt);
        txtConfPassword = findViewById(R.id.confPassword_etxt);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnConfirmar:
                createuser();
                break;
        }
    }
    public void createuser(){
        //Obtenemos los datos que ingreso el usuario
        String name = txtUser.getText().toString();
        String mail = txtMail.getText().toString();
        String password = txtPassword.getText().toString();
        String confpassword = txtConfPassword.getText().toString();

        //Condiciones para verificar que los datos esten correctos
        if(TextUtils.isEmpty(name)){
            txtUser.setError("Ingrese un nombre de usuario");
            txtUser.requestFocus();
        } else if (TextUtils.isEmpty(mail)){
            txtMail.setError("Ingrese un correo");
            txtMail.requestFocus();
        } else if(TextUtils.isEmpty(password)){
            txtPassword.setError("Ingrese una contraseña");
            txtPassword.requestFocus();
        } else if(TextUtils.isEmpty(confpassword)){
            txtConfPassword.setError("Ingrese la confrimación de su contraseña");
            txtConfPassword.requestFocus();
        } else if(!password.equals(confpassword)) {
            txtConfPassword.setError("Las contraseñas no coinciden");
            txtPassword.requestFocus();
        } else if (password.length() <= 5) {
            txtPassword.setError("La contraseña debe tener mas de 6 caracteres");
            txtPassword.requestFocus();
        } else {
            ValidateMail(mail, password, name);
        }


    }

    private void ValidateMail(String mail, String password, String name ) {

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!(snapshot.child("users").child(name).exists())){

                    HashMap<String, Object> infoMap = new HashMap<>();
                    infoMap.put("Nombre", name);
                    infoMap.put("Correo", mail);
                    infoMap.put("Contraseña", password);

                    dbRef.child("users").child(name).updateChildren(infoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(RegisterActivity.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                   startActivity(intent);
                               }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Este nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                    txtUser.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }
}

