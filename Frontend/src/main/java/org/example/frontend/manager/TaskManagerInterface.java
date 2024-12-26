package org.example.frontend.manager;

import org.example.frontend.task.Task;

import java.util.List;

public interface TaskManagerInterface {
    //Metod för att lägga till ett Task-objekt
    void addTask(Task task);
    //Metod för att ta bort ett Task-objekt
    void removeTask(int taskId);
    //Metod för att uppdatera/edit ett Task-objekt
    void updateTask(Task task);
    //Metod för att hämta alla Task-objekt
    List<Task> getAllTasks();
    //Metod för att hämta alla Task-objekt av typen "morning"
    List<Task> getMorningTasks();
    //Metod för att hämta alla Task-objekt av typen "afternoon"
    List<Task> getAfternoonTasks();
    //Metod för att hämta alla Task-objekt av typen "evening"
    List<Task> getEveningTasks();
}