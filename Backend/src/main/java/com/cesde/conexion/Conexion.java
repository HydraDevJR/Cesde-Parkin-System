package com.cesde.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CESDE_Parking_System;encrypt=false;trustServerCertificate=true;integratedSecurity=true;";
    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL);
                System.out.println("Conexion exitosa con SQL Server");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver SQL Server no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
