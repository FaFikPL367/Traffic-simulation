package enums;

import org.junit.jupiter.api.Test;
import org.project.enums.RoadOrientation;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoadOrientationTest {

    @Test
    void giveWrongRoadOrientation_whenParsingRoadOrientation_thenThrowException() {
        // Given
        String value = "BLA";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> RoadOrientation.fromString(value)
        );

        // Check exception message
        assertEquals("Passed road orientation is unknown: " + value + ". Permitted values are: " + Arrays.toString(RoadOrientation.values()),
                exception.getMessage());
    }

    @Test
    void giveCorrectRoadOrientation_whenParsingRoadOrientation_ThenReturnRoadOrientation() {
        // Given
        String value = "BOTTOM";

        // When
        RoadOrientation actualRoadOrientation = RoadOrientation.fromString(value);

        // Then
        RoadOrientation expectedRoadOrientation = RoadOrientation.BOTTOM;
        assertEquals(expectedRoadOrientation, actualRoadOrientation);
    }
}
