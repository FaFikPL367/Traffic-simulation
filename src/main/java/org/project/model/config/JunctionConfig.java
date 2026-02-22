package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * JunctionConfig is a record that contains first part on input JSON file. It contains all information
 * needed to create own junction for simulation.
 *
 * @param roadConfigList Read config of how junction will be created
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
