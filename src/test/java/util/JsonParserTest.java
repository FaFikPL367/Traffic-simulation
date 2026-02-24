package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.project.model.config.SimulationConfig;
import org.project.model.dto.StepStatusDto;
import org.project.util.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with test for - JsonParser
 */
public class JsonParserTest {

    // Variable for testing object
    private JsonParser jsonParser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        jsonParser = new JsonParser();
    }

    @Test
    void giveNotExistingFile_whenReadInput_thenThrowException() {
        // Given
        String notExistingFile = tempDir.resolve("input.json").toString();

        // When & Then
        assertThrows(FileNotFoundException.class,
                () -> jsonParser.readInput(notExistingFile)
        );
    }

    @Test
    void giveExistingFile_whenReadInput_thenReturnSimulationConfig() throws IOException {
        // Given
        Path existingFile = tempDir.resolve("input.json");

        // Write some correct JSON to file
        String jsonContent = """
                {
                    "junction": {
                        "roads": [
                            {
                                "roadOrientation": "BOTTOM",
                                "lanes": [
                                    {
                                        "laneDirection": "STRAIGHT",
                                        "hasPriority": 0
                                    }
                                ]
                            },
                            {
                                "roadOrientation": "TOP",
                                "lanes": [
                                    {
                                        "laneDirection": "BACKWARD",
                                        "hasPriority": 0
                                    }
                                ]
                            }
                        ]
                    },
                    "wages": {
                        "waitingTimeWage": 0.5,
                        "vehicleCountWage": 0.5
                    },
                    "command": {
                        "commandList": [
                            {
                                "type": "step"
                            }
                        ]
                    }
                }
                """;
        Files.writeString(existingFile, jsonContent);

        // When
        SimulationConfig simulationConfig = jsonParser.readInput(existingFile.toString());

        // Then
        assertNotNull(simulationConfig);
        assertNotNull(simulationConfig.commandConfig());
        assertNotNull(simulationConfig.junctionConfig());
        assertNotNull(simulationConfig.wagesConfig());
    }

    @Test
    void giveCorrectSimulationOutput_whenWriteOutput_thenCreateOutputFile() throws IOException {
        // Given
        Path outputFile = tempDir.resolve("output.json");
        List<StepStatusDto> passedVehicle = List.of(
                new StepStatusDto(List.of("car1", "car2"), 1, 0)
        );

        // When
        jsonParser.writeOutput(outputFile.toString(), passedVehicle);

        // Then
        assertTrue(Files.exists(outputFile));

        String outputResult = Files.readString(outputFile);
        assertTrue(outputResult.contains("car1"));
        assertTrue(outputResult.contains("car2"));
    }

    @Test
    void giveEmptyInputFile_whenReadInput_thenThrowException() throws IOException {
        // Given
        Path existingFile = tempDir.resolve("input.json");

        String fileContent = "";
        Files.writeString(existingFile, fileContent);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonParser.readInput(existingFile.toString())
        );

        // Check exception message
        assertEquals("The structure of the input file is wrong - No content to map due to end-of-input", exception.getMessage());
    }
}
