package org.example.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontendApplication extends Application {
    /**
     * Metod som sätter grundinställningarna för JavaFX-applikationen
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FrontendApplication.class.getResource("to-do-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 807, 783);
        stage.setTitle("To-Do List");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main som anropar metod som startar upp JavaFX-applikationen
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}