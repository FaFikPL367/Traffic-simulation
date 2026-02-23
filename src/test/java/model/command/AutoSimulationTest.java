package model.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.Simulation;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.command.AddVehicle;
import org.project.model.command.AutoSimulation;
import org.project.model.command.CommandType;
import org.project.model.command.Step;
import org.project.model.config.CommandConfig;
import org.project.model.config.SimulationConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Class with tests - AutoSimulation
 */
@ExtendWith(MockitoExtension.class)
public class AutoSimulationTest {

    @ParameterizedTest
    @CsvSource({
            // numberOfSimulationSteps, minNumberOfVehicleToGenerate, maxNumberOfVehicleToGenerate, minSimulationStepInRow, maxSimulationStepInRow
            "0, 1, 2, 3, 4",
            "1, 0, 2, 3, 4",
            "1, 2, 0, 3, 4",
            "1, 2, 3, 0, 4",
            "1, 2, 3, 4, 0",

            "5, 3, 2, 3, 4",

            "5, 1, 2, 4, 3",

            "5, 1, 2, 9, 10"
    })
    void giveWrongAutoSimulationParameters_whenCreatingAutoSimulationCommand_thenReturnException(
            Integer numberOfSimulationSteps,
            Integer minNumberOfVehicleToGenerate,
            Integer maxNumberOfVehicleToGenerate,
            Integer minSimulationStepInRow,
            Integer maxSimulationStepInRow
    ) {
        // When & Then
        assertThrows(IllegalArgumentException.class,
            () -> new AutoSimulation(
                    numberOfSimulationSteps, minNumberOfVehicleToGenerate, maxNumberOfVehicleToGenerate,
                    minSimulationStepInRow, maxSimulationStepInRow
            )
        );
    }

    @Mock
    private Simulation simulationMock;

    @Mock
    private Junction junctionMock;

    @Mock
    private SimulationConfig simulationConfigMock;

    @Mock
    private CommandConfig commandConfigMock;

    @Test
    void giveCorrectParameters_whenCreatingAutoSimulationCommand_thenReturnCorrectCommandList() {
        // Create example lane
        Lane lane = new Lane(1, RoadOrientation.BOTTOM, LaneDirection.STRAIGHT, 0);

        // Set parameters for autoSimulation
        Integer numberOfSimulationSteps = 15;
        Integer minNumberOfVehicleToGenerate = 3;
        Integer maxNumberOfVehicleToGenerate = 8;
        Integer minSimulationStepInRow = 5;
        Integer maxSimulationStepInRow = 8;

        // Mock some methods
        when(junctionMock.allLanes()).thenReturn(List.of(lane));
        when(simulationMock.getJunction()).thenReturn(junctionMock);

        List<CommandType> commandList = new ArrayList<>();
        when(simulationMock.getSimulationConfig()).thenReturn(simulationConfigMock);
        when(simulationMock.getSimulationConfig().commandConfig()).thenReturn(commandConfigMock);
        when(simulationMock.getSimulationConfig().commandConfig().commandList()).thenReturn(commandList);

        // When
        AutoSimulation autoSimulation = new AutoSimulation(
                numberOfSimulationSteps,
                minNumberOfVehicleToGenerate,
                maxNumberOfVehicleToGenerate,
                minSimulationStepInRow,
                maxSimulationStepInRow
        );
        autoSimulation.execute(simulationMock);

        // Then
        assertTrue(checkIfRangesAreCorrect(commandList, maxNumberOfVehicleToGenerate, maxSimulationStepInRow));
        assertEquals((long) numberOfSimulationSteps, countAllStepCommand(commandList));
    }

    /**
     * Checks if the sequences of generated commands are within the specified maximum limits.
     * @param commandList The list of generated commands to check.
     * @param addVehicleMax The maximum allowed number of generated vehicles in a sequence.
     * @param stepMax The maximum allowed number of generated step commands in a sequence.
     * @return TRUE if the ranges are valid, and FALSE otherwise.
     */
    private boolean checkIfRangesAreCorrect(List<CommandType> commandList, int addVehicleMax, int stepMax) {
        // Variable to count how many command in a row
        int countStep = stepMax;
        int countAddVehicle = addVehicleMax;

        // Go through each command
        for (CommandType command : commandList) {
            switch (command) {
                case AddVehicle _ -> {
                    if (countStep < 0) return false;
                    else countStep = stepMax;

                    countAddVehicle--;
                }
                case Step _ -> {
                    if (countAddVehicle < 0) return false;
                    else countAddVehicle = addVehicleMax;

                    countStep--;
                }
                default -> throw new IllegalStateException("Unexpected value: " + command);
            }
        }

        return true;
    }

    /**
     * Counts all Step command in the command list.
     * @param commandList The list containing all generated commands.
     * @return The number of Step commands.
     */
    private long countAllStepCommand(List<CommandType> commandList) {
        return commandList.stream().filter(command -> command.getClass() == Step.class)
                .count();
    }
}
