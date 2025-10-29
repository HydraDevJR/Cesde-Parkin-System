package com.cesde.dao;

import com.cesde.conexion.Conexion;
import com.cesde.model.vehiculos.Vehiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla Vehiculos.
 * Mapea la tabla:
 *  placa VARCHAR(10) PRIMARY KEY,
 *  tipo VARCHAR(50),
 *  marca VARCHAR(50),
 *  modelo VARCHAR(50),
 *  color VARCHAR(30),
 *  documento INT,
 *  fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP
 */
public class VehiculoDAO {

    // Listar todos los vehículos
    public List<Vehiculo> listarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT placa, tipo, marca, modelo, color, documento, fechaRegistro FROM Vehiculos ORDER BY fechaRegistro ASC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                        rs.getString("placa"),
                        rs.getString("tipo"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("color"),
                        rs.getInt("documento"),
                        rs.getTimestamp("fechaRegistro").toLocalDateTime()
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error listarVehiculos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Listar vehículos de un usuario
    public List<Vehiculo> listarPorUsuario(int id_usuario) {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT placa, tipo, marca, modelo, color, documento, fechaRegistro FROM Vehiculos WHERE documento = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = new Vehiculo(
                            rs.getString("placa"),
                            rs.getString("tipo"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("color"),
                            rs.getInt("documento"),
                            rs.getTimestamp("fechaRegistro").toLocalDateTime()
                    );
                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error listarPorUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Validar si la placa ya existe
    private boolean existePlaca(String placa) {
        String sql = "SELECT 1 FROM Vehiculos WHERE placa = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error verificando placa: " + e.getMessage());
            return false;
        }
    }

    // Validar si el usuario/documento existe
    private boolean existeUsuario(int documento) {
        String sql = "SELECT 1 FROM Usuarios WHERE documento = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error verificando usuario: " + e.getMessage());
            return false;
        }
    }

    // Insertar nuevo vehículo
    public boolean insertarVehiculo(Vehiculo v) {
        if (existePlaca(v.getPlaca())) {
            System.out.println("Error: placa ya existente");
            return false;
        }
        if (!existeUsuario(v.getDocumento())) {
            System.out.println("Error: usuario no registrado");
            return false;
        }

        String sql = "INSERT INTO Vehiculos (placa, tipo, marca, modelo, color, documento, fechaRegistro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getTipo());
            ps.setString(3, v.getMarca());
            ps.setString(4, v.getModelo());
            ps.setString(5, v.getColor());
            ps.setInt(6, v.getDocumento());
            ps.setTimestamp(7, Timestamp.valueOf(v.getFechaRegistro()));

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar vehículo
    public boolean actualizarVehiculo(Vehiculo v, String placaOriginal) {
        System.out.println("=== DAO: Actualizando vehículo ===");
        System.out.println("Placa original: " + placaOriginal);
        System.out.println("Nueva placa: " + v.getPlaca());
        System.out.println("Documento: " + v.getDocumento());

        // Validar que el usuario (documento) exista
        if (!existeUsuario(v.getDocumento())) {
            System.out.println("Error: usuario no registrado");
            return false;
        }

        // Si intenta cambiar la placa, validar que la nueva no esté en uso
        if (!v.getPlaca().equalsIgnoreCase(placaOriginal)) {
            if (existePlaca(v.getPlaca())) {
                System.out.println("Error: la nueva placa ya está registrada");
                return false;
            }
        }

        // Actualizar el vehículo (permite cambiar todos los campos excepto fechaRegistro)
        String sql = "UPDATE Vehiculos SET tipo = ?, marca = ?, modelo = ?, color = ?, documento = ?, placa = ? WHERE placa = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getTipo());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setInt(5, v.getDocumento());
            ps.setString(6, v.getPlaca());           // Nueva placa
            ps.setString(7, placaOriginal);           // Placa actual (WHERE)

            int filas = ps.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizarVehiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar vehículo por placa
    public boolean eliminarVehiculo(String placa) {
        String sql = "DELETE FROM Vehiculos WHERE placa = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error eliminarVehiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Buscar vehículo por placa
    public Vehiculo buscarPorPlaca(String placa) {
        String sql = "SELECT placa, tipo, marca, modelo, color, documento, fechaRegistro FROM Vehiculos WHERE placa = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehiculo(
                            rs.getString("placa"),
                            rs.getString("tipo"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("color"),
                            rs.getInt("documento"),
                            rs.getTimestamp("fechaRegistro").toLocalDateTime()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error buscarPorPlaca: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
