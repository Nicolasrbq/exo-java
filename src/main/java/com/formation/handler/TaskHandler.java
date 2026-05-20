package com.formation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.controller.TaskController;
import com.formation.dto.ErrorResponse;
import com.formation.dto.TaskDTO;
import com.formation.exception.InvalidTaskException;
import com.formation.exception.TaskNotFoundException;
import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.SimpleTask;
import com.formation.model.TaskId;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;

public class TaskHandler implements HttpHandler {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(TaskHandler.class);
	
    private final TaskController controller;
    private final ObjectMapper jackson;
    public TaskHandler(TaskController controller, ObjectMapper jackson) {
        this.controller = controller;
        this.jackson = jackson;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod(); // "GET", "POST"...
        String path = exchange.getRequestURI().getPath(); // "/tasks" ou "/tasks/42"
        try {
            // Routing : méthode + forme de l'URL
            if ("GET".equals(method) && path.equals("/tasks")) {
                handleGetAllActive(exchange);
            } else if ("GET".equals(method) && path.matches("/tasks/\\d+")) {
                int id = extractId(path);
                handleGetById(exchange, id);
            } else if ("POST".equals(method) && path.equals("/tasks")) {
                handleCreate(exchange);
            } else if ("PUT".equals(method) && path.matches("/tasks/\\d+")) {
                int id = extractId(path);
                handleUpdate(exchange, id);
            } else if ("PATCH".equals(method) && path.matches("/tasks/\\d+/complete")) {
                int id = extractId(path);
                handleComplete(exchange, id);
            } else if ("DELETE".equals(method) && path.matches("/tasks/\\d+")) {
                int id = extractId(path);
                handleDelete(exchange, id);
            } else {
                sendError(exchange, 404, "ROUTE_NOT_FOUND", "Route inconnue : " + path);
            }
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, "TASK_NOT_FOUND", e.getMessage());
        } catch (InvalidTaskException e) {
            sendError(exchange, 400, "INVALID_TASK", e.getMessage());
        } catch (Exception e) {
            log.error("Erreur inattendue", e);
            sendError(exchange, 500, "INTERNAL_ERROR", "Erreur serveur interne");
        }
    }

 // Envoie une réponse JSON avec le bon code HTTP
    private void sendJson(HttpExchange ex, int statusCode, Object body) throws IOException {
    byte[] bytes = jackson.writeValueAsBytes(body);
    ex.getResponseHeaders().set("Content-Type", "application/json");
    ex.sendResponseHeaders(statusCode, bytes.length);
    ex.getResponseBody().write(bytes);
    ex.close();
    }
    // Lit le body de la requête et le désérialise
    private <T> T readBody(HttpExchange ex, Class<T> clazz) throws IOException {
    try (InputStream is = ex.getRequestBody()) {
    return jackson.readValue(is, clazz);
    } }
    // Envoie une réponse d'erreur standardisée
    private void sendError(HttpExchange ex, int status,
    String code, String message) throws IOException {
    var error = new ErrorResponse(status, code, message,
    ex.getRequestURI().getPath());
    sendJson(ex, status, error);
    }

    private void handleDelete(HttpExchange exchange, int id) throws IOException {
        try {
            controller.deleteTask(id);
            exchange.sendResponseHeaders(204, 0);
        } catch (Exception e) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    private void handleComplete(HttpExchange exchange, int id) throws IOException {
        try {
            controller.completeTask(id);
            sendJson(exchange, 200, "Tache marque comme complete");
        } catch (Exception e ) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    private void handleUpdate(HttpExchange exchange, int id) throws IOException {
    	TaskDTO dto = readBody(exchange, TaskDTO.class);
		try {
			TaskDTO created = controller.updateTask(dto, id);
			sendJson(exchange, 201, created);
		} catch (InvalidTaskException e) {
			sendError(exchange, 400, "INVALID_TASK", e.getMessage());
		}
    }

    private void handleCreate(HttpExchange exchange) throws IOException {  
		TaskDTO dto = readBody(exchange, TaskDTO.class);
		try {
			TaskDTO created = controller.createTask(dto);
			sendJson(exchange, 201, created);
		} catch (InvalidTaskException e) {
			sendError(exchange, 400, "INVALID_TASK", e.getMessage());
		}
    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        try {
           TaskDTO dto =  controller.getTaskById(id);
           sendJson(exchange, 200, dto);
        } catch(Exception e) {
            sendError(exchange, 404, "TASK_NOT_FOUND", "Tache non trouvee avec id : %s".formatted(id)); // j'utilise ici .formatted() pour concaténer au lieu de null c'est une bonne pratique pour éviter les nullPointerException.
        }
    }

    private void handleGetAllActive(HttpExchange exchange) throws IOException {
      List<TaskDTO> tasks =   controller.getAllActiveTasks();
       sendJson(exchange, 200, tasks);
    }

    private int extractId(String path) {
        // "/tasks/42" ® 42
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}
