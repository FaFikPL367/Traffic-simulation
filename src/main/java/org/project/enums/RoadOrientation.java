package org.project.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

/**
 * RoadOrientation is an enumeration that represents the possible orientations of roads at a junction.
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
     * Finds the destination road orientation based on the starting road orientation and lane direction.
     * @param laneDirection The direction of the lane that vehicle is currently on.
     * @param startRoadOrientation The starting road orientation for the vehicle.
     * @return An optional containing the destination RoadOrientation, or an empty optional if the lane direction is BACKWARD.
     */
    public static Optional<RoadOrientation> findRoadOrientationBaseOnStartAndLaneDirection(LaneDirection laneDirection, RoadOrientation startRoadOrientation) {
        return switch (laneDirection) {
            case LEFT -> Optional.of(roadOrientationAfterLeftTurn(startRoadOrientation));
            case RIGHT -> Optional.of(roadOrientationAfterRightTurn(startRoadOrientation));
            case STRAIGHT -> Optional.of(roadOrientationAfterStraight(startRoadOrientation));
            case BACKWARD -> Optional.empty();
        };
    }

    // Helper methods to find the correct road orientation based on the lane direction.
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
