package model;

import org.junit.jupiter.api.Test;
import org.project.enums.RoadOrientation;
import org.project.model.command.AddVehicle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with tests to - AddVehicle
 */
public class AddVehicleTest {

    @Test
    void giveNullStartRoadOrientation_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = null;
        RoadOrientation endRoadOrientation = RoadOrientation.TOP;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("StartRoadOrientation wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveNullEndRoadOrientation_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = RoadOrientation.RIGHT;
        RoadOrientation endRoadOrientation = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("EndRoadOrientation wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveSameStartAndEndRoadDirection_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = RoadOrientation.RIGHT;
        RoadOrientation endRoadOrientation = RoadOrientation.RIGHT;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("StartRoadOrientation and EndRoadOrientation can't be the same!", exception.getMessage());
    }
}
