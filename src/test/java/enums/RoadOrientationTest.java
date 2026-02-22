package enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @ParameterizedTest
    @CsvSource({
            // LaneDirection, StartRoadOrientation, Expected EndRoadOrientation
            "STRAIGHT, BOTTOM, TOP",
            "STRAIGHT, TOP, BOTTOM",
            "STRAIGHT, LEFT, RIGHT",
            "STRAIGHT, RIGHT, LEFT",

            "RIGHT, BOTTOM, RIGHT",
            "RIGHT, RIGHT, TOP",
            "RIGHT, TOP, LEFT",
            "RIGHT, LEFT, BOTTOM",

            "LEFT, BOTTOM, LEFT",
            "LEFT, LEFT, TOP",
            "LEFT, TOP, RIGHT",
            "LEFT, RIGHT, BOTTOM"
    })
    void findEndRoadOrientationBaseOnStartAndLaneDirectionTest(
            LaneDirection laneDirection,
            RoadOrientation startRoadOrientation,
            RoadOrientation expectedRoadOrientation
    ) {
        // When
        Optional<RoadOrientation> resultRoadOrientation =
                RoadOrientation.findRoadOrientationBaseOnStartAndLaneDirection(laneDirection, startRoadOrientation);

        // Then
        assertTrue(resultRoadOrientation.isPresent());
        assertEquals(expectedRoadOrientation, resultRoadOrientation.get());
    }

    @Test
    void giveBackwardLaneDirection_whenFindEndRoadDirection_thenReturnEmptyOptional() {
        // Given
        RoadOrientation startRoadOrientation = RoadOrientation.BOTTOM;

        // When
        Optional<RoadOrientation> resultRoadOrientation =
                RoadOrientation.findRoadOrientationBaseOnStartAndLaneDirection(LaneDirection.BACKWARD, startRoadOrientation);

        // Then
        assertTrue(resultRoadOrientation.isEmpty());
    }
}
