package org.project;

import org.project.model.config.SimulationConfig;
import org.project.util.JsonParser;

import java.io.FileNotFoundException;

public class Simulation {

    // Attributes
    private String inputFileName;
    private String outputFileName;
    private SimulationConfig simulationConfig;
    private final JsonParser jsonParser = new JsonParser();

    // Constructor
    public Simulation(String inputFileName, String outputFileName) throws FileNotFoundException {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;

        // Read input file
        this.simulationConfig = jsonParser.readInput(inputFileName);

        System.out.println(simulationConfig);
    }
}
