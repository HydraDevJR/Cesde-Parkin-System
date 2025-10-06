package com.usuarios.model;

import com.vehiculos.model.Vehiculo;

import java.util.ArrayList;

//Estudiante hereda de Usuario
public class Estudiante extends Usuario {

    private String carrera;
    private final ArrayList<Vehiculo> listaVehiculos; // <-- lista de vehículos del estudiante

    public Estudiante(int id_usuario, String nombre, String correo, String contrasena, String carrera) {
        super(id_usuario, nombre, correo, contrasena, "ESTUDIANTE");
        this.carrera = carrera;
        this.listaVehiculos = new ArrayList<>(); // inicializar lista
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    // Métodos para vehículos
    public ArrayList<Vehiculo> getListaVehiculos() {
        return listaVehiculos;
    }

    public int getVehiculos() {
        return listaVehiculos.size();
    }

    public void agregarVehiculo(Vehiculo v) {
        listaVehiculos.add(v);
    }
}
