package com.cesde.control;

import com.cesde.dao.VehiculoDAO;
import com.cesde.model.usuarios.Estudiante;
import com.cesde.model.usuarios.Usuario;
import com.cesde.model.vehiculos.Vehiculo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehiculoControl {

    final ArrayList<Vehiculo> vehiculos;
    private final Scanner read;
    private final UsuarioControl uControl; // Para validar usuarios
    private final VehiculoDAO dao;         // Nuevo: DAO para persistencia

    public VehiculoControl(UsuarioControl uControl) {
        this.vehiculos = new ArrayList<>();
        this.read = new Scanner(System.in);
        this.uControl = uControl;
        this.dao = new VehiculoDAO();
        cargarVehiculosDesdeDB(); // Inicializa la lista global con lo que hay en la DB
    }

    // ====================== Cargar vehículos desde BD ======================
    private void cargarVehiculosDesdeDB() {
        List<Vehiculo> lista = dao.listarVehiculos();
        if (lista != null) {
            vehiculos.addAll(lista);
        }
    }
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

    // ====================== Métodos CRUD ======================

    // Actualizamos guardarVehiculo para persistir en BD y aceptar los nuevos campos
    private void guardarVehiculo(String placa, Estudiante est) {
        char ultimoChar = placa.charAt(placa.length() - 1);
        String tipo = Character.isLetter(ultimoChar) ? "Moto" : "Carro";

        System.out.print("Ingrese marca del vehículo: ");
        String marca = read.next();

        System.out.print("Ingrese modelo del vehículo: ");
        String modelo = read.next();

        System.out.print("Ingrese color del vehículo: ");
        String color = read.next();

        LocalDateTime fechaRegistro = LocalDateTime.now();

        Vehiculo v = new Vehiculo(placa, tipo, marca, modelo, color, est.getDocumento(), fechaRegistro);

        // Guardar en DAO (base de datos)
        boolean ok = dao.insertarVehiculo(v);
        if (ok) {
            vehiculos.add(v);      // lista global
            est.agregarVehiculo(v); // lista estudiante
            System.out.println("Vehículo registrado correctamente para " + est.getNombre());
        } else {
            System.out.println("Error al registrar vehículo en la base de datos.");
        }
    }
    // Registrar vehículo para estudiante usando DAO
    public void registrarVehiculo(Estudiante est) {
        System.out.print("Ingrese placa del vehículo: ");
        String placa = read.next();
        if (placa == null || placa.isEmpty()) {
            System.out.println("Placa no válida.");
            return;
        }

        // Llamamos a guardarVehiculo, que persiste en BD
        guardarVehiculo(placa, est);
    }

    // Listar vehículos del estudiante usando DAO
    public void listarVehiculos(Estudiante est) {
        // Obtenemos desde DAO solo los vehículos de ese estudiante
        List<Vehiculo> lista = dao.listarPorUsuario(est.getDocumento());
        if (lista.isEmpty()) {
            System.out.println("No tienes vehículos registrados.");
            return;
        }

        System.out.println("\n--- Mis Vehículos ---");
        for (Vehiculo v : lista) {
            System.out.println(v);
        }
    }
    // Registrar vehículo para administrador usando DAO
    public void registrarVehiculoAdmin() {
        Estudiante est = obtenerPropietario();
        if (est == null) {
            System.out.println("Error: usuario no registrado o no es estudiante.");
            return;
        }

        System.out.print("Ingrese placa del vehículo: ");
        String placa = read.next();
        if (placa == null || placa.isEmpty()) {
            System.out.println("Placa no válida.");
            return;
        }

        guardarVehiculo(placa, est);
    }

    // Listar todos los vehículos usando DAO
    public void listarVehiculosAdmin() {
        List<Vehiculo> lista = dao.listarVehiculos(); // Todos los vehículos en BD
        if (lista.isEmpty()) {
            System.out.println("No hay vehículos registrados.");
            return;
        }

        System.out.println("\n--- Lista de Vehículos ---");
        for (Vehiculo v : lista) {
            System.out.println(v);
        }
    }

    // Modificar vehículo para estudiantes
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

        System.out.print("Nueva marca: ");
        vehiculo.setMarca(read.next());

        System.out.print("Nuevo modelo: ");
        vehiculo.setModelo(read.next());

        System.out.print("Nuevo color: ");
        vehiculo.setColor(read.next());

        // Actualizar en DAO
        boolean ok = dao.actualizarVehiculo(vehiculo, vehiculo.getPlaca());
        if (ok) {
            System.out.println("Vehículo modificado con éxito.");
        } else {
            System.out.println("Error al actualizar vehículo en la base de datos.");
        }
    }

    // Modificar vehículo para admin
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

        System.out.print("Nueva marca: ");
        vehiculo.setMarca(read.next());

        System.out.print("Nuevo modelo: ");
        vehiculo.setModelo(read.next());

        System.out.print("Nuevo color: ");
        vehiculo.setColor(read.next());

        boolean ok = dao.actualizarVehiculo(vehiculo, vehiculo.getPlaca());
        if (ok) {
            System.out.println("Vehículo modificado con éxito.");
        } else {
            System.out.println("Error al actualizar vehículo en la base de datos.");
        }
    }

    // Eliminar vehículo para estudiantes
    private void eliminarVehiculo(Estudiante est) {
        System.out.print("Ingrese la placa del vehículo a eliminar: ");
        String placa = read.next();

        Vehiculo vehiculo = buscarVehiculoEstudiante(est, placa);

        if (vehiculo == null) {
            System.out.println("Error: no tienes registrado un vehículo con esa placa.");
            return;
        }

        boolean ok = dao.eliminarVehiculo(placa);
        if (ok) {
            est.getListaVehiculos().remove(vehiculo);
            vehiculos.remove(vehiculo);
            System.out.println("Vehículo eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar vehículo de la base de datos.");
        }
    }

    // Eliminar vehículo para admin
    private void eliminarVehiculoAdmin() {
        System.out.print("Ingrese la placa del vehículo a eliminar: ");
        String placa = read.next();

        Vehiculo vehiculo = buscarVehiculoPorPlaca(placa);
        if (vehiculo == null) {
            System.out.println("Vehículo no encontrado.");
            return;
        }

        boolean ok = dao.eliminarVehiculo(placa);
        if (ok) {
            // eliminar de lista global
            vehiculos.remove(vehiculo);
            Usuario propietario = uControl.buscarUsuarioPorId(vehiculo.getDocumento());
            if (propietario instanceof Estudiante est) {
                est.borrarVehiculo(placa);
            }
            System.out.println("Vehículo eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar vehículo de la base de datos.");
        }
    }

    // ====================== Métodos de búsqueda ======================
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    private Vehiculo buscarVehiculoEstudiante(Estudiante est, String placa) {
        for (Vehiculo v : est.getListaVehiculos()) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    // ====================== Métodos de entrada ======================
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

    private Estudiante obtenerPropietario() {
        System.out.print("Ingrese el ID del propietario (estudiante): ");
        int idProp = read.nextInt();
        Usuario propietario = uControl.buscarUsuarioPorId(idProp);

        if (propietario instanceof Estudiante est) {
            return est;
        }
        return null;
    }
}
