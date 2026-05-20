package com.formation;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.formation.controller.TaskController;
import com.formation.dto.TaskMapper;
import com.formation.handler.TaskHandler;
import com.formation.repository.InMemoryTaskRepository;
import com.formation.repository.TaskRepository;
import com.formation.service.DefaultTaskService;
import com.formation.service.TaskService;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

import org.slf4j.Logger;

public class App {
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(App.class);
    public static void main(String[] args) throws Exception {
// 1. Construire la chaîne de dépendances (DI manuelle)
        TaskRepository repo = new InMemoryTaskRepository();
        TaskService service = new DefaultTaskService(repo);
        TaskMapper mapper = new TaskMapper();
        TaskController controller = new TaskController(service, mapper);
        ObjectMapper jackson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // pour LocalDate
// 2. Créer le serveur sur le port 8080
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
// 3. Enregistrer les handlers
        server.createContext("/tasks", new TaskHandler(controller, jackson));
// 4. Démarrer
        server.start();
        log.info("Serveur démarré sur http://localhost:8080");
    }
}