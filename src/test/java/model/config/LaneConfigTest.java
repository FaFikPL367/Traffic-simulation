package model.config;

import org.junit.jupiter.api.Test;
import org.project.enums.LaneDirection;
import org.project.model.config.LaneConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with tests for - LaneConfig
 */
public class LaneConfigTest {

    @Test
    void giveNullLaneDirection_whenParsingLaneConfig_thenThrowException() {
        // Given
        LaneDirection laneDirection = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LaneConfig(laneDirection, false)
        );

        // Check exception message
        assertEquals("LaneDirection wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveStraightLaneDirectionWithGreenArrow_whenParsingLaneConfig_thenFalseGreenArrow() {
        // Given
        LaneDirection laneDirection = LaneDirection.STRAIGHT;

        // When
        LaneConfig laneConfig = new LaneConfig(laneDirection, true);

        // Then
        boolean expectedGreenArrowBoolean = false;
        assertEquals(expectedGreenArrowBoolean, laneConfig.availableGreenArrow());
    }

    @Test
    void giveNullGreenArrow_whenParsingLaneConfig_thenThrowException() {
        // Given
        LaneDirection laneDirection = LaneDirection.STRAIGHT;
        Boolean availableGreenArrow = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LaneConfig(laneDirection, availableGreenArrow)
        );

        // Check exception message
        assertEquals("AvailableGreenArrow wasn't given or given value is incorrect!", exception.getMessage());
    }
}
