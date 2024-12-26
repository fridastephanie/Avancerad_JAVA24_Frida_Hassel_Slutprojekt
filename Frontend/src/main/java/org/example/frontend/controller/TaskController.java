package org.example.frontend.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.frontend.manager.TaskManager;
import org.example.frontend.manager.TaskManagerInterface;
import org.example.frontend.service.TaskService;
import org.example.frontend.task.Task;
import org.example.frontend.task.TaskFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskController {
    //Skapar en static int med värdet 1, som används för att tilldela objekt ett id
    private static int idCounter = 1;
    //Skapar en referens till TaskService klassen
    private TaskService taskService;
    //Skapar en referens till TaskManagerInterface (använder Interface för att inte vara beroende av just TaskManager klassen)
    private TaskManagerInterface taskManager;

    /**
     * Konstruktor för TaskController, som skapar en instans av klassen TaskManager och klassen TaskService
     */
    public TaskController() {
        taskService = new TaskService(this);
        taskManager = new TaskManager(taskService);
    }

    @FXML
    private ToggleGroup TaskTime;
    @FXML
    private ToggleGroup ManageTask;
    @FXML
    private Button btnAddTask;
    @FXML
    private Button btnAfternoonTasks;
    @FXML
    private Button btnAllTasks;
    @FXML
    private Button btnDeleteTask;
    @FXML
    private Button btnEditTask;
    @FXML
    private Button btnEveningTasks;
    @FXML
    private Button btnMorningTasks;
    @FXML
    private Label lblDate;
    @FXML
    private ScrollPane paneShowTasks;
    @FXML
    private VBox taskListContainer;
    @FXML
    private RadioButton radioAfternoonTask;
    @FXML
    private RadioButton radioEveningTask;
    @FXML
    private RadioButton radioMorningTask;
    @FXML
    private RadioButton radioDeleteTask;
    @FXML
    private RadioButton radioEditTask;
    @FXML
    private TextArea txaTaskDescription;
    @FXML
    private TextField txfTaskTitle;
    @FXML
    private ImageView imgAllDay;
    @FXML
    private ImageView imgCheckList;

    /**
     * Sätter text och bilder som ska synas när programmet startar
     */
    @FXML
    public void initialize() {
        lblDate.setText(String.valueOf(LocalDate.now()));
        Image editImage = new Image("file:src/main/resources/image/allDay.png");
        imgAllDay.setImage(editImage);
        Image checkListImage = new Image("file:src/main/resources/image/checkList.jpg");
        imgCheckList.setImage(checkListImage);
    }

    /**
     * Inhämtar användarens inmatningar och skapar därefter ett Task-objekt genom TaskFactory
     * @param event
     */
    @FXML
    void actAddTask(ActionEvent event) {
        String title = txfTaskTitle.getText().trim();
        String description = txaTaskDescription.getText().trim();

        if (title.isEmpty() || description.isEmpty() || TaskTime.getSelectedToggle() == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        String taskType = getTaskType();
        createNewTask(title, description, taskType);
    }

    /**
     * Visar alla MorningTask-objekt och ändrar bilden till "morning.png"
     * @param event
     */
    @FXML
    public void actMorningTasks(ActionEvent event) {
        displayTasks(taskManager.getMorningTasks());
        Image editImage = new Image("file:src/main/resources/image/morning.png");
        imgAllDay.setImage(editImage);
    }

    /**
     * Visar alla AfternoonTask-objekt och ändrar bilden till "noon.png"
     * @param event
     */
    @FXML
    public void actAfternoonTasks(ActionEvent event) {
        displayTasks(taskManager.getAfternoonTasks());
        Image editImage = new Image("file:src/main/resources/image/noon.png");
        imgAllDay.setImage(editImage);
    }

    /**
     * Visar alla EveningTask-objekt och ändrar bilden till "evening.png"
     * @param event
     */
    @FXML
    public void actEveningTasks(ActionEvent event) {
        displayTasks(taskManager.getEveningTasks());
        Image editImage = new Image("file:src/main/resources/image/evening.png");
        imgAllDay.setImage(editImage);
    }

    /**
     * Visar alla Task-objekt och ändrar bilden till "allDay.png"
     * @param event
     */
    @FXML
    public void actAllTasks(ActionEvent event) {
        displayTasks(taskManager.getAllTasks());
        Image editImage = new Image("file:src/main/resources/image/allDay.png");
        imgAllDay.setImage(editImage);
    }

    /**
     * Metod som skapar ett nytt Task-objekt i både backend och frontend, baserat på användarens input
     */
    private void createNewTask(String title, String description, String taskType) {
        int id = idCounter++;
        boolean isChecked = false;

        Task newTask = TaskFactory.createTask(title, description, taskType, id, isChecked);

        if (newTask != null) {
            taskService.sendTaskToBackend(newTask);
            taskManager.addTask(newTask);
            clearInputFields();
        }
    }

    /**
     * Metod som låter användaren ändra titel, beskrivning och typ (morning, afternoon, evening) på ett befintligt Task-objekt,
     * därefter uppdateras objektet både i frontend och backend
     * @param task
     */
    private void editTask(Task task) {
        String newTitle = getNewTitle(task);
        String newDescription = getNewDescription(task);
        String newType = getNewType(task);

        updateTaskFields(task, newTitle, newDescription, newType);
        taskManager.updateTask(task);

        try {
            taskService.updateTaskInBackend(task);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the task.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Metod som låter användaren ta bort ett befintligt Task-objekt,
     * därefter raderas objektet både i frontend och backend
     * @param task
     */
    private void deleteTask(Task task) {
        if (showDeleteConfirmationDialog()) {
            try {
                HttpURLConnection connection = taskService.deleteTaskFromBackend(task);
                taskService.handleDeleteResponse(connection, task);
                actAllTasks(null);
            } catch (IOException e) {
                taskService.handleDeleteError(e);
                showAlert("Error", "An error occurred while deleting the task.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Metod som skapar utskrifterna på de Task-objekt som har skapats
     * @param tasks
     */
    public void displayTasks(List<Task> tasks) {
        taskListContainer.getChildren().clear();
        for (Task task : tasks) {
            VBox taskBox = createTaskBox();

            Text taskTitle = createTaskTitle(task);
            Text taskDescription = createTaskDescription(task);

            ImageView checkIcon = createCheckIcon(task, taskTitle, taskDescription);
            ImageView editIcon = createEditIcon(task);
            ImageView deleteIcon = createDeleteIcon(task);

            HBox buttonsBox = createButtonsBox(editIcon, deleteIcon, checkIcon);

            taskBox.getChildren().addAll(taskTitle, taskDescription, buttonsBox);
            taskListContainer.getChildren().add(taskBox);
        }
    }

    /**
     * Metod som bestämmer vilken typ av task(morning, afternoon, evening) som användaren har valt
     */
    private String getTaskType() {
        if (radioMorningTask.isSelected()) {
            return "morning";
        } else if (radioAfternoonTask.isSelected()) {
            return "afternoon";
        } else if (radioEveningTask.isSelected()) {
            return "evening";
        }
        return "";
    }

    /**
     * Metod som rensar inmatningsfälten och bockar ur radioboxarna
     */
    private void clearInputFields() {
        txfTaskTitle.clear();
        txaTaskDescription.clear();
        TaskTime.selectToggle(null);
    }

    /**
     * Metod som skapar en ny VBox
     * @return
     */
    private VBox createTaskBox() {
        VBox taskBox = new VBox();
        taskBox.setSpacing(10);
        return taskBox;
    }

    /**
     * Metod som skapar en ny text (för rubrik/title av task)
     * @param task
     * @return
     */
    private Text createTaskTitle(Task task) {
        Text taskTitle = new Text(task.getTitle());
        taskTitle.setStyle("-fx-font-size: 18px;");
        taskTitle.setStrikethrough(task.isChecked());
        return taskTitle;
    }

    /**
     * Metod som skapar ny text (för beskrivning av task)
     * @param task
     * @return
     */
    private Text createTaskDescription(Task task) {
        Text taskDescription = new Text(task.getDescription());
        taskDescription.setStyle("-fx-font-size: 14px;");
        taskDescription.setStrikethrough(task.isChecked());
        return taskDescription;
    }

    /**
     * Metod som skapar en ImageView med setOnMouseClicked effekter (för att ändra innehåll i task)
     * @param task
     * @return
     */
    private ImageView createEditIcon(Task task) {
        Image editImage = new Image("file:src/main/resources/image/edit.png");
        ImageView editIcon = new ImageView(editImage);
        editIcon.setFitWidth(25);
        editIcon.setFitHeight(25);
        editIcon.setOnMouseClicked(e -> editTask(task));
        return editIcon;
    }

    /**
     * Metod som skapar en ImageView med setOnMouseClicked effekter (för att radera en task)
     * @param task
     * @return
     */
    private ImageView createDeleteIcon(Task task) {
        Image deleteImage = new Image("file:src/main/resources/image/delete.png");
        ImageView deleteIcon = new ImageView(deleteImage);
        deleteIcon.setFitWidth(25);
        deleteIcon.setFitHeight(25);
        deleteIcon.setOnMouseClicked(e -> deleteTask(task));
        return deleteIcon;
    }

    /**
     * Metod som skapar en ImageView med setOnMouseClicked effekter (för att markera avklarad task)
     * @param task
     * @param taskTitle
     * @param taskDescription
     * @return
     */
    private ImageView createCheckIcon(Task task, Text taskTitle, Text taskDescription) {
        Image checkImage = new Image("file:src/main/resources/image/check.png");
        ImageView checkIcon = new ImageView(checkImage);
        checkIcon.setFitWidth(25);
        checkIcon.setFitHeight(25);
        checkIcon.setOnMouseClicked(e -> {
            task.setChecked(!task.isChecked());
            taskTitle.setStrikethrough(task.isChecked());
            taskDescription.setStrikethrough(task.isChecked());
            taskService.updateTaskStatus(task.getId(), task.isChecked());
        });
        return checkIcon;
    }

    /**
     * Metod som skappar en ny HBox med bildikoner
     * @param editIcon
     * @param deleteIcon
     * @param checkIcon
     * @return
     */
    private HBox createButtonsBox(ImageView editIcon, ImageView deleteIcon, ImageView checkIcon) {
        HBox buttonsBox = new HBox(10, editIcon, deleteIcon, checkIcon);
        return buttonsBox;
    }

    /**
     * Metod som skapar en TextInputDialog och ber användaren mata in ny title för ett Task-Objekt
     * Använder Optional för att undvika NullPointerException om användaren inte ändrar något
     * @param task
     * @return
     */
    private String getNewTitle(Task task) {
        TextInputDialog titleDialog = new TextInputDialog(task.getTitle());
        titleDialog.setTitle("Edit Task");
        titleDialog.setHeaderText("Edit Task Title");
        titleDialog.setContentText("Enter new title:");

        Optional<String> newTitle = titleDialog.showAndWait();
        return newTitle.orElse(task.getTitle());
    }

    /**
     * Metod som skapar en TextInputDialog och ber användaren mata in ny beskrivning för ett Task-Objekt
     * Använder Optional för att undvika NullPointerException om användaren inte ändrar något
     * @param task
     * @return
     */
    private String getNewDescription(Task task) {
        TextInputDialog descriptionDialog = new TextInputDialog(task.getDescription());
        descriptionDialog.setTitle("Edit Task");
        descriptionDialog.setHeaderText("Edit Task Description");
        descriptionDialog.setContentText("Enter new description:");

        Optional<String> newDescription = descriptionDialog.showAndWait();
        return newDescription.orElse(task.getDescription());
    }

    /**
     * Metod som skapar en ChoiceDialog och ber användaren välja en ny typ(morning, afternoon, evening) för ett Task-Objekt
     * Använder Optional för att undvika NullPointerException om användaren inte ändrar något
     * @param task
     * @return
     */
    private String getNewType(Task task) {
        ChoiceDialog<String> typeDialog = new ChoiceDialog<>(task.getType(), "Morning", "Afternoon", "Evening");
        typeDialog.setTitle("Edit Task");
        typeDialog.setHeaderText("Edit Task Type");
        typeDialog.setContentText("Select a new type:");

        Optional<String> newType = typeDialog.showAndWait();
        return newType.orElse(task.getType());
    }

    /**
     * Metod som uppdaterar utskriften av ett Task-objekt efter en edit av Task-objektet
     * @param task
     * @param newTitle
     * @param newDescription
     * @param newType
     */
    private void updateTaskFields(Task task, String newTitle, String newDescription, String newType) {
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setType(newType);
    }

    /**
     * Metod som skapar ett nytt Alert objekt som ber användaren bekräfta borttagning av en Task
     * @return true om användaren bekräftar borttagningen, annars false
     */
    private boolean showDeleteConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText("Are you sure you want to delete this task?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Metod som skapar en popup-fönsterruta med inhämtad titel, meddelande och typ
     * @param title
     * @param message
     * @param type
     */
    public void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}