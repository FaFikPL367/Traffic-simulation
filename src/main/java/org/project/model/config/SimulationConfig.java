package org.project.model.config;

/**
 * Simulation config is a record that contains all necessary configuration for simulation
 *
 * @param junctionConfig Read config about junction from input JSON file
 * @param commandConfig Read commands from input JSON file
 */
public record SimulationConfig(
        JunctionConfig junctionConfig,
        CommandConfig commandConfig
) {
}
