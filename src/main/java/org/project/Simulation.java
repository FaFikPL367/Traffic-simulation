package org.project;

import lombok.Getter;
import lombok.Setter;
import org.project.factory.JunctionFactory;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.command.CommandType;
import org.project.model.config.SimulationConfig;
import org.project.model.dto.StepStatusDto;
import org.project.util.ConflictFinder;
import org.project.util.JsonParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The core engine of the traffic simulation, managing the state, junction model, and execution of commands.
 */
@Getter
@Setter
public class Simulation {

    // Attributes
    private String inputFileName;
    private String outputFileName;
    private SimulationConfig simulationConfig;
    private Junction junction;
    private final JsonParser jsonParser = new JsonParser();

    // Simulation parameters
    private boolean countWagesAgain = true;
    private Lane mostImportantLane = null;

    /**
     * PriorityQueue to manage lanes based on their urgency.
     */
    private PriorityQueue<Lane> priorityLanes = new PriorityQueue<>(LANE_COMPARATOR);

    private int maxPassedVehicleOnMostImportantLane = 0;
    private List<Lane> lastGreenLanes = new ArrayList<>();
    private List<StepStatusDto> passedVehicle = new ArrayList<>();

    // Simulation constants
    public static final double WAITING_TIME_WAGE = 0.8;
    public static final double VEHICLE_COUNT_WAGE = 0.2;

    /**
     * Comparator for ordering lanes in the priority queue.
     * Priority is determined by:
     * 1. Time spent in priority queue
     * 2. Calculated lane weight (priority value)
     * 3. Static priority flag
     */
    public static final Comparator<Lane> LANE_COMPARATOR = Comparator
            .comparingInt(Lane::getTimeInPriorityQueue)
            .thenComparingDouble(Lane::getLaneWage)
            .thenComparingInt(Lane::getHasPriority)
            .reversed();

    // Constructor
    public Simulation(String inputFileName, String outputFileName) throws FileNotFoundException {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;

        // Read input file
        this.simulationConfig = jsonParser.readInput(inputFileName);

        // Create model of junction
        junction = JunctionFactory.createJunction(simulationConfig.junctionConfig());

        // Find conflict lanes for each lane
        junction.allLanes()
                .forEach(lane -> lane.getLaneWithConflict()
                        .addAll(ConflictFinder.findConflictForLane(lane, junction.allLanes())));

        // Add all created lanes to priority queue
        priorityLanes.addAll(junction.allLanes());
    }

    /**
     * Starts the simulation by executing all commands loaded from the configuration.
     * After execution, the results are saved to the output JSON file.
     */
    public void runSimulation() {
        // Copy list of commands
        List<CommandType> copyCommandList = new ArrayList<>(simulationConfig.commandConfig().commandList());

        // Read simulation command
        for (CommandType command : copyCommandList) {
            // Execute command
            command.execute(this);
        }

        // Create output JSON file
        jsonParser.writeOutput(outputFileName, passedVehicle);
    }

    /**
     * Decrements the counter for vehicles allowed to pass on the current priority lane.
     */
    public void decrementMaxPassedVehicle() {
        maxPassedVehicleOnMostImportantLane--;
    }
}
