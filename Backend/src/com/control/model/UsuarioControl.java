package com.control.model;
import com.usuarios.model.*;
import java.util.ArrayList;
import java.util.Scanner;

//Esta clase es para controlar lo relacionado con los usuarios
public class UsuarioControl {
    ArrayList<Usuario> usuarios = new ArrayList<>();
    private final Scanner read = new Scanner(System.in);

    //En este constructor se crea un Administrador y Estudiante por defecto
    public UsuarioControl(){
        Administrador adminDefault = new Administrador(1, "adminDefault", "admin@cesde.com", "1234","General");
        Estudiante estDefault = new Estudiante(2, "estDefault", "est@cesde.com", "1234","General");
        usuarios.add(adminDefault);
        usuarios.add(estDefault);
    }

    //Identifica el tipo de rol y muestra el menu correspondiente
    public void menuUsuario(Usuario usuario) {
        if (usuario instanceof Administrador admin) {
            menuAdministrador(admin);
        } else if (usuario instanceof Estudiante est) {
            menuEstudiante(est);
        } else {
            System.out.println("Rol no válido.");
        }
    }

    //Menu para administrador
    private void menuAdministrador(Administrador admin) {
        int opcion;
        do {
            System.out.println("\n*** MENÚ ADMINISTRADOR ***");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Modificar usuario");
            System.out.println("3. Eliminar usuario");
            System.out.println("4. Listar usuarios");
            System.out.println("5. Volver");
            System.out.print("Seleccione: ");
            opcion = read.nextInt();

            switch (opcion) {
                case 1 -> registrarUsuario(admin);
                case 2 -> modificarUsuario();
                case 3 -> eliminarUsuario();
                case 4 -> listarUsuarios();
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    //Menu para estudiante
    private void menuEstudiante(Estudiante estudiante) {
        int opcion;
        VehiculoControl vControl = new VehiculoControl(this); // Delegar gestión de vehículos

        do {
            System.out.println("\n*** MENÚ ESTUDIANTE ***");
            System.out.println("1. Editar perfil");
            System.out.println("2. Mis vehículos");
            System.out.println("3. Volver");
            System.out.print("Seleccione: ");
            opcion = Integer.parseInt(read.nextLine());

            switch (opcion) {
                case 1 -> editarPerfil(estudiante);
                case 2 -> vControl.menuVehiculo(estudiante); // Se delega la lógica de vehículos
                case 3 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    //Registro de usuario para administrador
    void registrarUsuario(Administrador admin) {
        System.out.println("\n*** REGISTRAR USUARIO (ADMIN) ***");
        System.out.print("Documento: ");
        int id = Integer.parseInt(read.nextLine());

        System.out.print("Nombre: ");
        String nombre = read.nextLine();

        System.out.print("Correo: ");
        String correo = read.next();

        System.out.print("Contraseña: ");
        String contrasena = read.next();

        System.out.print("Rol (ESTUDIANTE/ADMIN): ");
        String rol = read.next().toUpperCase();

        Usuario nuevo;
        if (rol.equalsIgnoreCase("ESTUDIANTE")) {
            System.out.print("Carrera: ");
            String carrera = read.next();
            nuevo = new Estudiante(id, nombre, correo, contrasena, carrera);
        } else {
            System.out.print("Área: ");
            String area = read.next();
            nuevo = new Administrador(id, nombre, correo, contrasena, area);
        }

        usuarios.add(nuevo);
        System.out.println("Usuario registrado correctamente por el administrador " + admin.getNombre());
    }

    //Modificar usuario
    private void modificarUsuario() {
        System.out.println("\n*** MODIFICAR USUARIO ***");
        System.out.print("Ingrese el documento: ");
        int id = read.nextInt(); read.nextLine();

        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario != null) {
            System.out.print("Nuevo nombre (" + usuario.getNombre() + "): ");
            usuario.setNombre(read.nextLine());

            System.out.print("Nuevo correo (" + usuario.getCorreo() + "): ");
            usuario.setCorreo(read.next());

            System.out.print("Nueva contraseña: ");
            usuario.setContrasena(read.next());

            System.out.println("Usuario modificado correctamente.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    //Eliminar usuario
    private void eliminarUsuario() {
        System.out.println("\n*** ELIMINAR USUARIO ***");
        System.out.print("Ingrese el documento: ");
        int id = read.nextInt();

        Usuario usuario = buscarUsuarioPorId(id);
        if (usuario != null) {
            usuarios.remove(usuario);
            System.out.println("Usuario eliminado.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }

    //Listar usuarios
    private void listarUsuarios() {
        System.out.println("\n--- LISTA DE USUARIOS ---");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (Usuario u : usuarios) {
                System.out.println(u + " | Vehículos: " + u.getVehiculos());
            }
        }
    }

    //Editar perfil de estudiante
    private void editarPerfil(Estudiante e) {
        System.out.print("Nuevo nombre (" + e.getNombre() + "): ");
        e.setNombre(read.next());
        System.out.print("Nuevo correo (" + e.getCorreo() + "): ");
        e.setCorreo(read.next());
        System.out.print("Nueva contraseña: ");
        e.setContrasena(read.next());
        System.out.println("Perfil actualizado.");
    }

    //Login
    public Usuario login(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equals(correo) && u.getContrasena().equals(contrasena)) {
                System.out.println("Login exitoso: " + u.getNombre() + " (" + u.getRol() + ")");
                return u;
            }
        }
        System.out.println("Credenciales inválidas.");
        return null;
    }

    //Buscar usuario por ID
    public Usuario buscarUsuarioPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId_usuario() == id) {
                return u;
            }
        }
        return null;
    }
}
