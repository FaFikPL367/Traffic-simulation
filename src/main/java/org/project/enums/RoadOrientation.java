package org.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * RoadOrientation is an enumeration that contains possible orientation of roads at junction.
 */
public enum RoadOrientation {
    TOP, RIGHT, BOTTOM, LEFT;

    @JsonCreator
    public static RoadOrientation fromString(String value) {
        try {
            return RoadOrientation.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Passed road orientation is unknown: " + value + ". Permitted values are: " + Arrays.toString(values())
            );
        }
    }
}
