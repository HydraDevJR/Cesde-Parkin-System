package com.cesde.rest;

import com.cesde.dao.UsuarioDAO;
import com.cesde.model.usuarios.Usuario;
import com.google.gson.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                    (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                    (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();

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
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Usuario nuevo = gson.fromJson(body, Usuario.class);

        // Validar documento duplicado
        if (dao.buscarPorDocumento(nuevo.getDocumento()) != null) {
            sendResponse(exchange, 400, "Error: Documento ya registrado por otro usuario");
            return;
        }

        // Validar correo duplicado
        if (dao.validarLogin(nuevo.getCorreo(), nuevo.getContrasena()) != null ||
                dao.existeCorreoEnBD(nuevo.getCorreo())) {
            sendResponse(exchange, 400, "Error: Correo ya registrado por otro usuario");
            return;
        }

        boolean ok = dao.insertarUsuario(nuevo);
        if (ok)
            sendResponse(exchange, 201, "Usuario creado correctamente");
        else
            sendResponse(exchange, 400, "Error al crear usuario");
    }

    private void actualizarUsuario(HttpExchange exchange, int id) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Usuario actualizado = gson.fromJson(body, Usuario.class);
        System.out.println("Actualizando usuario con ID original: " + id);
        System.out.println("Nuevo documento solicitado: " + actualizado.getDocumento());

        // Si intenta cambiar el documento, validar que el nuevo no esté en uso
        if (actualizado.getDocumento() != id) {
            if (dao.buscarPorDocumento(actualizado.getDocumento()) != null) {
                sendResponse(exchange, 400, "Error: El nuevo documento ya está registrado por otro usuario");
                return;
            }
        }

        // Validar correo duplicado (excluyendo al usuario actual)
        if (dao.existeCorreoExcluyendo(actualizado.getCorreo(), id)) {
            sendResponse(exchange, 400, "Error: Correo ya registrado por otro usuario");
            return;
        }

        // Intentar actualizar con el documento original como referencia
        boolean ok = dao.actualizarUsuario(actualizado, id);
        if (ok) {
            System.out.println("Usuario actualizado exitosamente");
            sendResponse(exchange, 200, "Usuario actualizado correctamente");
        } else {
            System.out.println("Error al actualizar usuario");
            sendResponse(exchange, 400, "Error: No se puede actualizar el documento, tiene vehículos asociados o hubo otro error");
        }
    }

    private void eliminarUsuario(HttpExchange exchange, int id) throws IOException {
        boolean ok = dao.eliminarUsuario(id);
        if (ok)
            sendResponse(exchange, 200, "Usuario eliminado correctamente y sus vehiculos asociados");
        else
            sendResponse(exchange, 404, "Usuario no encontrado");
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
