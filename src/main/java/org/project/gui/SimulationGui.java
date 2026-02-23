package org.project.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main class to start simulation GUI
 */
public class SimulationGui extends Application {

    // Method to starts GUI
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Load FXML for first page
            URL fxmlFile = getClass().getResource("/fxml/load_scene_view.fxml");

            if (fxmlFile == null) {
                throw new IllegalStateException("Couldn't find load_scene_view.fxml file!");
            }

            // Load root
            Parent root = FXMLLoader.load(fxmlFile);

            // Create scene
            Scene scene = new Scene(root, 1000, 800);
            stage.setTitle("Simulation configuration");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


