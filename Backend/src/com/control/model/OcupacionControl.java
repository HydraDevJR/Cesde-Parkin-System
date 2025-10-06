package com.control.model;
import com.parqueadero.model.Ocupacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Esta clase controla la logica del historial de usos (Ocupacion) por espacios
public class OcupacionControl {
    private List<Ocupacion> ocupaciones;
    private int contadorId;

    public OcupacionControl() {
        this.ocupaciones = new ArrayList<>();
        this.contadorId = 1;
    }
    //Registra una ocupacion teniendo como parametro la placa del vehiculo y
    // el numero del espacio ocupado
    public int registrarOcupacion(String id_vehiculo, int id_espacio) {
        Ocupacion ocupacion = new Ocupacion();
        ocupacion.setId_ocupacion(contadorId++);
        ocupacion.setIdVehiculo(id_vehiculo);
        ocupacion.setId_espacio(id_espacio);
        ocupacion.setHoraEntrada(LocalDateTime.now());
        ocupacion.setEstado("ACTIVO");

        ocupaciones.add(ocupacion);
        System.out.println("Ocupación registrada: " + ocupacion);

        return ocupacion.getId_ocupacion();
    }

    public void finalizarOcupacion(int id_ocupacion, double tarifa) {
        for (Ocupacion ocupacion : ocupaciones) {
            if (ocupacion.getId_ocupacion() == id_ocupacion && "ACTIVO".equals(ocupacion.getEstado())) {
                ocupacion.setHoraSalida(LocalDateTime.now());
                ocupacion.setEstado("FINALIZADO");
                ocupacion.setTarifa(tarifa);
                System.out.println("Ocupación finalizada: " + ocupacion);
                return;
            }
        }
        System.out.println("No se encontró una ocupación activa con id: " + id_ocupacion);
    }
    //Ayuda a obtener la informacion de una ocupacion mediante la placa del vehiculo
    public Ocupacion obtenerOcupacionPorVehiculo(String idVehiculo) {
        for (Ocupacion o : ocupaciones) {
            if (o.getId_vehiculo().equals(idVehiculo) && "ACTIVO".equals(o.getEstado())) {
                return o;
            }
        }
        return null;
    }
    //Ayuda a obtener la informacion de una ocupacion mediante el numero de ocupacion guardado
    public Ocupacion obtenerOcupacionPorId(int idOcupacion) {
        for (Ocupacion o : ocupaciones) {
            if (o.getId_ocupacion() == idOcupacion) return o;
        }
        return null;
    }
    //Crea una lista de todas las ocupaciones activas en el momento
    public List<Ocupacion> listarOcupacionesActivas() {
        List<Ocupacion> activas = new ArrayList<>();
        for (Ocupacion ocupacion : ocupaciones) {
            if ("ACTIVO".equals(ocupacion.getEstado())) {
                activas.add(ocupacion);
            }
        }
        return activas;
    }
    //Historial de todas las ocupaciones registradas
    public List<Ocupacion> listarHistorial() {
        List<Ocupacion> historial = new ArrayList<>();
        for (Ocupacion ocupacion : ocupaciones) {
            if ("FINALIZADO".equals(ocupacion.getEstado())) {
                historial.add(ocupacion);
            }
        }
        return historial;
    }
}