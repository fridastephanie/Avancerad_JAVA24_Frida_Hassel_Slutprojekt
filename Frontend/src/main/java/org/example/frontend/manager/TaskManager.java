package org.example.frontend.manager;

import org.example.frontend.service.TaskService;
import org.example.frontend.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager implements TaskManagerInterface {
    //HashMap för alla MorningsTask-objekt
    private HashMap<Integer, Task> morningTasks = new HashMap<>();
    //HashMap för alla AfternoonTask-objekt
    private HashMap<Integer, Task> afternoonTasks = new HashMap<>();
    //HashMap för alla EveningTask-objekt
    private HashMap<Integer, Task> eveningTasks = new HashMap<>();
    //Skapar en referens till TaskService klassen
    private TaskService taskService;

    /**
     * Konstruktor för TaskManager, som skapar en instans av klassen TaskService
     * @param taskService
     */
    public TaskManager(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Metod som inhämtar ett Task-objekt och kollar vilken typ objektet är av,
     * därefter läggs objektet till i rätt HashMap
     * @param task
     */
    public void addTask(Task task) {
        switch (task.getType().toLowerCase()) {
            case "morning":
                morningTasks.put(task.getId(), task);
                break;
            case "afternoon":
                afternoonTasks.put(task.getId(), task);
                break;
            case "evening":
                eveningTasks.put(task.getId(), task);
                break;
        }
    }

    /**
     * Metod som inhämtar en int som nyckel, och raderar därefter id-numret från alla HashMaps
     * @param taskId
     */
    public void removeTask(int taskId) {
        morningTasks.remove(taskId);
        afternoonTasks.remove(taskId);
        eveningTasks.remove(taskId);
    }

    /**
     * Metod som inhämtar ett Task-objekt (som är uppdaterat)
     * och därefter lägger till det i rätt HashMap beroende på type
     * @param task
     */
    public void updateTask(Task task) {
        addTask(task);
    }

    /**
     * Metod som skapar en ArrayList och därefter lägger till innehållet från alla HashMaps med olika typer,
     * från morning-evening så de hamnar i rätt tidsordning när ArrayListen skrivs ut
     * @return
     */
    public List<Task> getAllTasks() {
        List<Task> orderedTasks = new ArrayList<>();
        orderedTasks.addAll(getMorningTasks());
        orderedTasks.addAll(getAfternoonTasks());
        orderedTasks.addAll(getEveningTasks());
        return orderedTasks;
    }

    /**
     * Metod som tömmer HashMap morningTasks och sedan inhämtar de uppdaterade värdena från backend med typen "morning"
     * och därefter läggs de till i en ny ArrayList
     * @return
     */
    public List<Task> getMorningTasks() {
        morningTasks.clear();
        List<Task> tasks = taskService.fetchTasksFromBackend("morning");
        for (Task task : tasks) {
            addTask(task);
        }
        return new ArrayList<>(morningTasks.values());
    }

    /**
     * Metod som tömmer HashMap afternoonTasks och sedan inhämtar de uppdaterade värdena från backend med typen "afternoon"
     * och därefter läggs de till i en ny ArrayList
     * @return
     */
    public List<Task> getAfternoonTasks() {
        afternoonTasks.clear();
        List<Task> tasks = taskService.fetchTasksFromBackend("afternoon");
        for (Task task : tasks) {
            addTask(task);
        }
        return new ArrayList<>(afternoonTasks.values());
    }

    /**
     * Metod som tömmer HashMap eveningTasks och sedan inhämtar de uppdaterade värdena från backend med typen "evening"
     * och därefter läggs de till i en ny ArrayList
     * @return
     */
    public List<Task> getEveningTasks() {
        eveningTasks.clear();
        List<Task> tasks = taskService.fetchTasksFromBackend("evening");
        for (Task task : tasks) {
            addTask(task);
        }
        return new ArrayList<>(eveningTasks.values());
    }
}