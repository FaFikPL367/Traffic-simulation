package org.project.model.config;

/**
 * Represents the root configuration required for the simulation.
 * @param junctionConfig The junction configuration parsed from the input JSON file.
 * @param commandConfig The command configuration parsed from the input JSON file.
 */
public record SimulationConfig(
        JunctionConfig junctionConfig,
        CommandConfig commandConfig
) {
}
