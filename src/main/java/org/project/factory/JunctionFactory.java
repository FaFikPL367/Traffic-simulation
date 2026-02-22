package org.project.factory;

import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.config.JunctionConfig;
import org.project.model.config.LaneConfig;
import org.project.model.config.RoadConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JunctionFactory {

    public static Junction createJunction(JunctionConfig junctionConfig) {
        // Variables
        Map<RoadOrientation, List<Lane>> roadWithLanes = new HashMap<>();
        List<Lane> allLanes = new ArrayList<>();

        // Add all road orientation to map
        addRoadOrientation(junctionConfig.roadConfigList(), roadWithLanes);

        // Add lanes to roads
        addLanesToRoads(junctionConfig.roadConfigList(), roadWithLanes, allLanes);

        // Validate created lanes
        validateCreatedLanes(roadWithLanes, allLanes);

        // Create junction model
        return new Junction(roadWithLanes, allLanes);
    }

    /**
     * Method to add all passed road orientation in input file to map
     * @param roadConfigList Read config of how junction will be created
     * @param roadWithLanes Map that represents list of lanes for each road orientation
     */
    private static void addRoadOrientation(List<RoadConfig> roadConfigList, Map<RoadOrientation, List<Lane>> roadWithLanes) {
        for (RoadConfig roadConfig : roadConfigList) {
            // Check if road orientation is in map already
            if (roadWithLanes.containsKey(roadConfig.roadOrientation())) {
                throw new IllegalArgumentException("In input file RoadOrientation - %s - appear more than once!".formatted(roadConfig.roadOrientation()));
            }

            // Add road orientation
            roadWithLanes.put(roadConfig.roadOrientation(), new ArrayList<>());
        }
    }

    /**
     * Method to add all lanes to added roads
     * @param roadConfigList Read config of how junction will be created
     * @param roadWithLanes Map that represents list of lanes for each road orientation
     * @param allLanes List with all created lanes
     */
    private static void addLanesToRoads(List<RoadConfig> roadConfigList,
                                        Map<RoadOrientation, List<Lane>> roadWithLanes,
                                        List<Lane> allLanes) {
        for (RoadConfig roadConfig : roadConfigList) {
            for (LaneConfig laneConfig : roadConfig.laneConfigList()) {
                // Create lane
                Lane lane = new Lane(laneConfig.id(), roadConfig.roadOrientation(), laneConfig.laneDirection(), laneConfig.priority());

                // Add lane to road
                roadWithLanes.get(roadConfig.roadOrientation()).add(lane);
                allLanes.add(lane);
            }
        }
    }

    /**
     * Method to validate created lanes base on input file
     * @param roadWithLanes Map that represents list of lanes for each road orientation
     * @param allLanes List with all created lanes
     */
    private static void validateCreatedLanes(Map<RoadOrientation, List<Lane>> roadWithLanes, List<Lane> allLanes) {
        for (Lane lane : allLanes) {
            // Skip BACKWARD lane
            if (lane.getLaneDirection() == LaneDirection.BACKWARD) continue;

            // Check if end road direction exist
            if (!roadWithLanes.containsKey(lane.getEndRoadOrientation())) {
                throw new IllegalArgumentException("Lane (%s, %s) cannot be add, because junction doesn't contain end road for this lane!".formatted(lane.getStartRoadOrientation(), lane.getLaneDirection()));
            }

            // Check if end road has backward lane
            boolean hasBackwardLane = roadWithLanes.get(lane.getEndRoadOrientation()).stream()
                    .anyMatch(l -> l.getLaneDirection() == LaneDirection.BACKWARD);
            if (!hasBackwardLane) {
                throw new IllegalArgumentException("Lane (%s, %s) cannot be added, because end road doesn't have BACKWARD lane!".formatted(lane.getStartRoadOrientation(), lane.getLaneDirection()));
            }
        }
    }
}
