package com.example.biblioloft.firebase;

public class Admin {
    private String Contraseña, Nombre;

    public Admin(){

    }

    public Admin(String contraseña, String nombre) {
        Contraseña = contraseña;
        Nombre = nombre;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
