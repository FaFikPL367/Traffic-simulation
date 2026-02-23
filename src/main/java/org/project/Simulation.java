package org.project;

import lombok.Getter;
import lombok.Setter;
import org.project.factory.JunctionFactory;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.command.CommandType;
import org.project.model.config.SimulationConfig;
import org.project.model.dto.StepStatusDto;
import org.project.util.JsonParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Main simulation class
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

    // PriorityQueue where will be added lanes with high priority (waiting time for vehicle is MAX or wage is the biggest)
    private PriorityQueue<Lane> priorityLanes = new PriorityQueue<>(LANE_COMPARATOR);

    private int maxPassedVehicleOnMostImportantLane = 0;
    private List<Lane> lastGreenLanes = new ArrayList<>();
    private List<StepStatusDto> passedVehicle = new ArrayList<>();

    // Constant values
    public static final double WAITING_TIME_WAGE = 0.8;
    public static final double VEHICLE_COUNT_WAGE = 0.2;
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
     * Method to run simulation
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

    public void decrementMaxPassedVehicle() {
        maxPassedVehicleOnMostImportantLane--;
    }
}
