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
 * Class for Lane mode in simulation
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
     * Method to count new lane wage
     */
    public void countNewWage() {
        this.laneWage = (Simulation.WAITING_TIME_WAGE * countVehicleWaitingTime()) + (Simulation.VEHICLE_COUNT_WAGE * getVehicleCount());
    }

    /**
     * Method to count vehicle
     * @return Number of vehicle on that lane
     */
    public int getVehicleCount() {
        return vehicles.size();
    }

    /**
     * Method to sum waiting times of all vehicles on that lane
     * @return Sum of waiting time
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
