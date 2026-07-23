package com.formation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
// Équivaut à @Configuration + @EnableAutoConfiguration + @ComponentScan
// Spring va scanner tout le package fr.formation.taskmanager et ses sous-packages
public class TaskManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
// Spring démarre Tomcat sur le port 8080,
// scanne les beans, injecte les dépendances
    }
}