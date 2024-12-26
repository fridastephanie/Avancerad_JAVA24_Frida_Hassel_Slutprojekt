package org.example.frontend.task;

public abstract class Task {
    //Referensvariabel för Tasks id
    private int id;
    //Referensvaribel för Tasks titel
    private String title;
    //Referensvariabel för Tasks beskrivning
    private String description;
    //Referensvariabel för Tasks typ (morning/afternoon/evening)
    private String type;
    //Refernsvariabel för Tasks "avklarad-status"
    private boolean isChecked;

    /**
     * Konstruktor för Task som inhämtar och sätter värdena för id, title, description, type och isChecked
     * @param title
     * @param description
     * @param type
     * @param id
     * @param isChecked
     */
    public Task(String title, String description, String type, int id, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.isChecked = isChecked;
    }

    /**
     * Getter för id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Getter för title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter för description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter för type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Setter för title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter för description
     * @return
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Setter för type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter för isChecked
     * @return
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Setter för isChecked
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}