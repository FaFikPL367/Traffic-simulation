package enums;

import org.junit.jupiter.api.Test;
import org.project.enums.LaneDirection;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LaneDirectionTest {
    @Test
    void giveWrongLaneDirection_whenParsingLaneDirection_thenThrowException() {
        // Given
        String value = "BLA";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> LaneDirection.fromString(value)
        );

        // Check exception message
        assertEquals("Passed lane direction is unknown: " + value + ". Permitted values are: " + Arrays.toString(LaneDirection.values()),
                exception.getMessage());
    }

    @Test
    void giveCorrectLaneDirection_whenParsingLaneDirection_ThenReturnLaneDirection() {
        // Given
        String value = "STRAIGHT";

        // When
        LaneDirection actualRoadOrientation = LaneDirection.fromString(value);

        // Then
        LaneDirection expectedRoadOrientation = LaneDirection.STRAIGHT;
        assertEquals(expectedRoadOrientation, actualRoadOrientation);
    }
}
