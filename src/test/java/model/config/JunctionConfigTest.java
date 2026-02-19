package model.config;


import org.junit.jupiter.api.Test;
import org.project.model.config.JunctionConfig;
import org.project.model.config.RoadConfig;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class with tests to - JunctionConfig
 */
public class JunctionConfigTest {

    @Test
    void giveNullRoadList_whenParsingJunctionConfig_thenThrowException() {
        // Given
        List<RoadConfig> roadConfigList = null;

        // When & Given
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JunctionConfig(roadConfigList)
        );

        // Check exception message
        assertEquals("RoadConfig wasn't give in input file!", exception.getMessage());
    }

    @Test
    void giveEmptyRoadList_whenParsingJunctionConfig_thenThrowException() {
        // Given
        List<RoadConfig> roadConfigList = Collections.emptyList();

        // When & Given
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JunctionConfig(roadConfigList)
        );

        // Check exception message
        assertEquals("RoadConfig is an empty list!", exception.getMessage());
    }
}
