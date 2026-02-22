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
                () -> new LaneConfig(laneDirection, 0)
        );

        // Check exception message
        assertEquals("LaneDirection wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveNullHasPriority_whenParsingLaneConfig_thenThrowException() {
        // Given
        LaneDirection laneDirection = LaneDirection.STRAIGHT;
        Integer hasPriority = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LaneConfig(laneDirection, hasPriority)
        );

        // Check exception message
        assertEquals("HasPriority wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveWrongPriority_whenParsingLaneConfig_thenThrowException() {
        // Given
        LaneDirection laneDirection = LaneDirection.STRAIGHT;
        Integer hasPriority = 2;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LaneConfig(laneDirection, hasPriority)
        );

        // Check exception message
        assertEquals("HasPriority need to be value 0 (low priority) or 1 (high priority)!", exception.getMessage());
    }
}
