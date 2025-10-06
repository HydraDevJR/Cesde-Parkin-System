package com.parqueadero.model;
import com.vehiculos.model.Vehiculo;

//Esta clase ayuda a tener la informacion de 1 espacio
public class Espacio {

    private int numero;
    private String tipo;
    private boolean estado;
    private Vehiculo vehiculoOcupando;

    public Espacio(int numero){
        this.numero = numero;
        this.tipo = null;
        this.estado = false;
        this.vehiculoOcupando = null;
    }

    public int getIdEspacio() {
        return numero;
    }

    public boolean isOcupado() {
        return estado;
    }

    public Vehiculo getVehiculoOcupando() {
        return vehiculoOcupando;
    }

    private String tipoPorPlaca(String placa){
        char ultimoChar = placa.charAt(placa.length() - 1);
        if (Character.isLetter(ultimoChar)) {
            return "Moto";
        } else {
            return "Carro";
        }
    }

    public void ocuparEspacio(Vehiculo vehiculo){
        this.vehiculoOcupando = vehiculo;
        this.estado = true;
        this.tipo = tipoPorPlaca(vehiculo.getPlaca());
    }

    public void liberarEspacio() {
        this.vehiculoOcupando = null;
        this.estado = false;
        this.tipo = null;
    }

    @Override
    public String toString() {
        return "Espacio #" + numero + " | Estado: " + (estado ? "Ocupado por " + vehiculoOcupando.getPlaca() : "Libre");
    }

}
