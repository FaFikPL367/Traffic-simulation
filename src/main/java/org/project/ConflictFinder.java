package org.project;

import org.project.enums.LaneDirection;
import org.project.model.Lane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for methods that help find conflict in junction between lanes
 */
public class ConflictFinder {
    /**
     * Method to find all conflict lanes for passed lane
     * @param lane Lane that we want to find conflicts
     * @param otherLanes All lanes in junction
     * @return List with conflict lanes
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
     * Method to find conflict lanes for passed lane that has STRAIGHT direction
     *
     * @param importantLane Lane for which we want to find conflict
     * @param otherLanes All other lanes
     * @return List that contains lane with conflict
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
     * Method to find conflict lanes for passed lane that has LEFT direction
     *
     * @param importantLane Lane for which we want to find conflict
     * @param otherLanes All other lanes
     * @return List that contains lane with conflict
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
