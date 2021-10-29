package com.example.biblioloft.firebase;

public class Users {
    private String Contraseña, Correo, Nombre;

    public Users(){

    }

    public Users(String contraseña, String correo, String nombre) {
        Contraseña = contraseña;
        Correo = correo;
        Nombre = nombre;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
