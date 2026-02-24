package model.config;

import org.junit.jupiter.api.Test;
import org.project.model.config.WagesConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for - WagesConfig
 */
public class WagesConfigTest {

    @Test
    void giveNullWages_whenParsingWagesConfig_thenThrowException() {
        // Given
        Double waitingTimeWage = 0.5;
        Double vehicleCountWage = 0.5;

        // When & Then
        IllegalArgumentException waitingTimeWageNullException = assertThrows(
                IllegalArgumentException.class,
                () -> new WagesConfig(null, vehicleCountWage)
        );

        IllegalArgumentException vehicleCountWageNullException = assertThrows(
                IllegalArgumentException.class,
                () -> new WagesConfig(waitingTimeWage, null)
        );

        // Check exception message
        assertEquals("The wage values were not provided in the input file!", waitingTimeWageNullException.getMessage());
        assertEquals("The wage values were not provided in the input file!", vehicleCountWageNullException.getMessage());
    }

    @Test
    void giveWagesNotInRage_whenParsingWagesConfig_thenThrowException() {
        // Given
        Double waitingTimeWage = -0.1;
        Double vehicleCountWage = 1.01;

        // When & Then
        IllegalArgumentException waitingTimeWageException = assertThrows(
                IllegalArgumentException.class,
                () -> new WagesConfig(waitingTimeWage, vehicleCountWage)
        );

        IllegalArgumentException vehicleCountWageException = assertThrows(
                IllegalArgumentException.class,
                () -> new WagesConfig(waitingTimeWage, vehicleCountWage)
        );

        // Check exception message
        assertEquals("Both wages must be within range of 0.0 to 1.0!", waitingTimeWageException.getMessage());
        assertEquals("Both wages must be within range of 0.0 to 1.0!", vehicleCountWageException.getMessage());
    }

    @Test
    void giveSumOfWagesAboveOne_whenParsingWagesConfig_thenThrowException() {
        // Given
        Double waitingTimeWage = 0.5;
        Double vehicleCountWage = 0.7;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WagesConfig(waitingTimeWage, vehicleCountWage)
        );


        // Check exception message
        assertEquals("The sum of the provided wages must be less than or equal to 1.0!", exception.getMessage());
    }
}
