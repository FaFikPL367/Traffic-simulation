package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.project.Simulation;
import org.project.enums.LaneDirection;
import org.project.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A command that generate a random simulation sequence based on the provided parameters.
 * @param numberOfSimulationSteps The total number of simulation steps in the generated sequence.
 * @param minNumberOfVehicleToGenerate The minimum number of vehicles that can be generated in a single batch.
 * @param maxNumberOfVehicleToGenerate The maximum number of vehicles that can be generated in a single batch.
 * @param minSimulationStepInRow The minimum number of consecutive simulation steps.
 * @param maxSimulationStepInRow The maximum number of consecutive simulation steps.
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
     * Executes the command by generating a random sequence of vehicles and steps.
     * @param simulation The main simulation object.
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
     * Generates a random integer within the specified range (inclusive).
     * @return The generated integer number.
     */
    private int generateRandomNumberInRange(int min, int max) {
        return RANDOM.nextInt(max + 1 - min) + min;
    }

    /**
     * Generates AddVehicle commands and adds them to the command list.
     * @param commandList The list containing all generated commands.
     * @param generatedNumberOfVehicle The number of AddVehicle commands to generate.
     * @param simulation The main simulation object.
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
     * Generates Step commands and adds them to the command list.
     * @param commandList The list containing all generated commands.
     * @param generatedNumberOfSteps The number of Step commands to generate.
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
