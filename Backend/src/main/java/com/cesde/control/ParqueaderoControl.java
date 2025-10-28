package com.cesde.control;

import com.cesde.model.parqueadero.Parqueadero;
import com.cesde.model.parqueadero.Ticket;
import com.cesde.model.vehiculos.Vehiculo;

import java.time.LocalDateTime;
import java.util.Scanner;

//Esta clase maneja el parqueadero en general
public class ParqueaderoControl {

    private final Parqueadero parqueadero;
    private final Scanner read;
    private final VehiculoControl vControl;
    private final OcupacionControl oControl;

    public ParqueaderoControl(Parqueadero parqueadero, VehiculoControl vControl, OcupacionControl oControl) {
        this.parqueadero = parqueadero;
        this.vControl = vControl;
        this.oControl = oControl;
        this.read = new Scanner(System.in);
    }
    //Menu parqueadero, solo es visible para administrador que es quien dirige el parqueadero
    public void menuParqueadero() {
        int option;
        do {
            System.out.println("\n*** GESTIÓN DEL PARQUEADERO ***");
            System.out.println("1. Registrar ingreso");
            System.out.println("2. Registrar salida");
            System.out.println("3. Consultar tarifas actuales");
            System.out.println("4. Modificar tarifas");
            System.out.println("5. Ver ocupaciones activas");
            System.out.println("6. Ver historial de ocupaciones");
            System.out.println("7. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            option = read.nextInt();

            switch (option) {
                case 1 -> registrarIngreso();
                case 2 -> registrarSalida();
                case 3 -> mostrarTarifas();
                case 4 -> modificarTarifas();
                case 5 -> listarOcupacionesActivas();
                case 6 -> listarHistorialOcupaciones();
                case 7 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción incorrecta, intente nuevamente.");
            }

        } while (option != 7);
    }

    //Registro de ingreso al parqueadero
    public void registrarIngreso() {
        System.out.println("\n*** REGISTRAR INGRESO ***");

        System.out.print("Placa del vehículo: ");
        String placa = read.next();

        Vehiculo vehiculo = vControl.buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            System.out.println("El vehículo no está registrado en el sistema.");
            return;
        }

        Ticket ticket = parqueadero.entradaParqueadero(vehiculo);
        if (ticket == null) {
            System.out.println("No hay espacios disponibles.");
            return;
        }

        int idOcupacion = oControl.registrarOcupacion(vehiculo.getPlaca(), ticket.getEspacio().getIdEspacio());
        ticket.setId_ocupacion(idOcupacion);

        System.out.println("Ingreso registrado con éxito.");
        System.out.println("Ticket ID: " + ticket.getIdTicket());
        System.out.println("Vehículo: " + vehiculo.getPlaca() + " (" + vehiculo.getTipo() + ")");
        System.out.println("Espacio asignado: " + ticket.getEspacio().getIdEspacio());
        System.out.println("Hora de entrada: " + ticket.getHoraEntrada());
        System.out.println("Tarifa por hora: $" + ticket.getTarifaHora());
    }

    //Registra salida del parqueadero
    private void registrarSalida() {
        System.out.println("\n*** REGISTRAR SALIDA ***");
        System.out.print("Ingrese la placa del vehículo: ");
        String placa = read.next();

        Vehiculo vehiculo = vControl.buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            System.out.println("El vehículo no está registrado en el sistema.");
            return;
        }

        Ticket ticket = parqueadero.buscarTicketActivo(vehiculo);
        if (ticket == null) {
            System.out.println("El vehículo no tiene un ticket activo.");
            return;
        }

        ticket.setHora_salida(LocalDateTime.now());
        double total = ticket.calcularTotal();

        System.out.println("\n*** DETALLE DE PAGO ***");
        System.out.println("Vehículo: " + vehiculo.getPlaca() + " (" + vehiculo.getTipo() + ")");
        System.out.println("Hora de entrada: " + ticket.getHoraEntrada());
        System.out.println("Hora de salida: " + ticket.getHoraSalida());
        System.out.println("Tarifa por hora: $" + ticket.getTarifaHora());
        System.out.println("TOTAL A PAGAR: $" + total);

        System.out.print("\n¿Desea proceder con el pago y salida? (S/N): ");
        String opcion = read.next();

        if (opcion.equalsIgnoreCase("S")) {
            boolean exito = parqueadero.salidaParqueadero(ticket.getIdTicket());
            if (exito) {
                oControl.finalizarOcupacion(ticket.getId_ocupacion(), total);
                System.out.println("Salida registrada con éxito.");
            } else {
                System.out.println("Error: el ticket ya estaba pagado.");
            }
        } else {
            System.out.println("Operación cancelada. El vehículo sigue dentro del parqueadero.");
        }
    }

    //Tarifas
    private void mostrarTarifas() {
        System.out.println("\n*** TARIFAS ACTUALES ***");
        System.out.println("Carro: $" + parqueadero.getTarifaHoraCarro());
        System.out.println("Moto: $" + parqueadero.getTarifaHoraMoto());
    }

    private void modificarTarifas() {
        System.out.println("\n*** MODIFICAR TARIFAS ***");
        System.out.print("Nueva tarifa por hora para carros: ");
        double nuevaTarifaCarro = read.nextDouble();
        System.out.print("Nueva tarifa por hora para motos: ");
        double nuevaTarifaMoto = read.nextDouble();

        parqueadero.setTarifaHoraCarro(nuevaTarifaCarro);
        parqueadero.setTarifaHoraMoto(nuevaTarifaMoto);

        System.out.println("Tarifas actualizadas con éxito.");
    }

    //Consulta de ocupaciones
    private void listarOcupacionesActivas() {
        System.out.println("\n*** OCUPACIONES ACTIVAS ***");
        oControl.listarOcupacionesActivas().forEach(System.out::println);
    }

    private void listarHistorialOcupaciones() {
        System.out.println("\n*** HISTORIAL DE OCUPACIONES ***");
        oControl.listarHistorial().forEach(System.out::println);
    }
}
