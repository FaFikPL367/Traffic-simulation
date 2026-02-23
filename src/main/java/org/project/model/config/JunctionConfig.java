package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A configuration record representing the first part of the input JSON file.
 * It contains all the necessary information to build a custom junction for the simulation.
 *
 * @param roadConfigList The list of road configurations defining how the junction will be constructed.
 */
public record JunctionConfig(
    @JsonProperty("roads") List<RoadConfig> roadConfigList
) {

    // Constructor for validating read values
    public JunctionConfig {
        if (roadConfigList == null) throw new IllegalArgumentException("RoadConfig wasn't give in input file!");
        if (roadConfigList.isEmpty()) throw new IllegalArgumentException("RoadConfig is an empty list!");
    }
}
