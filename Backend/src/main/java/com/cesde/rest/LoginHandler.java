package com.cesde.rest;

import com.cesde.dao.UsuarioDAO;
import com.cesde.model.usuarios.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Handler para manejar el login de usuarios.
 * Valida correo, contraseña y estado del usuario.
 */
public class LoginHandler implements HttpHandler {
    private final UsuarioDAO dao = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Configuración CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equalsIgnoreCase(method)) {
            procesarLogin(exchange);
        } else {
            sendResponse(exchange, 405, "Método no permitido");
        }
    }

    private void procesarLogin(HttpExchange exchange) throws IOException {
        try {
            // Leer el body de la petición
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JsonObject loginData = gson.fromJson(body, JsonObject.class);

            String correo = loginData.get("correo").getAsString();
            String contrasena = loginData.get("contrasena").getAsString();

            System.out.println("=== Intento de login ===");
            System.out.println("Correo: " + correo);

            // Validar credenciales usando el DAO
            Usuario usuario = dao.validarLogin(correo, contrasena);

            if (usuario == null) {
                System.out.println("Login fallido: credenciales incorrectas");
                sendResponse(exchange, 401, "Correo o contraseña incorrectos");
                return;
            }

            // Verificar que el usuario esté activo
            if (!"Activo".equalsIgnoreCase(usuario.getEstado())) {
                System.out.println("Login fallido: usuario inactivo");
                sendResponse(exchange, 403, "Usuario inactivo. Contacte al administrador");
                return;
            }

            System.out.println("Login exitoso para: " + usuario.getNombre() + " (Rol: " + usuario.getRol() + ")");

            // Crear respuesta con los datos del usuario (sin la contraseña)
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("mensaje", "Login exitoso");
            response.addProperty("documento", usuario.getDocumento());
            response.addProperty("nombre", usuario.getNombre());
            response.addProperty("correo", usuario.getCorreo());
            response.addProperty("rol", usuario.getRol());
            response.addProperty("estado", usuario.getEstado());

            sendJson(exchange, 200, response);

        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            e.printStackTrace();
            sendResponse(exchange, 500, "Error interno del servidor");
        }
    }

    private void sendJson(HttpExchange exchange, int status, Object data) throws IOException {
        String json = gson.toJson(data);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}