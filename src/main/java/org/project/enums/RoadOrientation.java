package org.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

/**
 * RoadOrientation is an enumeration that contains possible orientation of roads at junction.
 *
 *     | T |
 *     | O |
 * ____| P |_____
 * LEFT     RIGHT
 * ¯¯¯¯| B |¯¯¯¯¯
 *     | O |
 *     | T |
 *     | T |
 *     | O |
 *     | M |
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

    /**
     * Methods that find end road orientation for lane base on start road orientation and lane direction
     * @param laneDirection Value with lane direction that vehicle is on
     * @param startRoadOrientation Start road orientation for vehicle
     * @return EndRoadOrientation base on lane direction and start road direction
     */
    public static Optional<RoadOrientation> findRoadOrientationBaseOnStartAndLaneDirection(LaneDirection laneDirection, RoadOrientation startRoadOrientation) {
        return switch (laneDirection) {
            case LEFT -> Optional.of(roadOrientationAfterLeftTurn(startRoadOrientation));
            case RIGHT -> Optional.of(roadOrientationAfterRightTurn(startRoadOrientation));
            case STRAIGHT -> Optional.of(roadOrientationAfterStraight(startRoadOrientation));
            case BACKWARD -> Optional.empty();
        };
    }

    // Helper methods to find correct road orientation for lane direction
    private static RoadOrientation roadOrientationAfterRightTurn(RoadOrientation roadOrientation) {
        return switch (roadOrientation) {
            case RIGHT -> TOP;
            case TOP -> LEFT;
            case LEFT -> BOTTOM;
            case BOTTOM -> RIGHT;
        };
    }

    private static RoadOrientation roadOrientationAfterStraight(RoadOrientation roadOrientation) {
        return switch (roadOrientation) {
            case RIGHT -> LEFT;
            case TOP -> BOTTOM;
            case LEFT -> RIGHT;
            case BOTTOM -> TOP;
        };
    }

    private static RoadOrientation roadOrientationAfterLeftTurn(RoadOrientation roadOrientation) {
        return switch (roadOrientation) {
            case RIGHT -> BOTTOM;
            case TOP -> RIGHT;
            case LEFT -> TOP;
            case BOTTOM -> LEFT;
        };
    }
}
