package com.cesde.model.usuarios;

//Esta clase hereda de Usuario
public class Administrador extends Usuario {

    private String area; // Atributo propio de Administrador

    public Administrador(int id_usuario, String nombre, String correo, String contrasena,String estado, String area) {
        super(id_usuario, nombre, correo, contrasena, "ADMIN", estado);
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
