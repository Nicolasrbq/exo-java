package com.formation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.formation.controller.TaskController;
import com.formation.dto.TaskMapper;
import com.formation.handler.TaskHandler;
import com.formation.repository.InMemoryTaskRepository;
import com.formation.repository.TaskRepository;
import com.formation.service.DefaultTaskService;
import com.formation.service.TaskService;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class App {

}
