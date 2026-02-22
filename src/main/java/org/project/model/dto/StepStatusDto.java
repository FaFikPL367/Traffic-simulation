package org.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * StepStatus is a record that contains vehicle that left junction on particular simulation step
 * @param leftVehicles List with IDs of vehicle that left junction on particular step
 */
public record StepStatusDto(
        @JsonProperty("leftVehicles")
        List<String> leftVehicles
) {
}
