package org.example.frontend.task;

public class MorningTask extends Task {
    /**
     * Konstruktor för MorningTask, som inhämtar egenskaper som sätts i super klassen (Task)
     * @param title
     * @param description
     * @param id
     * @param isChecked
     */
    public MorningTask(String title, String description, int id, boolean isChecked) {
        super(title, description, "Morning", id, isChecked);
    }
}
