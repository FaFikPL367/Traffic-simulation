package org.project.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.RoadOrientation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AddVehicle is a command to add vehicle to simulation
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
}
