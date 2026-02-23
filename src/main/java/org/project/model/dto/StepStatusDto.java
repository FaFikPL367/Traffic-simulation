package org.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the status of a specific simulation step, detailing the vehicles that left the junction.
 * @param leftVehicles A list containing the IDs of the vehicles that exited the junction during this step.
 * @param stepId The unique ID of the simulation step.
 * @param totalNumberOfVehicleOnJunction The total number of vehicles remaining on the junction during this step.
 */
public record StepStatusDto(
        @JsonProperty("leftVehicles")  List<String> leftVehicles,
        @JsonProperty("stepId") int stepId,
        @JsonProperty("totalNumberOfVehicleOnJunction") int totalNumberOfVehicleOnJunction
) {
}
