package org.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * LaneDirection is an enumeration of all possible direction of lane
 */
public enum LaneDirection {
    STRAIGHT, LEFT, RIGHT;

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
