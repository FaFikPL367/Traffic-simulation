package org.project.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.project.Simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Class that represents view for first page
 */
public class LoadSceneController {

    @FXML
    private Button inputFileButton;

    @FXML
    private Label inputFileLabel;

    @FXML
    private TextField outputFileTextField;

    @FXML
    private Button startSimulationButton;

    @FXML
    private Label errorMessageLabel;


    // Variable for files
    private String inputFileName;
    private String outputFileName;
    private String inputFilePath;

    /**
     * Method to handle input file choose
     */
    @FXML
    public void handleInputFileChoose(ActionEvent event) {
        // Create file chooser
        FileChooser fileChooser = new FileChooser();

        // Set title to file chooser
        fileChooser.setInitialFileName("Choose input JSON file");

        // Set extension to file chooser
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("All files", "*")
        );

        // Need to get main stage
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();

        // Waiting for user selection
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Save file information
            inputFileName = selectedFile.getName();
            inputFilePath = selectedFile.getPath();

            // Set name of file to label
            inputFileLabel.setText(inputFileName);
        } else {
            errorMessageLabel.setText("Couldn't load passed file!");
        }
    }

    @FXML
    public void handleStartSimulation(ActionEvent event) throws FileNotFoundException {
        // Get output file name
        outputFileName = outputFileTextField.getText().trim();

        // Check if input file and output file was provided
        if (inputFileName == null || outputFileName == null) {
            errorMessageLabel.setText("You need to pass input file and output file name!");
            return;
        }

        // Otherwise we can create a simulation object
        Simulation simulation = new Simulation(inputFileName, outputFileName);

        // Load main simulation view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/simulation_view.fxml"));

            Parent root = loader.load();

            SimulationController simulationController = loader.getController();

            simulationController.setSimulation(simulation);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root, 1000, 800);

            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
