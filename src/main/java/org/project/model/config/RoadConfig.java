package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.RoadOrientation;

import java.util.List;

/**
 * Represents the configuration of a single road at the junction, including its lanes.
 * @param roadOrientation The orientation of the road at the junction (TOP, RIGHT, BOTTOM, or LEFT).
 * @param laneConfigList The list of configurations for the lanes on this road.
 */
public record RoadConfig(
        @JsonProperty("roadOrientation") RoadOrientation roadOrientation,
        @JsonProperty("lanes") List<LaneConfig> laneConfigList
        ) {

    // Constructor for validating read values
    public RoadConfig {
        if (roadOrientation == null) throw new IllegalArgumentException("RoadOrientation wasn't give or given value is incorrect!");
        if (laneConfigList == null) throw new IllegalArgumentException("LaneConfig wasn't given or given value is incorrect!");
        if (laneConfigList.isEmpty()) throw new IllegalArgumentException("LangConfig is an empty list!");
    }
}
