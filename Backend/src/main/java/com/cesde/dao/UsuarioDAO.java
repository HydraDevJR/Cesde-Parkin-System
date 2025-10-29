package com.cesde.dao;

import com.cesde.conexion.Conexion;
import com.cesde.model.usuarios.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Usuarios.
 * Mapea la tabla:
 *  documento INT PRIMARY KEY,
 *  nombre VARCHAR(100),
 *  correo VARCHAR(100),
 *  contrasena VARCHAR(100),
 *  rol VARCHAR(100),
 *  estado VARCHAR(50),
 *  fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
 */
public class UsuarioDAO {

    public boolean existeCorreoEnBD(String correo) {
        String sql = "SELECT 1 FROM Usuarios WHERE correo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verifica si existe otro usuario con el mismo correo, excluyendo al usuario actual
    public boolean existeCorreoExcluyendo(String correo, int idActual) {
        String sql = "SELECT 1 FROM Usuarios WHERE correo = ? AND documento <> ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setInt(2, idActual);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT documento, nombre, correo, contrasena, rol, estado FROM Usuarios";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("documento"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("rol"),
                        rs.getString("estado")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error listarUsuarios: " + e.getMessage());
        }
        return lista;
    }

    // Insertar nuevo usuario
    public boolean insertarUsuario(Usuario u) {
        String sql = "INSERT INTO Usuarios (documento, nombre, correo, contrasena, rol, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, u.getDocumento());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena()); // → idealmente guardar hash
            ps.setString(5, u.getRol());
            ps.setString(6, u.getEstado());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error insertarUsuario: " + e.getMessage());
            return false;
        }
    }

    public boolean tieneVehiculosAsociados(int documento) {
        String sql = "SELECT 1 FROM Vehiculos WHERE documento = ? LIMIT 1";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si hay al menos un vehículo
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar vehículos asociados: " + e.getMessage());
            return false;
        }
    }

    // Actualizar usuario (por documento)
    public boolean actualizarUsuario(Usuario u, int documentoOriginal) {
        System.out.println("=== DAO: Actualizando usuario ===");
        System.out.println("Documento original: " + documentoOriginal);
        System.out.println("Nuevo documento: " + u.getDocumento());

        // Si intenta cambiar el documento, verificar que no tenga vehículos asociados
        if (u.getDocumento() != documentoOriginal) {
            if (tieneVehiculosAsociados(documentoOriginal)) {
                System.out.println("No se puede cambiar el documento: hay vehículos asociados.");
                return false;
            }
        }

        String sql = "UPDATE Usuarios SET nombre = ?, correo = ?, contrasena = ?, rol = ?, estado = ?, documento = ? WHERE documento = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContrasena());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            ps.setInt(6, u.getDocumento());      // Nuevo documento
            ps.setInt(7, documentoOriginal);      // Documento actual (WHERE)

            int filas = ps.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizarUsuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar usuario por documento
    public boolean eliminarUsuario(int documento) {
        String sqlEliminarVehiculos = "DELETE FROM Vehiculos WHERE documento = ?";
        String sqlEliminarUsuario = "DELETE FROM Usuarios WHERE documento = ?";

        try (Connection con = Conexion.getConexion()) {
            // Desactivar autocommit para manejar transacción
            con.setAutoCommit(false);

            try (PreparedStatement psVeh = con.prepareStatement(sqlEliminarVehiculos);
                 PreparedStatement psUsr = con.prepareStatement(sqlEliminarUsuario)) {

                // Eliminar vehículos asociados
                psVeh.setInt(1, documento);
                psVeh.executeUpdate();

                // Eliminar usuario
                psUsr.setInt(1, documento);
                int filas = psUsr.executeUpdate();

                // Confirmar transacción
                con.commit();

                return filas > 0;
            } catch (SQLException e) {
                con.rollback(); // si algo falla, deshacer todo
                System.out.println("Error eliminarUsuario: " + e.getMessage());
                return false;
            } finally {
                con.setAutoCommit(true); // restaurar autocommit
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión eliminarUsuario: " + e.getMessage());
            return false;
        }
    }


    // Buscar usuario por documento
    public Usuario buscarPorDocumento(int documento) {
        String sql = "SELECT documento, nombre, correo, contrasena, rol, estado FROM Usuarios WHERE documento = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("documento"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("contrasena"),
                            rs.getString("rol"),
                            rs.getString("estado")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error buscarPorDocumento: " + e.getMessage());
        }
        return null;
    }

    // Validar login (por correo y contraseña) -> devuelve Usuario si coincide
    public Usuario validarLogin(String correo, String contrasena) {
        String sql = "SELECT documento, nombre, correo, contrasena, rol, estado FROM Usuarios WHERE correo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passDb = rs.getString("contrasena");
                    // Actualmente comparamos texto plano (temporal)
                    // En producción debes usar hashing (BCrypt) y verificar aquí.
                    if (passDb != null && passDb.equals(contrasena)) {
                        return new Usuario(
                                rs.getInt("documento"),
                                rs.getString("nombre"),
                                rs.getString("correo"),
                                rs.getString("contrasena"),
                                rs.getString("rol"),
                                rs.getString("estado")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validarLogin: " + e.getMessage());
        }
        return null;
    }
}
