package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.util.ConflictFinder;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.factory.JunctionFactory;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.config.JunctionConfig;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with tests for - ConflictFinder
 */
public class ConflictFInderTest {

    // Variable for example junction
    private Junction junction;
    private static final String CORRECT_4_WAY_JUNCTION = "valid_4_wave.json";

    // To parse JSON
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Read example 4 wave, correct junction for testing
        InputStream inputStream = getClass().getResourceAsStream("/junctions/" + CORRECT_4_WAY_JUNCTION);
        assertNotNull(inputStream);
        JunctionConfig junctionConfig = mapper.readValue(inputStream, JunctionConfig.class);

        // Create junction
        this.junction = JunctionFactory.createJunction(junctionConfig);
    }

    @Test
    void giveRightLane_whenFindingConflicts_thenReturnEmptyList() {
        // Given
        Lane lane = new Lane(
                1, RoadOrientation.BOTTOM, LaneDirection.RIGHT, 0
        );
        List<Lane> otherLanes = Collections.emptyList();

        // When
        List<Lane> actualConflictsLane = ConflictFinder.findConflictForLane(lane, otherLanes);

        // Then
        assertTrue(actualConflictsLane.isEmpty());
    }

    @Test
    void giveStraightLane_whenFindingConflicts_thenReturnCorrectConflicts() {
        // Given
        Lane straightLine = junction.allLanes()
                .stream().filter(lane -> lane.getLaneDirection() == LaneDirection.STRAIGHT)
                .findAny().get();

        // When
        List<Lane> foundConflictLanes = ConflictFinder.findConflictForLane(straightLine, junction.allLanes());

        // Then
        assertFalse(foundConflictLanes.isEmpty());
        assertEquals(4, foundConflictLanes.size());
    }

    @Test
    void giveLeftLane_whenFindingConflicts_thenReturnCorrectConflicts() {
        // Given
        Lane leftLine = junction.allLanes()
                .stream().filter(lane -> lane.getLaneDirection() == LaneDirection.LEFT)
                .findAny().get();

        // When
        List<Lane> foundConflictLanes = ConflictFinder.findConflictForLane(leftLine, junction.allLanes());

        // Then
        assertFalse(foundConflictLanes.isEmpty());
        assertEquals(4, foundConflictLanes.size());
    }
}
