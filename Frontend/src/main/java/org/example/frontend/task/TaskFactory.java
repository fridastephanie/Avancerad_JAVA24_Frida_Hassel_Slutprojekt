package org.example.frontend.task;

public class TaskFactory {

    /**
     * Metod som skapar ett specifikt Task-objekt baserat på typ (morning, afternoon, evening)
     *
     * @param title
     * @param description
     * @param type
     * @param existingId
     * @param isChecked
     * @return
     */
    public static Task createTask(String title, String description, String type, int existingId, boolean isChecked) {
        switch (type.toLowerCase()) {
            case "morning":
                return new MorningTask(title, description, existingId, isChecked);
            case "afternoon":
                return new AfternoonTask(title, description, existingId, isChecked);
            case "evening":
                return new EveningTask(title, description, existingId, isChecked);
            default:
                return null;
        }
    }
}