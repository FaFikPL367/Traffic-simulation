package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.project.Simulation;
import org.project.util.TrafficController;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.model.Lane;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A command that executes a single step of the simulation.
 * @param id The unique ID of the simulation step.
 */
public record Step(
        int id
) implements CommandType {

    // Variable for ID generator
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    // Constructor
    @JsonCreator
    public Step() {
        this(ID_GENERATOR.getAndIncrement());
    }

    /**
     * Executes the simulation step by updating traffic lights and moving vehicles.
     * @param simulation The main simulation object.
     */
    @Override
    public void execute(Simulation simulation) {
        System.out.printf("Do STEP (%d) of simulation!%n", id);

        // Check if we need to pick another priority lane or continue with last one
        if (simulation.getMaxPassedVehicleOnMostImportantLane() == 0) {
            // We need to pick another lane from priority - first recalculate priority queue
            recalculatePriorityQueue(simulation);

            // Clear list with lanes that had green light
            simulation.getLastGreenLanes().clear();

            // Get most priority lane
            Lane priorityLane = simulation.getPriorityLanes().poll();
            simulation.getLastGreenLanes().add(priorityLane);
            priorityLane.setTimeInPriorityQueue(0);

            // Count backward lane for each road
            Map<RoadOrientation, Integer> backwardLaneCounter = recalculateBackwardLanes(simulation, priorityLane);

            // Find max number of vehicle on lanes that will have green light
            int maxVehicleNumber = priorityLane.getVehicleCount();

            // Go through other lanes in priority queue and check if there is any conflict with other lanes with green lights
            while (!simulation.getPriorityLanes().isEmpty()) {
                // Get other lane from priority queue
                Lane otherLane = simulation.getPriorityLanes().poll();

                // Check conflict with all lanes that will have green light
                boolean hasNoConflict = simulation.getLastGreenLanes()
                        .stream().noneMatch(greenLane -> greenLane.getLaneWithConflict().contains(otherLane));

                if (hasNoConflict && backwardLaneCounter.get(otherLane.getEndRoadOrientation()) != 0) {
                    simulation.getLastGreenLanes().add(otherLane);
                    backwardLaneCounter.computeIfPresent(otherLane.getEndRoadOrientation(), (_, val) -> val - 1);
                    maxVehicleNumber = Math.max(maxVehicleNumber, otherLane.getVehicleCount());

                    // Reset waiting time in queue for lane
                    otherLane.setTimeInPriorityQueue(0);
                } else {
                    // Other lane that won't have green light need to increment waiting time in queue
                    if (otherLane.getVehicleCount() != 0) otherLane.setTimeInPriorityQueue(otherLane.getTimeInPriorityQueue() + 1);
                }
            }

            // Set new max passed vehicle number (half of max vehicle number on green lanes)
            simulation.setMaxPassedVehicleOnMostImportantLane(
                    maxVehicleNumber / 2 == 0 ? 1 : maxVehicleNumber / 2
            );
        }

        // We continue with last priority lane
        TrafficController.continueVehicleDrive(simulation, id);
    }

    /**
     * Recalculates the priority queue for all active lanes.
     * @param simulation The main simulation object.
     */
    private void recalculatePriorityQueue(Simulation simulation) {
        // Filter all active lane (lane without BACKWARD)
        List<Lane> activesLane = simulation.getJunction().allLanes()
                        .stream().filter(lane -> lane.getLaneDirection() != LaneDirection.BACKWARD)
                        .toList();

        // Calculate wages on all actives lane
        activesLane
                .forEach(Lane::countNewWage);

        // Clear priority queue and add lanes again
        simulation.getPriorityLanes().clear();
        simulation.getPriorityLanes().addAll(activesLane);
    }

    /**
     * Calculates the number of available BACKWARD (receiving) lanes for each road.
     * @param simulation   The main simulation object.
     * @param priorityLane The lane that was selected as the highest priority in the current simulation step.
     * @return A map associating each road orientation with its count of available BACKWARD lanes.
     */
    private Map<RoadOrientation, Integer> recalculateBackwardLanes(Simulation simulation, Lane priorityLane) {
        Map<RoadOrientation, Integer> backwardLaneCounter = new HashMap<>();

        for (Map.Entry<RoadOrientation, List<Lane>> entry : simulation.getJunction().roadWithLanes().entrySet()) {
            RoadOrientation key = entry.getKey();
            List<Lane> values = entry.getValue();

            backwardLaneCounter.put(
                    key,
                    (int) values.stream().filter(l -> l.getLaneDirection() == LaneDirection.BACKWARD).count()
            );
        }

        // Delete available backward lane for priority lane (for it end direction)
        backwardLaneCounter.computeIfPresent(priorityLane.getEndRoadOrientation(), (_, val) -> val - 1);

        return backwardLaneCounter;
    }
}
