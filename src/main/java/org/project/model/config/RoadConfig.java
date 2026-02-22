package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.enums.RoadOrientation;

import java.util.List;

/**
 * RoadConfig is a record that contains information about road ot junction and lanes in that road
 *
 * @param roadOrientation Road orientation on junction (TOP, RIGHT, BOTTOM, LEFT)
 * @param laneConfigList List with information about lanes in this road
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
