package com.cesde.rest;

import com.cesde.dao.UsuarioDAO;
import com.cesde.model.usuarios.Usuario;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Controlador REST para Usuarios.
 * Maneja las peticiones HTTP (GET, POST, PUT, DELETE)
 * que llegan desde el Frontend al backend.
 */
public class UsuarioHandler implements HttpHandler {

    private final UsuarioDAO dao = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        // Configuración CORS (permite conexión desde el frontend)
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        // Preflight para CORS
        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (method) {
                case "GET":
                    if (segments.length == 3) { // /api/usuarios
                        listarUsuarios(exchange);
                    } else if (segments.length == 4) { // /api/usuarios/{id}
                        int id = Integer.parseInt(segments[3]);
                        buscarUsuario(exchange, id);
                    }
                    break;

                case "POST":
                    crearUsuario(exchange);
                    break;

                case "PUT":
                    if (segments.length == 4) { // /api/usuarios/{id}
                        int id = Integer.parseInt(segments[3]);
                        actualizarUsuario(exchange, id);
                    }
                    break;

                case "DELETE":
                    if (segments.length == 4) { // /api/usuarios/{id}
                        int id = Integer.parseInt(segments[3]);
                        eliminarUsuario(exchange, id);
                    }
                    break;

                default:
                    sendResponse(exchange, 405, "Método no permitido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
        }
    }

    // ====================== MÉTODOS CRUD ====================== //

    private void listarUsuarios(HttpExchange exchange) throws IOException {
        List<Usuario> usuarios = dao.listarUsuarios();
        sendJson(exchange, 200, usuarios);
    }

    private void buscarUsuario(HttpExchange exchange, int id) throws IOException {
        Usuario usuario = dao.buscarPorDocumento(id);
        if (usuario != null)
            sendJson(exchange, 200, usuario);
        else
            sendResponse(exchange, 404, "Usuario no encontrado");
    }

    private void crearUsuario(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Usuario nuevo = gson.fromJson(body, Usuario.class);

        boolean ok = dao.insertarUsuario(nuevo);
        if (ok)
            sendResponse(exchange, 201, "✅ Usuario creado correctamente");
        else
            sendResponse(exchange, 400, "❌ Error al crear usuario");
    }

    private void actualizarUsuario(HttpExchange exchange, int id) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Usuario actualizado = gson.fromJson(body, Usuario.class);
        actualizado.setDocumento(id);

        boolean ok = dao.actualizarUsuario(actualizado);
        if (ok)
            sendResponse(exchange, 200, "✅ Usuario actualizado correctamente");
        else
            sendResponse(exchange, 400, "❌ Error al actualizar usuario");
    }

    private void eliminarUsuario(HttpExchange exchange, int id) throws IOException {
        boolean ok = dao.eliminarUsuario(id);
        if (ok)
            sendResponse(exchange, 200, "🗑️ Usuario eliminado correctamente");
        else
            sendResponse(exchange, 404, "❌ Usuario no encontrado");
    }

    // ====================== RESPUESTAS ====================== //

    private void sendJson(HttpExchange exchange, int status, Object data) throws IOException {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, message.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
    }
}
