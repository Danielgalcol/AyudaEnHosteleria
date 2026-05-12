package com.example.app;

public class Perfil {

    private String nombre;
    private String apellidos;
    private String correo;
    private String rol;
    private String telef;
    private boolean activo;
    private String direccion;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Perfil(String nombre, String apell, String email, String rol, String tel, boolean act, String direc){
        this.nombre=nombre;
        apellidos = apell;
        correo = email;
        this.rol = rol;
        telef = tel;
        activo = act;
        direccion = direc;
    }
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelef() {
        return telef;
    }

    public void setTelef(String telef) {
        this.telef = telef;
    }
}
