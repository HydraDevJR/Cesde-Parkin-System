package com.usuarios.model;

import com.vehiculos.model.Vehiculo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Usuario {

    //Atributos principales de la clase Usuario
    private int documento;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    private LocalDateTime fecha_creacion;
    private final ArrayList<Vehiculo> vehiculos;

    //Constructor para crear un usuario con fecha de creación automática
    public Usuario(int id_Usuario, String nombre, String correo, String contrasena, String rol) {
        this.documento = id_Usuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fecha_creacion = LocalDateTime.now(); // asigna automáticamente la fecha y hora actual
        this.vehiculos = new ArrayList<>();
    }

    //Getters y Setters de los atributos
    public int getId_usuario() {
        return documento;
    }

    public void setId_usuario(int id_usuario) {
        this.documento = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    //Metodo para obtener el total de vehiculos que tiene registrado este usuario
    public int getVehiculos() {
        int contador = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getId_usuario() == this.documento) {
                contador++;
            }
        }
        return contador;
    }

    //Metodo para agregar un vehiculo al usuario
    //  solo si el vehiculo pertenece a este usuario y no existe previamente
    public void agregarVehiculo(Vehiculo v) {
        if (v.getId_usuario() == this.documento && buscarVehiculoPorPlaca(v.getPlaca()) == null) {
            vehiculos.add(v);
        }
    }

    //Metodo para eliminar un vehiculo del usuario por su placa
    //  retorna true si se eliminó correctamente, false en caso contrario
    public boolean borrarVehiculo(String placa) {
        Vehiculo v = buscarVehiculoPorPlaca(placa);
        if (v != null && v.getId_usuario() == this.documento) {
            vehiculos.remove(v);
            return true;
        }
        return false;
    }

    //Metodo para buscar un vehiculo del usuario por su placa
    //  retorna el vehiculo si lo encuentra, de lo contrario null
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equals(placa)) {
                return v;
            }
        }
        return null;
    }

    //Metodo toString para mostrar informacion del usuario en formato legible
    @Override
    public String toString() {
        return "Usuario #" + documento + " | Nombre: " + nombre + " | Rol: " + rol + " | Fecha de creacion: " + fecha_creacion;
    }
}


