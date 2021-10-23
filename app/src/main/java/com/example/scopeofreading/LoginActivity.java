package com.example.scopeofreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.scopeofreading.Prevalent.Prevalent;
import com.example.scopeofreading.firebase.Admin;
import com.example.scopeofreading.firebase.Users;
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

import io.paperdb.Paper;    

public class LoginActivity extends AppCompatActivity {

    //Variables
    private EditText lo_nombre, lo_password;
    private Spinner spinner;
    private String parentdbName = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        lo_nombre = findViewById(R.id.lo_nombre);
        lo_password = findViewById(R.id.lo_password);

        spinner = (Spinner) findViewById(R.id.spinner);
        String [] opciones = {"Usuario", "Administrador"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(adapter);

        // Verificar si existe una sesion, para iniciar sesion automatico
        String UserNameKey = Paper.book().read(Prevalent.UserNameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserNameKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserNameKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserNameKey, UserPasswordKey);
            }
        }
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
        String nombre = lo_nombre.getText().toString();
        String password = lo_password.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            lo_nombre.setError("Ingrese su nombre de usuario");
            lo_nombre.requestFocus();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
            lo_password.requestFocus();
        } else {
           AllowAccessToAccount(nombre, password);
        }
    }

    private void AllowAccessToAccount(String nombre, String password) {
        String seleccion = spinner.getSelectedItem().toString();

        //Guardamos los datos del usuario cuando inicia sesión
        Paper.book().write(Prevalent.UserNameKey, nombre);
        Paper.book().write(Prevalent.UserPasswordKey, password);

        // checar que la cuenta que escriba sea de tipo usuario
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(seleccion.equals("Usuario")) {
                    if (snapshot.child(parentdbName).child(nombre).exists()) {
                        Users usersData = snapshot.child(parentdbName).child(nombre).getValue(Users.class);
                        if (usersData.getNombre().equals(nombre)) {
                            if (usersData.getContraseña().equals(password)) {
                                //La siguiente linea guarda al usuario en el prevalent para mostrar los datos en perfil
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                lo_password.requestFocus();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "El usuario es incorrecto", Toast.LENGTH_SHORT).show();
                            lo_nombre.requestFocus();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No existe una cuenta con este usuario o el tipo de usuario es incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                if(seleccion.equals("Administrador")){
                    if(snapshot.child("admin").child(nombre).exists()){
                        Admin adminData = snapshot.child("admin").child(nombre).getValue(Admin.class);
                        if(adminData.getNombre().equals(nombre)){
                            if(adminData.getContraseña().equals(password)){
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                lo_password.requestFocus();
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "El usuario es incorrecto", Toast.LENGTH_SHORT).show();
                            lo_nombre.requestFocus();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "No existe una cuenta con este usuario o el tipo de usuario es incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }

    private void AllowAccess(final String nombre, final String password) {
        final DatabaseReference dbRef;
        dbRef = FirebaseDatabase.getInstance().getReference();

        // checar que la cuenta que escriba sea de tipo usuario
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(nombre).exists()) {
                    Users usersData = snapshot.child("users").child(nombre).getValue(Users.class);
                    if (usersData.getNombre().equals(nombre)) {
                        if (usersData.getContraseña().equals(password)) {
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                            lo_password.requestFocus();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "El nombre de usuario es incorrecto", Toast.LENGTH_SHORT).show();
                        lo_nombre.requestFocus();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No existe una cuenta con este usuario o el tipo de usuario es incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
        });
    }
}