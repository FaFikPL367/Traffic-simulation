package org.project.util;

import org.project.enums.LaneDirection;
import org.project.model.Lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class that identifies conflicting traffic movements between lanes at a junction.
 */
public class ConflictFinder {
    /**
     * Finds all conflicting lanes for the specified lane.
     * @param lane The lane for which to find conflicts.
     * @param otherLanes A list of all lanes in the junction.
     * @return A list of conflicting lanes.
     */
    public static List<Lane> findConflictForLane(Lane lane, List<Lane> otherLanes) {
        // Find conflict base on a line direction
        return switch (lane.getLaneDirection()) {
            case STRAIGHT -> findConflictForStraightLane(lane, otherLanes);
            case LEFT -> findConflictLeftLane(lane, otherLanes);
            case RIGHT, BACKWARD -> Collections.emptyList();
        };
    }

    /**
     * Finds conflicting lanes for a specified lane that has a STRAIGHT direction.
     * @param importantLane The lane for which to find conflicts.
     * @param otherLanes A list of all other lanes.
     * @return A list containing the conflicting lanes.
     */
    private static List<Lane> findConflictForStraightLane(Lane importantLane, List<Lane> otherLanes) {
        // List for conflict lanes
        List<Lane> conflictLanes = new ArrayList<>();

        // Go through each other lane and check conflict
        for (Lane otherLane : otherLanes) {
            // Skip same lane that importantLane
            if (otherLane == importantLane) continue;

            // Straight cross lane
            if (otherLane.getLaneDirection() == LaneDirection.STRAIGHT &&
                otherLane.getStartRoadOrientation() != importantLane.getStartRoadOrientation() &&
                otherLane.getStartRoadOrientation() != importantLane.getEndRoadOrientation() &&
                otherLane.getEndRoadOrientation() != importantLane.getStartRoadOrientation() &&
                otherLane.getEndRoadOrientation() != importantLane.getEndRoadOrientation()
            )   conflictLanes.add(otherLane);

            // Left turn
            if (otherLane.getLaneDirection() == LaneDirection.LEFT && (
                    otherLane.getEndRoadOrientation() == importantLane.getStartRoadOrientation() ||
                    otherLane.getStartRoadOrientation() == importantLane.getEndRoadOrientation()
                )
            ) conflictLanes.add(otherLane);
        }

        // Return list
        return conflictLanes;
    }

    /**
     * Finds conflicting lanes for a specified lane that has a LEFT direction.
     * @param importantLane The lane for which to find conflicts.
     * @param otherLanes A list of all other lanes.
     * @return A list containing the conflicting lanes.
     */
    private static List<Lane> findConflictLeftLane(Lane importantLane, List<Lane> otherLanes) {
        // List for conflict lanes
        List<Lane> conflictLanes = new ArrayList<>();

        // Go through list with other lanes
        for (Lane otherLane : otherLanes) {
            // Other left turns
            if (otherLane.getLaneDirection() == LaneDirection.LEFT &&
                    (otherLane.getEndRoadOrientation() == importantLane.getStartRoadOrientation() ||
                     otherLane.getStartRoadOrientation() == importantLane.getEndRoadOrientation())
            ) conflictLanes.add(otherLane);

            // Straight lane that cross left turn
            if (otherLane.getLaneDirection() == LaneDirection.STRAIGHT && (
                otherLane.getEndRoadOrientation() == importantLane.getStartRoadOrientation() ||
                otherLane.getStartRoadOrientation() == importantLane.getEndRoadOrientation()
            )
            ) conflictLanes.add(otherLane);
        }

        // Return list
        return conflictLanes;
    }
}
