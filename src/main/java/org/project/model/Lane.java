package org.project.model;

import lombok.Getter;
import lombok.Setter;
import org.project.Simulation;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a single lane within the traffic simulation model.
 */
@Getter
@Setter
public class Lane {

    // Attributes
    private final int id;
    private final RoadOrientation startRoadOrientation;
    private final RoadOrientation endRoadOrientation;
    private final LaneDirection laneDirection;
    private Integer hasPriority;
    private Integer timeInPriorityQueue = 0;
    private final Queue<Vehicle> vehicles = new ArrayDeque<>();
    private final List<Lane> laneWithConflict = new ArrayList<>();
    private double laneWage = 0;

    // Constructor
    public Lane(int id, RoadOrientation startRoadOrientation, LaneDirection laneDirection, Integer hasPriority) {
        this.id = id;
        this.laneDirection = laneDirection;
        this.startRoadOrientation = startRoadOrientation;
        this.endRoadOrientation = RoadOrientation
                .findRoadOrientationBaseOnStartAndLaneDirection(laneDirection, startRoadOrientation)
                .orElse(null);
        this.hasPriority = hasPriority;
    }

    /**
     * Calculates and updates the priority weight of the lane.
     * The weight is based on the total waiting time and the current number of vehicles.
     */
    public void countNewWage() {
        this.laneWage = (Simulation.waitingTimeWage * countVehicleWaitingTime()) + (Simulation.vehicleCountWage * getVehicleCount());
    }

    /**
     * Counts vehicle on this lane.
     * @return The number of vehicles on this lane.
     */
    public int getVehicleCount() {
        return vehicles.size();
    }

    /**
     * Calculates the total sum of waiting times for all vehicles currently on this lane.
     * @return The sum of waiting times.
     */
    private int countVehicleWaitingTime() {
        return vehicles.stream()
                .mapToInt(Vehicle::getWaitingTime)
                .sum();
    }

    // ToString
    @Override
    public String toString() {
        return "LANE (%d) (%s, %s, %s), (wage: %f, priority: %d, added time: %d)".formatted(id, startRoadOrientation, laneDirection, endRoadOrientation, laneWage, hasPriority, timeInPriorityQueue);
    }
}
