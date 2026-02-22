package org.project;

import org.project.model.Lane;
import org.project.model.Vehicle;
import org.project.model.dto.StepStatusDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains methods to move vehicles at junction, increment waiting time
 */
public class TrafficController {

    public static void continueVehicleDrive(Simulation simulation) {
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
        simulation.getPassedVehicle().add(new StepStatusDto(passedVehicle));

        // Decrement max passed vehicle on this cycle
        simulation.decrementMaxPassedVehicle();
    }


    /**
     * Method to increment waiting time of every vehicle that were left on junction
     * @param simulation Main simulation object
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
