package org.project.model;

import org.project.enums.RoadOrientation;

import java.util.*;

/**
 * Class for Junction model in simulation
 *
 * @param roadWithLanes Map that represents list of lanes for each road orientation
 * @param allLanes List with all created lanes
 */
public record Junction(
        Map<RoadOrientation, List<Lane>> roadWithLanes,
        List<Lane> allLanes
) {
}
