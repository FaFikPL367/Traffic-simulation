package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.LaneDirection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LaneConfig record is for saving configuration of single lane
 */
public record LaneConfig(
        int id,
        LaneDirection laneDirection,
        Boolean availableGreenArrow
) {

    // Variable for ID generator
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    // Constructor for validating read values
    @JsonCreator
    public LaneConfig(
            @JsonProperty("laneDirection") LaneDirection laneDirection,
            @JsonProperty("availableGreenArrow") Boolean availableGreenArrow
    ) {
        if (laneDirection == null) throw new IllegalArgumentException("LaneDirection wasn't given or given value is incorrect!");
        if (availableGreenArrow == null) throw new IllegalArgumentException("AvailableGreenArrow wasn't given or given value is incorrect!");

        // Green arrow on straight lane isn't available
        if (laneDirection == LaneDirection.STRAIGHT && availableGreenArrow) availableGreenArrow = false;

        // Set attributes
        this(ID_GENERATOR.getAndIncrement(), laneDirection, availableGreenArrow);
    }
}
