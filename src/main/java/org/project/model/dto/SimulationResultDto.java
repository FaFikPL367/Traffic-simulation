package org.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * SimulationResult is a record that contains result of each simulation step
 * @param vehiclesLeft List with results of each simulation step
 */
public record SimulationResultDto(
        @JsonProperty("stepStatutes")
        List<StepStatusDto> vehiclesLeft
) {
}
