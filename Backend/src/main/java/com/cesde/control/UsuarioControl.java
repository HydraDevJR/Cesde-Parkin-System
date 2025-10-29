package com.cesde.control;

import com.cesde.dao.UsuarioDAO;
import com.cesde.model.usuarios.Administrador;
import com.cesde.model.usuarios.Estudiante;
import com.cesde.model.usuarios.Usuario;

import java.util.List;
import java.util.Scanner;

/**
 * Controlador de usuarios con integración a la base de datos.
 * Reemplaza el uso de ArrayList por consultas reales mediante UsuarioDAO.
 */
public class UsuarioControl {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Scanner read = new Scanner(System.in);

    // Constructor opcional (ya no carga usuaria por defecto, ahora vienen desde la BD)
    public UsuarioControl() {
        System.out.println("UsuarioControl inicializado (modo BD).");
    }

    // Identifica el tipo de rol y muestra el menú correspondiente
    public void menuUsuario(Usuario usuario) {
        if (usuario instanceof Administrador admin) {
            menuAdministrador(admin);
        } else if (usuario instanceof Estudiante est) {
            menuEstudiante(est);
        } else {
            System.out.println("Rol no válido.");
        }
    }

    // --- MENÚ ADMINISTRADOR ---
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
            opcion = Integer.parseInt(read.nextLine());

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

    // --- MENÚ ESTUDIANTE ---
    private void menuEstudiante(Estudiante estudiante) {
        int opcion;
        VehiculoControl vControl = new VehiculoControl(this);

        do {
            System.out.println("\n*** MENÚ ESTUDIANTE ***");
            System.out.println("1. Editar perfil");
            System.out.println("2. Mis vehículos");
            System.out.println("3. Volver");
            System.out.print("Seleccione: ");
            opcion = Integer.parseInt(read.nextLine());

            switch (opcion) {
                case 1 -> editarPerfil(estudiante);
                case 2 -> vControl.menuVehiculo(estudiante);
                case 3 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    // --- REGISTRAR USUARIO (ADMIN) ---
    private void registrarUsuario(Administrador admin) {
        try {
            System.out.println("\n*** REGISTRAR USUARIO (ADMIN) ***");
            System.out.print("Documento: ");
            int id = Integer.parseInt(read.nextLine());

            System.out.print("Nombre: ");
            String nombre = read.nextLine();

            System.out.print("Correo: ");
            String correo = read.nextLine();

            System.out.print("Contraseña: ");
            String contrasena = read.nextLine();

            System.out.print("Rol (ESTUDIANTE/ADMIN): ");
            String rol = read.nextLine().toUpperCase();

            System.out.print("Estado (ACTIVO/INACTIVO): ");
            String estado = read.nextLine().toUpperCase();

            Usuario nuevo;
            if (rol.equals("ESTUDIANTE")) {
                System.out.print("Carrera: ");
                String carrera = read.nextLine();
                nuevo = new Estudiante(id, nombre, correo, contrasena, estado, carrera);
            } else {
                System.out.print("Área: ");
                String area = read.nextLine();
                nuevo = new Administrador(id, nombre, correo, contrasena, estado, area);
            }

            boolean insertado = usuarioDAO.insertarUsuario(nuevo);
            if (insertado)
                System.out.println("Usuario registrado correctamente por " + admin.getNombre());
            else
                System.out.println("Error al registrar usuario.");
        } catch (Exception e) {
            System.out.println("Error registrarUsuario: " + e.getMessage());
        }
    }

    // --- MODIFICAR USUARIO ---
    private void modificarUsuario() {
        try {
            System.out.println("\n*** MODIFICAR USUARIO ***");
            System.out.print("Ingrese el documento: ");
            int id = Integer.parseInt(read.nextLine());

            Usuario usuario = usuarioDAO.buscarPorDocumento(id);
            if (usuario == null) {
                System.out.println("Usuario no encontrado.");
                return;
            }

            System.out.print("Nuevo nombre (" + usuario.getNombre() + "): ");
            String nombre = read.nextLine();
            if (!nombre.isBlank()) usuario.setNombre(nombre);

            System.out.print("Nuevo correo (" + usuario.getCorreo() + "): ");
            String correo = read.nextLine();
            if (!correo.isBlank()) usuario.setCorreo(correo);

            System.out.print("Nueva contraseña: ");
            String contrasena = read.nextLine();
            if (!contrasena.isBlank()) usuario.setContrasena(contrasena);

            System.out.print("Nuevo rol (" + usuario.getRol() + "): ");
            String rol = read.nextLine();
            if (!rol.isBlank()) usuario.setRol(rol);

            System.out.print("Nuevo estado (" + usuario.getEstado() + "): ");
            String estado = read.nextLine();
            if (!estado.isBlank()) usuario.setEstado(estado);

            boolean actualizado = usuarioDAO.actualizarUsuario(usuario, usuario.getDocumento());
            if (actualizado)
                System.out.println("Usuario actualizado correctamente.");
            else
                System.out.println("No se pudo actualizar el usuario.");

        } catch (Exception e) {
            System.out.println("Error modificarUsuario: " + e.getMessage());
        }
    }

    // --- ELIMINAR USUARIO ---
    private void eliminarUsuario() {
        try {
            System.out.println("\n*** ELIMINAR USUARIO ***");
            System.out.print("Ingrese el documento: ");
            int id = Integer.parseInt(read.nextLine());

            boolean eliminado = usuarioDAO.eliminarUsuario(id);
            if (eliminado)
                System.out.println("Usuario eliminado correctamente.");
            else
                System.out.println("No se encontró el usuario a eliminar.");
        } catch (Exception e) {
            System.out.println("Error eliminarUsuario: " + e.getMessage());
        }
    }

    // --- LISTAR USUARIOS ---
    private void listarUsuarios() {
        System.out.println("\n--- LISTA DE USUARIOS ---");
        List<Usuario> lista = usuarioDAO.listarUsuarios();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        for (Usuario u : lista) {
            System.out.println("Documento: " + u.getDocumento() +
                    " | Nombre: " + u.getNombre() +
                    " | Correo: " + u.getCorreo() +
                    " | Rol: " + u.getRol() +
                    " | Estado: " + u.getEstado());
        }
    }

    // --- EDITAR PERFIL ESTUDIANTE ---
    private void editarPerfil(Estudiante e) {
        try {
            System.out.print("Nuevo nombre (" + e.getNombre() + "): ");
            String nombre = read.nextLine();
            if (!nombre.isBlank()) e.setNombre(nombre);

            System.out.print("Nuevo correo (" + e.getCorreo() + "): ");
            String correo = read.nextLine();
            if (!correo.isBlank()) e.setCorreo(correo);

            System.out.print("Nueva contraseña: ");
            String contrasena = read.nextLine();
            if (!contrasena.isBlank()) e.setContrasena(contrasena);

            boolean actualizado = usuarioDAO.actualizarUsuario(e, e.getDocumento());
            if (actualizado)
                System.out.println("Perfil actualizado correctamente.");
            else
                System.out.println("No se pudo actualizar el perfil.");
        } catch (Exception ex) {
            System.out.println("Error editarPerfil: " + ex.getMessage());
        }
    }

    // --- LOGIN ---
    public Usuario login(String correo, String contrasena) {
        Usuario u = usuarioDAO.validarLogin(correo, contrasena);
        if (u != null) {
            System.out.println("Login exitoso: " + u.getNombre() + " (" + u.getRol() + ")");
            return u;
        }
        System.out.println("Credenciales inválidas.");
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) {
        return usuarioDAO.buscarPorDocumento(id);
    }
}
