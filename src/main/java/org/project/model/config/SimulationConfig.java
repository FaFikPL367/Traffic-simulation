package org.project.model.config;

/**
 * Simulation config is a record that contains all necessary configuration for simulation
 *
 * @param junctionConfig
 * @param commandConfig
 */
public record SimulationConfig(
        JunctionConfig junctionConfig,
        CommandConfig commandConfig
) {
}
