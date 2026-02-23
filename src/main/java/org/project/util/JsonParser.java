package org.project.util;


import org.project.model.config.*;
import org.project.model.dto.SimulationResultDto;
import org.project.model.dto.StepStatusDto;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * A utility class responsible for parsing and generating JSON files for the simulation.
 */
public class JsonParser {

    // Attributes
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads the input JSON file and parses it into a SimulationConfig object.
     * @param inputFileName The path to the input JSON file.
     * @return The parsed simulation configuration.
     * @throws FileNotFoundException If the file is not found or cannot be parsed.
     */
    public SimulationConfig readInput(String inputFileName) throws FileNotFoundException {
        // Create object for file
        File input = new File(inputFileName);

        // Check if file exist
        if (!input.exists()) throw new FileNotFoundException("Input JSON file not found!");

        // Read junction config
        JunctionConfig junctionConfig = mapper.readValue(input, JunctionConfig.class);

        // Read commands
        CommandConfig commandConfig = mapper.readValue(input, CommandConfig.class);

        // Return simulation config
        return new SimulationConfig(junctionConfig, commandConfig);
    }

    /**
     * Writes the simulation results to an output JSON file.
     * @param outputFileName The path where the output JSON file will be saved.
     * @param passedVehicle A list containing the statuses of vehicles in each step.
     */
    public void writeOutput(String outputFileName, List<StepStatusDto> passedVehicle) {
        try {
            // Create output JSON file
            File output = new File(outputFileName);

            // Write first list
            mapper.writerWithDefaultPrettyPrinter().writeValue(output, new SimulationResultDto(passedVehicle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
