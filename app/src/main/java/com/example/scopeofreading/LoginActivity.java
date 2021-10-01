package com.example.scopeofreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    //Variables
    private FirebaseAuth mAuth;
    private EditText lo_mail, lo_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        lo_mail = findViewById(R.id.lo_mail);
        lo_password = findViewById(R.id.lo_password);
    }

    public void onClick(View v){
        switch (v.getId()) {

            case R.id.btniniciarSesion:
                Toast.makeText(this, "Verificando datos", Toast.LENGTH_SHORT).show();
                userLogin();
                break;

            case R.id.btnRegistrarse:
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
                Toast.makeText(this, "Cargando...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void userLogin(){
        String mail = lo_mail.getText().toString();
        String password = lo_password.getText().toString();

        if(TextUtils.isEmpty(mail)){
            lo_mail.setError("Ingrese su nombre de usuario");
            lo_mail.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            lo_password.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Bienvenid@ a Biblioloft", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Log.w("TAG", "Error:", task.getException());
                    }
                }
            });
        }
    }
}