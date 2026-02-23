package org.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the final result of the simulation, containing the outcome of each step.
 * @param vehiclesLeft A list containing the status and results of each simulation step.
 */
public record SimulationResultDto(
        @JsonProperty("stepStatutes")
        List<StepStatusDto> vehiclesLeft
) {
}
