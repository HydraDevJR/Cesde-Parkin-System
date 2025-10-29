package com.cesde.rest;

import com.cesde.dao.VehiculoDAO;
import com.cesde.model.vehiculos.Vehiculo;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador REST para Vehículos.
 * Maneja las peticiones HTTP (GET, POST, PUT, DELETE)
 * que llegan desde el Frontend al backend.
 * Soporta todos los campos de la tabla Vehiculos.
 */
public class VehiculoHandler implements HttpHandler {

    private final VehiculoDAO dao = new VehiculoDAO();
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

        // Configuración CORS
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (method) {
                case "GET":
                    if (segments.length == 3) { // /api/vehiculos
                        listarVehiculos(exchange);
                    } else if (segments.length == 4) { // /api/vehiculos/{placa}
                        String placa = segments[3];
                        buscarVehiculo(exchange, placa);
                    }
                    break;

                case "POST":
                    crearVehiculo(exchange);
                    break;

                case "PUT":
                    if (segments.length == 4) { // /api/vehiculos/{placa}
                        String placa = segments[3];
                        actualizarVehiculo(exchange, placa);
                    }
                    break;

                case "DELETE":
                    if (segments.length == 4) { // /api/vehiculos/{placa}
                        String placa = segments[3];
                        eliminarVehiculo(exchange, placa);
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

    private void listarVehiculos(HttpExchange exchange) throws IOException {
        List<Vehiculo> vehiculos = dao.listarVehiculos();
        sendJson(exchange, 200, vehiculos);
    }

    private void buscarVehiculo(HttpExchange exchange, String placa) throws IOException {
        Vehiculo v = dao.buscarPorPlaca(placa);
        if (v != null) {
            sendJson(exchange, 200, v);
        } else {
            sendResponse(exchange, 404, "Vehículo no encontrado");
        }
    }

    private void crearVehiculo(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Vehiculo nuevo = gson.fromJson(body, Vehiculo.class);

        if (nuevo.getFechaRegistro() == null) nuevo.setFechaRegistro(LocalDateTime.now());

        boolean ok = dao.insertarVehiculo(nuevo);
        if (ok) {
            sendResponse(exchange, 201, "Vehiculo creado correctamente");
        } else {
            sendResponse(exchange, 400, "Error al crear vehículo (placa ya existente o usuario no registrado)");
        }
    }

    private void actualizarVehiculo(HttpExchange exchange, String placa) throws IOException {
        System.out.println("=== Handler: Actualizando vehículo ===");
        System.out.println("Placa original (URL): " + placa);

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Vehiculo actualizado = gson.fromJson(body, Vehiculo.class);

        System.out.println("Placa del body: " + actualizado.getPlaca());
        System.out.println("Documento del body: " + actualizado.getDocumento());

        // Buscar el vehículo existente para preservar la fecha de registro
        Vehiculo existente = dao.buscarPorPlaca(placa);
        if (existente == null) {
            sendResponse(exchange, 404, "Vehículo no encontrado");
            return;
        }

        // Preservar la fecha de registro original
        if (actualizado.getFechaRegistro() == null) {
            actualizado.setFechaRegistro(existente.getFechaRegistro());
        }

        // Validar que el documento (usuario) exista antes de actualizar
        // Esta validación ya está en el DAO, pero podemos hacerla aquí también

        // Intentar actualizar pasando la placa original
        boolean ok = dao.actualizarVehiculo(actualizado, placa);

        if (ok) {
            System.out.println("Vehículo actualizado exitosamente");
            sendResponse(exchange, 200, "Vehiculo actualizado exitosamente");
        } else {
            System.out.println("Error al actualizar vehículo");
            sendResponse(exchange, 400, "Error al actualizar vehículo (placa duplicada o usuario no registrado)");
        }
    }

    private void eliminarVehiculo(HttpExchange exchange, String placa) throws IOException {
        boolean ok = dao.eliminarVehiculo(placa);
        if (ok) {
            sendResponse(exchange, 200, "Vehículo eliminado correctamente");
        } else {
            sendResponse(exchange, 404, "Vehículo no encontrado");
        }
    }

    // ====================== RESPUESTAS ====================== //

    private void sendJson(HttpExchange exchange, int status, Object data) throws IOException {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, json.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, message.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
