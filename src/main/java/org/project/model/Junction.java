package org.project.model;

import org.project.enums.RoadOrientation;

import java.util.*;

/**
 * Represents the main junction model used in the simulation.
 * @param roadWithLanes A map associating each road orientation with its corresponding list of lanes.
 * @param allLanes A list containing all the lanes that exist within the junction.
 */
public record Junction(
        Map<RoadOrientation, List<Lane>> roadWithLanes,
        List<Lane> allLanes
) {
}
