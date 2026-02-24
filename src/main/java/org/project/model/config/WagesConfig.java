package org.project.model.config;


import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A configuration record containing wages that influence the calculation of lane priority.
 * @param waitingTimeWage The weight factor applied to the vehicle waiting time (0.0 to 1.0).
 * @param vehicleCountWage The weight factor applied to the total number of vehicles (0.0 to 1.0).
 */
public record WagesConfig(
        Double waitingTimeWage,
        Double vehicleCountWage
) {

    @JsonCreator
    public WagesConfig {
        if (waitingTimeWage == null || vehicleCountWage == null) {
            throw new IllegalArgumentException("The wage values were not provided in the input file!");
        }

        if (!(waitingTimeWage > 0.0 && waitingTimeWage < 1.0) || !(vehicleCountWage > 0.0 && vehicleCountWage < 1.0)) {
            throw new IllegalArgumentException("Both wages must be within range of 0.0 to 1.0!");
        }

        if (waitingTimeWage + vehicleCountWage > 1.0) {
            throw new IllegalArgumentException("The sum of the provided wages must be less than or equal to 1.0!");
        }
    }
}
