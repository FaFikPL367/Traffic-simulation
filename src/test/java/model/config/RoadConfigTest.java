package model.config;

import org.junit.jupiter.api.Test;
import org.project.enums.RoadOrientation;
import org.project.model.config.LaneConfig;
import org.project.model.config.RoadConfig;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with tests for - RoadConfig
 */
public class RoadConfigTest {

    @Test
    void giveNullRoadOrientation_whenParsingRoadConfig_thenThrowException() {
        // Given
        RoadOrientation roadOrientation = null;
        List<LaneConfig> laneConfigList = Collections.emptyList();

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoadConfig(roadOrientation, laneConfigList)
        );

        // Check exception message
        assertEquals("RoadOrientation wasn't give or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveNullLaneConfigList_whenParsingRoadConfig_thenThrowException() {
        // Given
        RoadOrientation roadOrientation = RoadOrientation.BOTTOM;
        List<LaneConfig> laneConfigList = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoadConfig(roadOrientation, laneConfigList)
        );

        // Check exception message
        assertEquals("LaneConfig wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveEmptyLaneConfigList_whenParsingRoadConfig_thenThrowException() {
        // Given
        RoadOrientation roadOrientation = RoadOrientation.BOTTOM;
        List<LaneConfig> laneConfigList = Collections.emptyList();

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new RoadConfig(roadOrientation, laneConfigList)
        );

        // Check exception message
        assertEquals("LangConfig is an empty list!", exception.getMessage());
    }
}
