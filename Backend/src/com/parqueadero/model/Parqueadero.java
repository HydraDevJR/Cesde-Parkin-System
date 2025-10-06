package com.parqueadero.model;
import com.vehiculos.model.Vehiculo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//En esta clase definimos 1 parqueadero y
public class Parqueadero {

    private String nombre;
    private String direccion;
    private double tarifaHoraCarro;
    private double tarifaHoraMoto;
    private List<Espacio> espacios;
    private List<Ticket> tickets;

    public Parqueadero(int cantidadEspacios){
        this.nombre = "CESDE Parkin System";
        this.direccion = "Carrera 42 #48-20";
        this.tarifaHoraCarro = 3000;
        this.tarifaHoraMoto = 1500;
        this.espacios = new ArrayList<>();
        this.tickets = new ArrayList<>();

        for (int i= 1; i <= cantidadEspacios; i++){
            espacios.add(new Espacio(i));
        }
    }

    public Ticket buscarTicketActivo(Vehiculo vehiculo) {
        for (Ticket ticket : tickets) {
            if (ticket.getVehiculo().equals(vehiculo) && !ticket.isPagado()) {
                return ticket;
            }
        }
        return null;
    }

    public Ticket entradaParqueadero(Vehiculo vehiculo){
        //Valida el tipo de vehiculo para asignarle una tarifa
        double tarifa = (vehiculo.getTipo().equalsIgnoreCase("Carro")) ? tarifaHoraCarro : tarifaHoraMoto;

        //Verifica que espacio está disponible para ocuparlo
        for (Espacio espacio : espacios) {
            if (!espacio.isOcupado()) {
                espacio.ocuparEspacio(vehiculo);
                Ticket ticket = new Ticket(tickets.size() + 1, vehiculo, espacio, tarifa);
                tickets.add(ticket);
                return ticket;
            }
        }
        System.out.println("No hay espacios disponibles.");
        return null;
    }

    public boolean salidaParqueadero(int idTicket) {
        for (Ticket ticket : tickets) {
            if (ticket.getIdTicket() == idTicket) {
                if (!ticket.isPagado()) {
                    ticket.pagarTicket(); // Marca el ticket como pagado
                    liberarEspacio(ticket.getVehiculo()); // Libera el espacio ocupado
                    return true;
                }
                return false; // Ya estaba pagado
            }
        }
        return false; // Ticket no encontrado
    }

    private void liberarEspacio(Vehiculo vehiculo) {
        for (Espacio espacio : espacios) {
            if (espacio.getVehiculoOcupando() != null && espacio.getVehiculoOcupando().equals(vehiculo)) {
                espacio.liberarEspacio();
                break;
            }
        }
    }

    // Métodos para modificar tarifas
    public void setTarifaHoraCarro(double tarifaHoraCarro) {
        this.tarifaHoraCarro = tarifaHoraCarro;
    }

    public void setTarifaHoraMoto(double tarifaHoraMoto) {
        this.tarifaHoraMoto = tarifaHoraMoto;
    }

    public double getTarifaHoraCarro() {
        return tarifaHoraCarro;
    }

    public double getTarifaHoraMoto() {
        return tarifaHoraMoto;
    }

    public List<Espacio> getEspacios() {
        return espacios;
    }
}
