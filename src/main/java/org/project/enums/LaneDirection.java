package org.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * LaneDirection is an enumeration of all possible directions for a lane.
 * STRAIGHT - a lane for going straight.
 * LEFT - a lane for turning left.
 * RIGHT - a lane for turning right.
 * BACKWARD - a receiving lane that vehicle from other roads can turn into.
 */
public enum LaneDirection {
    STRAIGHT, LEFT, RIGHT, BACKWARD;

    @JsonCreator
    public static LaneDirection fromString(String value) {
        try {
            return LaneDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Passed lane direction is unknown: " + value + ". Permitted values are: " + Arrays.toString(values())
            );
        }
    }
}
