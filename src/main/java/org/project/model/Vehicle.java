package org.project.model;

import lombok.Getter;
import org.project.enums.RoadOrientation;

/**
 * Class for Vehicle model in simulation
 */
@Getter
public class Vehicle {

    // Attributes
    private final int id;
    private final RoadOrientation startRoadDirection;
    private final RoadOrientation endRoadDirection;
    private int waitingTime = 0;

    // Constructor
    public Vehicle(int id, RoadOrientation startRoadDirection, RoadOrientation endRoadDirection) {
        this.id = id;
        this.startRoadDirection = startRoadDirection;
        this.endRoadDirection = endRoadDirection;
    }

    /**
     * Method to increment waiting time
     */
    public void incrementWaitingTime() {
        this.waitingTime++;
    }

    // ToString
    @Override
    public String toString() {
        return "Vehicle (%d) (%s, %s)".formatted(id, startRoadDirection, endRoadDirection);
    }
}
