package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.Simulation;
import org.project.enums.RoadOrientation;
import org.project.model.Lane;
import org.project.model.Vehicle;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A command that adds a new vehicle to the simulation.
 * @param id The unique ID of added vehicle.
 * @param startRoadOrientation The road from which the vehicle enters the junction.
 * @param endRoadOrientation The destination road where the vehicle wants to go.
 */
public record AddVehicle(
        int id,
        RoadOrientation startRoadOrientation,
        RoadOrientation endRoadOrientation
        ) implements CommandType {

    // Variable for ID generator
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    @JsonCreator
    public AddVehicle(
            @JsonProperty("startRoadOrientation") RoadOrientation startRoadOrientation,
            @JsonProperty("endRoadOrientation") RoadOrientation endRoadOrientation
    ) {
        if (startRoadOrientation == null) throw new IllegalArgumentException(
                "StartRoadOrientation wasn't given or given value is incorrect!"
        );
        if (endRoadOrientation == null) throw new IllegalArgumentException(
                "EndRoadOrientation wasn't given or given value is incorrect!"
        );
        if (startRoadOrientation == endRoadOrientation) throw new IllegalArgumentException(
                "StartRoadOrientation and EndRoadOrientation can't be the same!"
        );

        // Set attributes
        this(ID_GENERATOR.getAndIncrement(), startRoadOrientation, endRoadOrientation);
    }

    /**
     * Executes the command by creating a vehicle and adding it to the appropriate lane in the junction.
     * @param simulation The current simulation instance.
     */
    @Override
    public void execute(Simulation simulation) {
        // Create object of vehicle
        Vehicle vehicle = new Vehicle(id, startRoadOrientation, endRoadOrientation);

        // Add vehicle to lane
        for (Lane lane : simulation.getJunction().allLanes()) {
            if (lane.getStartRoadOrientation() == startRoadOrientation && lane.getEndRoadOrientation() == endRoadOrientation) {
                // Add vehicle to lane
                lane.getVehicles().add(vehicle);

                // Calculate wage of lane and update priority queue
                simulation.getPriorityLanes().remove(lane);
                lane.countNewWage();
                simulation.getPriorityLanes().add(lane);

                System.out.printf("Vehicle (%d) added to lane (%s, %s, %s)!%n", id, lane.getStartRoadOrientation(), lane.getLaneDirection(), lane.getEndRoadOrientation());
                return;
            }
        }

        // There isn't any lane for vehicle
        throw new IllegalArgumentException("Vehicle (%s) can't put in declared lane for him, because this lane doesn't exist!".formatted(vehicle));
    }
}
