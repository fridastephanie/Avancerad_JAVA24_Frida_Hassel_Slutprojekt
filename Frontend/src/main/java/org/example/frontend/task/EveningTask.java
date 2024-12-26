package org.example.frontend.task;

public class EveningTask extends Task {
    /**
     * Konstruktor för EveningTask, som inhämtar egenskaper som sätts i super klassen (Task)
     * @param title
     * @param description
     * @param id
     * @param isChecked
     */
    public EveningTask(String title, String description, int id, boolean isChecked) {
        super(title, description, "Evening", id, isChecked);
    }
}
