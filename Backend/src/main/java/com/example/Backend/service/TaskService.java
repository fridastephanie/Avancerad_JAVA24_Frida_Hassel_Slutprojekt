package com.example.Backend.service;
import com.example.Backend.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    //Skapar en referens List av objekttypen Task
    private List<Task> tasks;
    //Skapar en referensvariabel som används för att sätta ID till Task objekten
    private int idCounter;

    /**
     * Konstruktor för TaskService som initierar listan tasks och sätter värdet på idCounter till 1
     */
    public TaskService() {
        this.tasks = new ArrayList<>();
        this.idCounter = 1;
    }

    /**
     * Metod som hämtar alla sparade Task objekt från en List
     * @return
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Metod som kollar igenom alla Task objekt i listan tasks och hämtar de objekt med den Type som skickas i argumentet
     * @param type
     * @return
     */
    public List<Task> getTasksByType(String type) {
        return tasks.stream()
                .filter(task -> task.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Metod som kollar igenom alla Task objekt i listan tasks och hämtar de objektet vars id skickats i argumentet
     * @param id
     * @return
     */
    public Optional<Task> getTaskById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    /**
     * Metod som inhämtar ett Task objekt och sätter/skapar dens id samt lägger till objektet i listan
     * @param task
     * @return
     */
    public Task addTask(Task task) {
        task.setId(idCounter++);
        tasks.add(task);
        return task;
    }

    /**
     * Metod som uppdaterar egenskaperna för en task och returnerar den uppdaterade tasken (eller felmeddelande)
     * @param id
     * @param updatedTask
     * @return
     */
    public Optional<Task> updateTask(int id, Task updatedTask) {
        Optional<Task> existingTask = getTaskById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setType(updatedTask.getType());
            task.setChecked(updatedTask.isChecked());
            return Optional.of(task);
        }
        return Optional.empty();
    }

    /**
     * Metod som raderar en Task (från listan tasks)
     * @param id
     * @return
     */
    public boolean deleteTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    /**
     * Metod som uppdaterar isChecked-statusen för en task och returnerar den uppdaterade tasken (eller felmeddelande)
     * @param id
     * @param isChecked
     */
    public void setChecked(int id, boolean isChecked) {
        Optional<Task> task = getTaskById(id);
        task.ifPresent(t -> t.setChecked(isChecked));
    }
}