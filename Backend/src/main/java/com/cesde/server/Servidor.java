package com.cesde.server;

import com.cesde.rest.LoginHandler;
import com.sun.net.httpserver.HttpServer;
import com.cesde.rest.UsuarioHandler;
import com.cesde.rest.VehiculoHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Servidor {

    public static void iniciarServidor() {
        try {
            int puerto = 8080;
            HttpServer server = HttpServer.create(new InetSocketAddress(puerto), 0);

            server.createContext("/api/login", new LoginHandler());
            server.createContext("/api/usuarios", new UsuarioHandler());
            server.createContext("/api/vehiculos", new VehiculoHandler());

            server.setExecutor(null);
            server.start();

            System.out.println("Servidor HTTP en ejecucion: http://localhost:" + puerto);
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor HTTP: " + e.getMessage());
        }
    }
}
