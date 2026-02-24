package org.project.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the root configuration required for the simulation.
 * @param junctionConfig The junction configuration parsed from the input JSON file.
 * @param commandConfig The command configuration parsed from the input JSON file.
 * @param wagesConfig The wages configuration parsed from the input JSON file.
 */
public record SimulationConfig(
        @JsonProperty("junction") JunctionConfig junctionConfig,
        @JsonProperty("command") CommandConfig commandConfig,
        @JsonProperty("wages") WagesConfig wagesConfig
) {

    @JsonCreator
    public SimulationConfig {
        if (junctionConfig == null || commandConfig == null || wagesConfig == null) {
            throw new IllegalArgumentException("Cannot read configurations from input file!");
        }
    }
}
