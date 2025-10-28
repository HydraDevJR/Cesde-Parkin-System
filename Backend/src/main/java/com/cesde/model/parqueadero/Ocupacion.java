package com.cesde.model.parqueadero;
import java.time.LocalDateTime;

//Esta clase ayuda a guardar la informacion de 1 ocupacion
//  el vehiculo que ocupa 1 espacio, el numero del espacio
//  la hora de entrada y salida, tarifas
//  En general, es para tener un historial de uso en relacion de vehiculo con espacio
public class Ocupacion {

    private int id_ocupacion;
    private String id_vehiculo;
    private int id_espacio;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private String estado; // "ACTIVO" o "FINALIZADO"
    private double tarifa;

    public Ocupacion() {

    }

    public Ocupacion(int id_ocupacion, String id_vehiculo, int id_espacio, LocalDateTime horaEntrada, LocalDateTime horaSalida, String estado, double tarifa) {
        this.id_ocupacion = id_ocupacion;
        this.id_vehiculo = id_vehiculo;
        this.id_espacio = id_espacio;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.estado = estado;
        this.tarifa = tarifa;
    }

    // Getters y Setters
    public int getId_ocupacion() {
        return id_ocupacion;
    }

    public void setId_ocupacion(int id_ocupacion) {
        this.id_ocupacion = id_ocupacion;
    }

    public String getId_vehiculo() {
        return id_vehiculo;
    }

    public void setIdVehiculo(String id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public int getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(int id_espacio) {
        this.id_espacio = id_espacio;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public long getDuracionMinutos() {
        if (horaEntrada != null && horaSalida != null) {
            return java.time.Duration.between(horaEntrada, horaSalida).toMinutes();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Ocupacion{" +
                "idOcupacion=" + id_ocupacion +
                ", idVehiculo=" + id_vehiculo +
                ", idEspacio=" + id_espacio +
                ", horaEntrada=" + horaEntrada +
                ", horaSalida=" + horaSalida +
                ", estado='" + estado + '\'' +
                ", costo=" + tarifa +
                '}';
    }
}