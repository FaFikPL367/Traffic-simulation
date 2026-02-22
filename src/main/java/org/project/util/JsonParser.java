package org.project.util;


import org.project.model.config.*;
import org.project.model.dto.SimulationResultDto;
import org.project.model.dto.StepStatusDto;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class for methods used for parsing JSON files
 */
public class JsonParser {

    // Attributes
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method to read JSON input file
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
     * Method to write JSON output file
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
