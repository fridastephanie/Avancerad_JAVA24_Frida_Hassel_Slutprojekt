package org.example.frontend.task;

public class AfternoonTask extends Task {
    /**
     * Konstruktor för AfternoonTask, som inhämtar egenskaper som sätts i super klassen (Task)
     * @param title
     * @param description
     * @param id
     * @param isChecked
     */
    public AfternoonTask(String title, String description, int id, boolean isChecked) {
        super(title, description, "Afternoon", id, isChecked);
    }
}
