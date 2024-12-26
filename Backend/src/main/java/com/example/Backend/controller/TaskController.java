package com.example.Backend.controller;

import com.example.Backend.model.Task;
import com.example.Backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    //Skapar en final referensvariabel av TaskService klassen
    private final TaskService taskService;

    /**
     * Konstruktor för TaskController som hämtar en TaskService och tilldelar referensvariabel taskService
     * @param taskService
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Metod som kallar på en metod i TaskService, som hämtar alla sparade Task objekt från en List
     * @return
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Metod som kallar på en metod i TaskService, som hämtar alla sparade Task objekt med typen "Morning"
     * @return
     */
    @GetMapping("/morning")
    public List<Task> getMorningTasks() {
        return taskService.getTasksByType("Morning");
    }

    /**
     * Metod som kallar på en metod i TaskService, som hämtar alla sparade Task objekt med typen "Afternoon"
     * @return
     */
    @GetMapping("/afternoon")
    public List<Task> getAfternoonTasks() {
        return taskService.getTasksByType("Afternoon");
    }

    /**
     * Metod som kallar på en metod i TaskService, som hämtar alla sparade Task objekt med typen "Evening"
     * @return
     */
    @GetMapping("/evening")
    public List<Task> getEveningTasks() {
        return taskService.getTasksByType("Evening");
    }

    /**
     * Metod som kallar på metod i TaskService, som uppdaterar isChecked-statusen för en task och returnerar den uppdaterade tasken (eller felmeddelande)
     * @return
     */
    @PutMapping("/{id}/check")
    public ResponseEntity<Task> checkTask(@PathVariable int id, @RequestBody boolean isChecked) {
        taskService.setChecked(id, isChecked);
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Metod som skapar en ny Task
     * @param task
     * @return
     */
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        Task createdTask = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Metod som kallar på metod i TaskService, som uppdaterar egenskaperna för en task och returnerar den uppdaterade tasken (eller felmeddelande)
     * @param id
     * @param updatedTask
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task updatedTask) {
        Optional<Task> task = taskService.updateTask(id, updatedTask);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Metod som kallar på metod i TaskService, som raderar en Task (från List)
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        boolean removed = taskService.deleteTask(id);
        if (removed) {
            return ResponseEntity.ok("Task with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with ID " + id);
        }
    }
}