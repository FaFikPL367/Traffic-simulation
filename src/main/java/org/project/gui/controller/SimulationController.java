package org.project.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import lombok.Setter;
import org.project.Simulation;
import org.project.gui.JunctionRender;

/**
 * Controller for main simulation view
 */
public class SimulationController {

    @FXML
    private Canvas canvas;

    // Main simulation object
    private Simulation simulation;

    // Method to set simulation
    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;

        JunctionRender.drawJunction(simulation.getJunction(), canvas);
    }
}
