package com.cesde.model.parqueadero;
import com.cesde.model.vehiculos.Vehiculo;

import java.time.Duration;
import java.time.LocalDateTime;


//Aqui definimos nuestro ticket
public class Ticket {

    private int id_recibo;
    private int id_ocupacion;
    private Vehiculo placa;
    private Espacio numero;
    private LocalDateTime hora_entrada;
    private LocalDateTime hora_salida;
    private double tarifaHora;
    private double totalPagar;
    private boolean pagado;


    public Ticket(int id_recibo, Vehiculo placa, Espacio numero, double tarifaHora){
        this.id_recibo = id_recibo;
        this.placa = placa;
        this.numero = numero;
        this.hora_entrada = LocalDateTime.now();
        this.tarifaHora = tarifaHora;
        this.pagado = false;
        this.id_ocupacion = 0;
    }

    public double calcularTotal() {
        if (this.hora_salida == null) {
            this.hora_salida = LocalDateTime.now();
        }

        Duration duracion = Duration.between(hora_entrada, hora_salida);
        long minutos = duracion.toMinutes();
        double horas = Math.ceil(minutos / 60.0);

        this.totalPagar = horas * tarifaHora;
        return totalPagar;
    }

    public void pagarTicket(){
        this.pagado = true;
    }

    public int getIdTicket(){
        return id_recibo;
    }

    public Vehiculo getVehiculo(){
        return placa;
    }

    public Espacio getEspacio(){
        return numero;
    }

    public LocalDateTime getHoraEntrada(){
        return hora_entrada;
    }

    public LocalDateTime getHoraSalida(){
        return hora_salida;
    }

    public double getTotalPagar(){
        return totalPagar;
    }
    public double getTarifaHora(){
        return tarifaHora;
    }

    public int getId_ocupacion() {
        return id_ocupacion;
    }

    public void setId_ocupacion(int id_ocupacion) {
        this.id_ocupacion = id_ocupacion;
    }

    public boolean isPagado(){
        return pagado;
    }

    public double getTotal() { return totalPagar; }

    public void setHora_salida(LocalDateTime hora_salida) {
        this.hora_salida = hora_salida;
    }
}
