package org.example.frontend.service;

import javafx.scene.control.Alert;
import org.example.frontend.controller.TaskController;
import org.example.frontend.manager.TaskManager;
import org.example.frontend.manager.TaskManagerInterface;
import org.example.frontend.task.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.example.frontend.task.TaskFactory.createTask;

public class TaskService {
    //Static final String som är URL till backend
    private static final String BASE_URL = "http://localhost:8080/api/tasks";
    //Skapar en referens till klassen TaskController
    private TaskController taskController;
    //Skapar en referens till TaskManagerInterface (använder Interface för att inte vara beroende av just TaskManager klassen)
    private TaskManagerInterface taskManager;

    /**
     * Konstruktor för TaskService, som inhämtar ett objekt av klassen TaskController som initieras till referensen i konstruktorn
     * och ett objekt av klassen TaskManager initieras till referensvariabeln
     * @param
     * @param taskController
     */
    public TaskService(TaskController taskController) {
        this.taskController = taskController;
        this.taskManager = new TaskManager(this);
    }

    /**
     * Metod som skickar ett Task-objekt till backend
     * @param task
     */
    public void sendTaskToBackend(Task task) {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\", \"type\":\"%s\"}",
                    task.getTitle(), task.getDescription(), task.getType());

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                taskController.actAllTasks(null);
            } else {
                String errorMessage = readResponse(connection);
                taskController.showAlert("Error", "Failed to add task. HTTP Response Code: " + responseCode + "\n" + errorMessage, Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            taskController.showAlert("Error", "An error occurred while sending the task.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Metod som uppdaterar ett Task-objekts title, descirption och type i backend     *
     * @param task
     * @throws IOException
     */
    public void updateTaskInBackend(Task task) throws IOException {
        URL url = new URL(BASE_URL + "/" + task.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\", \"type\":\"%s\"}",
                                                task.getTitle(), task.getDescription(), task.getType());

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = connection.getResponseCode();
        String responseMessage = readResponse(connection);
        handleApiResponse(responseCode, responseMessage);
    }

    /**
     * Metod som skickar en Delete förfrågan till backend på ett Task-Objekt
     * @param task
     * @return
     * @throws IOException
     */
    public HttpURLConnection deleteTaskFromBackend(Task task) throws IOException {
        URL url = new URL(BASE_URL + "/" + task.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        return connection;
    }

    /**
     * Metod som uppdaterar ett Task-objekts isChecked värde i backend
     * @param taskId
     * @param isChecked
     */
    public void updateTaskStatus(int taskId, boolean isChecked) {
        try {
            String json = Boolean.toString(isChecked);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/" + taskId + "/check"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metod som inhämtar uppgifter från backend baserat på vilken typ av Task-objekt det är (morning, afternoon, evening)
     * och därefter läggs objekten till i HashMap-listorna beroende på type samt skapar en lista med alla tasks som returneras
     * @param timeOfDay
     * @return
     */
    public List<Task> fetchTasksFromBackend(String timeOfDay) {
        String urlString = BASE_URL + "/" + timeOfDay;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                List<Task> tasks = parseTaskListFromJson(response);
                return tasks;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Metod som konverterar en JSON-sträng som innehåller en lista av Task-objekt och därefter skapar en lista av Task-objekt
     * @param response
     * @return
     */
    public static List<Task> parseTaskListFromJson(String response) {
        List<Task> tasks = new ArrayList<>();
        try {
            String[] taskStrings = extractTaskStrings(response);

            for (String taskStr : taskStrings) {
                Task task = createTaskFromJson(taskStr);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * Metod som visar popup-meddelande vid lyckad eller misslyckad uppdatering/edit i backend av ett TaskObjekt
     * @param responseCode
     * @param responseMessage
     */
    private void handleApiResponse(int responseCode, String responseMessage) {
        if (responseCode == HttpURLConnection.HTTP_OK) {
             taskController.actAllTasks(null);
        } else {
             taskController.showAlert("Error", "Failed to update task. HTTP Response Code: " + responseCode + "\n" + responseMessage, Alert.AlertType.ERROR);
        }
    }

    /**
     * Metod som inhämtar svaret från Delete förfrågan från backend,
     * samt raderar Task-objektet i frontent via metod i TaskManager klassen
     * @param connection
     * @param task
     * @return
     * @throws IOException
     */
    public void handleDeleteResponse(HttpURLConnection connection, Task task) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            taskManager.removeTask(task.getId());
        }
    }

    /**
     * Metod som loggar om fel uppstår vid borttagning
     * @param e
     */
    public void handleDeleteError(IOException e) {
        e.printStackTrace();
    }

    /**
     * Metod för att extrahera Task-strängar från den övergripande JSON-strängen.
     * @param response
     * @return
     */
    private static String[] extractTaskStrings(String response) {
        response = response.replace("[", "").replace("]", "");
        return response.split("},\\{");
    }

    /**
     * Metod för att skapa ett Task-objekt från en task-sträng.
     * @param taskStr
     * @return
     */
    private static Task createTaskFromJson(String taskStr) {
        taskStr = taskStr.replace("{", "").replace("}", "").trim();
        String[] taskData = taskStr.split(",");

        int id = -1;
        String title = "";
        String description = "";
        String type = "";
        boolean isChecked = false;

        for (String data : taskData) {
            String[] keyValue = data.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");

                switch (key) {
                    case "id":
                        id = Integer.parseInt(value);
                        break;
                    case "title":
                        title = value;
                        break;
                    case "description":
                        description = value;
                        break;
                    case "type":
                        type = value;
                        break;
                    case "checked":
                        isChecked = Boolean.parseBoolean(value);
                        break;
                }
            }
        }
        if (id != -1 && !title.isEmpty() && !description.isEmpty()) {
            return createTask(title, description, type, id, isChecked);
        }
        return null;
    }

    /**
     * Metod som läser svaret från HTTP-förfrågan
     * @param connection
     * @return
     * @throws IOException
     */
    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringWriter response = new StringWriter()) {
            String line;
            while ((line = in.readLine()) != null) {
                response.write(line);
            }
            return response.toString();
        }
    }
}