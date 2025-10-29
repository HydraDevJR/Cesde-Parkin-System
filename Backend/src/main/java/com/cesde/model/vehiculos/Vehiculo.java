package com.cesde.model.vehiculos;

import java.time.LocalDateTime;

public class Vehiculo {

    private String placa;
    private String tipo;
    private String marca;
    private String modelo;
    private String color;
    private int documento; // id del propietario
    private LocalDateTime fechaRegistro;

    // Constructor completo
    public Vehiculo(String placa, String tipo, String marca, String modelo, String color, int documento, LocalDateTime fechaRegistro) {
        this.placa = placa;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.documento = documento;
        this.fechaRegistro = fechaRegistro;
    }

    // ================== Getters y Setters ==================

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // ================== toString ==================

    @Override
    public String toString() {
        return "Placa: " + placa +
                " | Tipo: " + tipo +
                " | Marca: " + marca +
                " | Modelo: " + modelo +
                " | Color: " + color +
                " | Documento propietario: " + documento +
                " | Fecha registro: " + fechaRegistro;
    }
}
