package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.LaneDirection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the configuration of a single lane parsed from the input file.
 * @param id The unique ID of the lane.
 * @param laneDirection The directional type of the lane (LEFT, RIGHT, STRAIGHT, or BACKWARD).
 * @param priority The priority level of the lane (0 for low priority, 1 for high priority).
 */
public record LaneConfig(
        int id,
        LaneDirection laneDirection,
        Integer priority
) {

    // Variable for ID generator
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    // Constructor for validating read values
    @JsonCreator
    public LaneConfig(
            @JsonProperty("laneDirection") LaneDirection laneDirection,
            @JsonProperty("hasPriority") Integer hasPriority
    ) {
        if (laneDirection == null) throw new IllegalArgumentException("LaneDirection wasn't given or given value is incorrect!");
        if (hasPriority == null) throw new IllegalArgumentException("HasPriority wasn't given or given value is incorrect!");
        if (hasPriority != 0 && hasPriority != 1) throw new IllegalArgumentException("HasPriority need to be value 0 (low priority) or 1 (high priority)!");

        // Set attributes
        this(ID_GENERATOR.getAndIncrement(), laneDirection, hasPriority);
    }
}
