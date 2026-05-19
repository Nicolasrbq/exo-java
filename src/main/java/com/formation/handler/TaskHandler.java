package com.formation.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.controller.TaskController;
import com.formation.exception.InvalidTaskException;
import com.formation.exception.TaskNotFoundException;
import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.SimpleTask;
import com.formation.model.TaskId;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TaskHandler implements HttpHandler {
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
                handleGetAll(exchange);
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

    private void sendError(HttpExchange exchange, int i, String routeNotFound, String s) {
        exchange.sendResponseHeaders(i, s);
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
            exchange.sendResponseHeaders(200, 0);
        } catch (Exception e ) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    private void handleUpdate(HttpExchange exchange, int id) {

    }

    private void handleCreate(HttpExchange exchange) throws IOException {    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        try {
            controller.getTaskById(id);
            exchange.sendResponseHeaders(200, 0);
        } catch(Exception e) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        controller.getAllTasks();
        exchange.sendResponseHeaders(200, 0);
    }

    private int extractId(String path) {
        // "/tasks/42" ® 42
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}
