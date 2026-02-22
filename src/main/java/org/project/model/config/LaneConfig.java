package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.LaneDirection;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LaneConfig record is for saving configuration of single lane
 * @param id ID of lane
 * @param laneDirection Direction of lane (LEFT, RIGHT, STRAIGHT, BACKWARD)
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
