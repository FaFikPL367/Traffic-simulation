package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.project.Simulation;
import org.project.enums.LaneDirection;
import org.project.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AutoSimulation is a command to generate random simulation based on passed parameters
 * @param numberOfSimulationSteps Number of total simulation steps in random simulation
 * @param minNumberOfVehicleToGenerate Lower range number of vehicle that can be generated in single step
 * @param maxNumberOfVehicleToGenerate Higher range number of vehicle that can be generated in single step
 * @param minSimulationStepInRow Lower range number of steps that can be in a row
 * @param maxSimulationStepInRow Higher range number of steps that can be in a row
 */
public record AutoSimulation(
        Integer numberOfSimulationSteps,
        Integer minNumberOfVehicleToGenerate,
        Integer maxNumberOfVehicleToGenerate,
        Integer minSimulationStepInRow,
        Integer maxSimulationStepInRow
) implements CommandType {

    // Variable for RANDOM object
    private static final Random RANDOM = new Random();

    @JsonCreator
    public AutoSimulation {
        if (numberOfSimulationSteps == null || minNumberOfVehicleToGenerate == null || maxNumberOfVehicleToGenerate == null ||
            minSimulationStepInRow == null || maxSimulationStepInRow == null) {
            throw new IllegalArgumentException("One of the parameters wasn't provided!");
        }

        if (numberOfSimulationSteps <= 0 || minSimulationStepInRow <= 0 || maxSimulationStepInRow <= 0 ||
            minNumberOfVehicleToGenerate <= 0 || maxNumberOfVehicleToGenerate <= 0) {
            throw new IllegalArgumentException("None of passed parameters can be lower or equal to 0!");
        }

        if (minNumberOfVehicleToGenerate > maxNumberOfVehicleToGenerate) {
            throw new IllegalArgumentException("Minimum number of vehicle cannot be greater than max!");
        }

        if (minSimulationStepInRow > maxSimulationStepInRow) {
            throw new IllegalArgumentException("Minimum number of simulation steps cannot be greater than max!");
        }

        if (minSimulationStepInRow > numberOfSimulationSteps) {
            throw new IllegalArgumentException("Minimum number of simulation steps cannot be greater than total number of simulation steps!");
        }
    }

    /**
     * Method to execute command
     * @param simulation Main simulation object
     */
    @Override
    public void execute(Simulation simulation) {
        System.out.println("Generate commands to RANDOM simulation!");

        // Create list for commands
        List<CommandType> commandList = new ArrayList<>();

        // Copy numberOfSimulationSteps
        int remainingSteps = this.numberOfSimulationSteps;

        // In rotation, we generate vehicle and generate number of simulation steps
        boolean generateVehicle = true;
        while (remainingSteps != 0) {
            if (generateVehicle) {
                // Need to generate some vehicle
                int numberOfVehicle = generateRandomNumberInRange(
                        minNumberOfVehicleToGenerate,
                        maxNumberOfVehicleToGenerate
                );

                // Generate addVehicle commands
                generateAddVehicleCommands(
                        commandList,
                        numberOfVehicle,
                        simulation
                );

            } else {
                // Need to add some simulation steps
                int generatedNumberOfSimulationSteps = generateRandomNumberInRange(
                        minSimulationStepInRow,
                        maxSimulationStepInRow
                );

                // Check if generated value is smaller than general number of simulation steps
                if (generatedNumberOfSimulationSteps > remainingSteps) generatedNumberOfSimulationSteps = remainingSteps;

                // Generate Step commands
                generateStepCommands(
                        commandList,
                        generatedNumberOfSimulationSteps
                );

                // Decrement number of general simulation steps
                remainingSteps -= generatedNumberOfSimulationSteps;
            }

            // Toggle generateVehicle boolean
            generateVehicle = !generateVehicle;
        }

        // Set new command list to simulation
        simulation.getSimulationConfig().commandConfig().commandList()
                .clear();
        simulation.getSimulationConfig().commandConfig().commandList()
                .addAll(commandList);

        // Run simulation again
        simulation.runSimulation();
    }

    /**
     * Method to generate random INTEGER number in passed range
     * @return Generated INTEGER number
     */
    private int generateRandomNumberInRange(int min, int max) {
        return RANDOM.nextInt(max + 1 - min) + min;
    }

    /**
     * Method to generate addVehicle command and add it to command lst
     * @param commandList List of all commands
     * @param generatedNumberOfVehicle Number of addVehicle command that we need to generate
     */
    private void generateAddVehicleCommands(List<CommandType> commandList, int generatedNumberOfVehicle, Simulation simulation) {
        // Get copy of list with lanes but without BACKWARD lanes
        List<Lane> allLanesWithoutBackward = simulation.getJunction().allLanes()
                .stream().filter(lane -> lane.getLaneDirection() != LaneDirection.BACKWARD)
                .toList();

        while (generatedNumberOfVehicle != 0) {
            // Need to generate random lane that vehicle will be put on
            Lane randomLane = allLanesWithoutBackward
                    .get(RANDOM.nextInt(allLanesWithoutBackward.size()));

            // Create command - addVehicle
            AddVehicle addVehicle = new AddVehicle(
                    randomLane.getStartRoadOrientation(),
                    randomLane.getEndRoadOrientation()
            );

            // Add command to list
            commandList.add(addVehicle);

            // Decrement number of generated command
            generatedNumberOfVehicle--;
        }
    }

    /**
     * Method to generate step command and add it to command list
     * @param commandList List of all commands
     * @param generatedNumberOfSteps Number of step command that we need to generate
     */
    private void generateStepCommands(List<CommandType> commandList, int generatedNumberOfSteps) {
        while (generatedNumberOfSteps != 0) {
            // Create command - step
            Step step = new Step();

            // Add command to list
            commandList.add(step);

            // Decrement number of generated command
            generatedNumberOfSteps--;
        }
    }
}
