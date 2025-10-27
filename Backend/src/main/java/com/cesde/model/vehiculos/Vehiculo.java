package com.cesde.model.vehiculos;

public class Vehiculo {

    //Atributos principales de la clase Vehiculo
    private String placa;
    private String tipo;
    private int documento;

    //Constructor para crear un vehiculo con su placa, tipo y el id del propietario
    public Vehiculo(String placa, String tipo, int id_usuario) {
        this.placa = placa;
        this.tipo = tipo;
        this.documento = id_usuario;
    }

    //Getters y Setters de los atributos
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId_usuario() {
        return documento;
    }

    public void setId_usuario(int id_usuario) {
        this.documento = id_usuario;
    }

    //Metodo toString para mostrar informacion del vehiculo en formato legible
    @Override
    public String toString() {
        return "Placa: " + placa + " | Tipo: " + tipo + " | Documento del propietario: " + documento;
    }
}
