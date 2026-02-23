package org.project.util;

import org.project.Simulation;
import org.project.model.Lane;
import org.project.model.Vehicle;
import org.project.model.dto.StepStatusDto;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class responsible for moving vehicles through the junction and managing their waiting times.
 */
public class TrafficController {

    /**
     * Executes the movement of vehicles on lanes with a green light and updates simulation statistics.
     * @param simulation The main simulation object.
     * @param stepId The ID of the current simulation step.
     */
    public static void continueVehicleDrive(Simulation simulation, int stepId) {
        // List for vehicle that left junction on this step
        List<String> passedVehicle = new ArrayList<>();

        // Go through each lane with green light and pass single vehicle
        for (Lane greenLane : simulation.getLastGreenLanes()) {
            // Delete first car in lane
            Vehicle tmpVehicle = greenLane.getVehicles().poll();
            if (tmpVehicle != null) passedVehicle.add(String.valueOf(tmpVehicle.getId()));
        }

        // Increment waiting time for every vehicle that left on junction
        incrementVehicleWaitingTime(simulation);

        // Add all passed vehicle to simulation result
        simulation.getPassedVehicle().add(new StepStatusDto(
                passedVehicle,
                stepId,
                simulation.getJunction().allLanes().stream()
                        .mapToInt(Lane::getVehicleCount)
                        .sum()
                )
        );

        // Decrement max passed vehicle on this cycle
        simulation.decrementMaxPassedVehicle();
    }


    /**
     * Increments the waiting time for every vehicle remaining at the junction.
     * @param simulation The main simulation object.
     */
    private static void incrementVehicleWaitingTime(Simulation simulation) {
        // Go through each lane ane each vehicle on it
        for (Lane lane : simulation.getJunction().allLanes()) {
            // Increment waiting time
            lane.getVehicles()
                    .forEach(Vehicle::incrementWaitingTime);
        }
    }
}
