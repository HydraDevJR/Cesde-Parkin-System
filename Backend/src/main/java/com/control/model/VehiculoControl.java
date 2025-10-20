package com.control.model;

import com.usuarios.model.Estudiante;
import com.usuarios.model.Usuario;
import com.vehiculos.model.Vehiculo;

import java.util.ArrayList;
import java.util.Scanner;

public class VehiculoControl {

    final ArrayList<Vehiculo> vehiculos;
    private final Scanner read;
    private final UsuarioControl uControl; // Para validar usuarios

    public VehiculoControl(UsuarioControl uControl) {
        this.vehiculos = new ArrayList<>();
        this.read = new Scanner(System.in);
        this.uControl = uControl;
    }

    //Menu para estudiantes
    public void menuVehiculo(Estudiante est) {
        int opcion;
        do {
            System.out.println("\n*** Mis vehiculos - " + est.getNombre() + " ***");
            System.out.println("1. Registrar vehículo");
            System.out.println("2. Modificar vehículo");
            System.out.println("3. Eliminar vehículo");
            System.out.println("4. Listar mis vehículos");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = read.nextInt();

            switch (opcion) {
                case 1 -> registrarVehiculo(est);
                case 2 -> modificarVehiculo(est);
                case 3 -> eliminarVehiculo(est);
                case 4 -> listarVehiculos(est);
                case 5 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    //Menu para administradores
    public void menuVehiculoAdmin() {
        int opcion;
        do {
            System.out.println("\n*** Menú de Vehículos - ADMINISTRADOR ***");
            System.out.println("1. Registrar vehículo");
            System.out.println("2. Modificar vehículo");
            System.out.println("3. Eliminar vehículo");
            System.out.println("4. Listar todos los vehículos");
            System.out.println("5. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = read.nextInt();

            switch (opcion) {
                case 1 -> registrarVehiculoAdmin();
                case 2 -> modificarVehiculoAdmin();
                case 3 -> eliminarVehiculoAdmin();
                case 4 -> listarVehiculosAdmin();
                case 5 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    //    Los siguientes metodos van dirigidos a estudiantes
    //Registrar vehiculos
    private void registrarVehiculo(Estudiante est) {
        String placa = leerDatosVehiculo();
        if (placa == null) return;

        guardarVehiculo(placa, est);
    }

    //Para modificar vehiculos
    // Actualiza el tipo de vehiculo asociado a la placa ingresada
    private void modificarVehiculo(Estudiante est) {
        System.out.print("Ingrese la placa del vehículo a modificar: ");
        String placa = read.next();
        Vehiculo vehiculo = buscarVehiculoEstudiante(est, placa);

        if (vehiculo == null) {
            System.out.println("Vehículo no encontrado.");
            return;
        }

        System.out.print("Nuevo tipo de vehículo: ");
        vehiculo.setTipo(read.next());
        System.out.println("Vehículo modificado con éxito.");
    }

    // Elimina un vehículo pero valida que esté asociado al estudiante actual
    private void eliminarVehiculo(Estudiante est) {
        System.out.print("Ingrese la placa del vehículo a eliminar: ");
        String placa = read.next();

        // Busca el vehículo en la lista del estudiante
        Vehiculo vehiculo = buscarVehiculoEstudiante(est, placa);

        if (vehiculo == null) {
            System.out.println("Error: no tienes registrado un vehículo con esa placa.");
            return;
        }

        // Eliminar de la lista del estudiante
        est.getListaVehiculos().remove(vehiculo);

        // Eliminar de la lista global
        vehiculos.remove(vehiculo);

        System.out.println("Vehículo eliminado correctamente.");
    }

    //Lista de vehiculos para estudiantes
    private void listarVehiculos(Estudiante est) {
        if (est.getVehiculos() == 0) {
            System.out.println("No tienes vehículos registrados.");
        } else {
            System.out.println("\n--- Mis Vehículos ---");
            for (Vehiculo v : est.getListaVehiculos()) {
                System.out.println(v);
            }
        }
    }

    // Dejando de lado los metodos para estudiantes, los siguientes son par administradores

    //Registrar vehiculo para administrador
    private void registrarVehiculoAdmin() {
        Estudiante est = obtenerPropietario(); // Paso 1: validar estudiante
        if (est != null) {
            String placa = leerDatosVehiculo(); // Paso 2: pedir placa
            if (placa == null) return;
            guardarVehiculo(placa, est);       // Paso 3: guardar
        } else {
            System.out.println("Error: el usuario no existe o no es un estudiante. Primero debe registrarlo.");
        }
    }

    //Modificar vehiculos
    // Actualiza el tipo de vehiculo asociado a la placa ingresada
    private void modificarVehiculoAdmin() {
        System.out.print("Ingrese la placa del vehículo a modificar: ");
        String placa = read.next();
        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("Vehículo no encontrado.");
            return;
        }

        System.out.print("Nuevo tipo de vehículo: ");
        vehiculo.setTipo(read.next());
        System.out.println("Vehículo modificado con éxito.");
    }

    // Elimina el vehiculo tanto de la lista global como de la lista del estudiante propietario
    private void eliminarVehiculoAdmin() {
        System.out.print("Ingrese la placa del vehículo a eliminar: ");
        String placa = read.next();

        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("Vehículo no encontrado.");
            return;
        }

        // Eliminar de la lista global
        vehiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));

        // Eliminar de la lista del estudiante
        Usuario propietario = uControl.buscarUsuarioPorId(vehiculo.getId_usuario());
        if (propietario instanceof Estudiante est) {
            est.borrarVehiculo(vehiculo.getPlaca());
        }

        System.out.println("Vehículo eliminado correctamente.");
    }

    //Lista todos los vehiculos
    // Recorre y muestra en pantalla la lista de vehiculos disponibles
    private void listarVehiculosAdmin() {
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehículos registrados.");
        } else {
            System.out.println("\n--- Lista de Vehículos ---");
            for (Vehiculo v : vehiculos) {
                System.out.println(v);
            }
        }
    }

    // Los siguientes metodos son para busqueda

    // Metodo de búsqueda global en la lista de vehiculos
    //  retorna el vehiculo si existe, de lo contrario null
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    //Igual que el metodo anterior pero para estudiantes
    private Vehiculo buscarVehiculoEstudiante(Estudiante est, String placa) {
        for (Vehiculo v : est.getListaVehiculos()) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    //Los siguientes metodos están enfocados en el registro de vehiculos

    //Metodo para recibir los datos de un vehiculo (placa) con validacion
    //  para que el campo no sea nulo ni este vacio
    private String leerDatosVehiculo() {
        System.out.print("Ingrese la placa del vehículo: ");
        String placa = read.next();

        if (placa == null || placa.isEmpty()) {
            System.out.println("Placa no válida. Intente nuevamente.");
            return null;
        }
        return placa;
    }

    public String solicitarPlaca() {
        return leerDatosVehiculo();
    }

    //Metodo para validar existencia del usuario
    private Estudiante obtenerPropietario() {
        System.out.print("Ingrese el ID del propietario (estudiante): ");
        int idProp = read.nextInt();
        Usuario propietario = uControl.buscarUsuarioPorId(idProp);

        if (propietario instanceof Estudiante est) {
            return est;
        }
        return null;
    }

    //Metodo para guardar los datos del vehiculo previamente validados
    //  y tambien define el tipo de vehiculo acorde al numero de placa
    //  si termina en letra es Moto, si no es Carro
    private void guardarVehiculo(String placa, Estudiante est) {
        char ultimoChar = placa.charAt(placa.length() - 1);
        String tipo = Character.isLetter(ultimoChar) ? "Moto" : "Carro";

        Vehiculo v = new Vehiculo(placa, tipo, est.getId_usuario());
        vehiculos.add(v);
        est.agregarVehiculo(v);

        System.out.println("Vehículo registrado correctamente para " + est.getNombre());
    }
}
