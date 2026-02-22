package factory;

import org.junit.jupiter.api.Test;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.factory.JunctionFactory;
import org.project.model.Junction;
import org.project.model.config.JunctionConfig;
import org.project.model.config.LaneConfig;
import org.project.model.config.RoadConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with tests for - JunctionFactory
 */
public class JunctionFactoryTest {

    @Test
    void giveDuplicateRoadOrientation_whenCreateJunction_thenThrowException() {
        // Given
        List<RoadConfig> roadConfigList = List.of(
                new RoadConfig(RoadOrientation.BOTTOM, List.of(new LaneConfig(LaneDirection.STRAIGHT, 0))),
                new RoadConfig(RoadOrientation.TOP, List.of(new LaneConfig(LaneDirection.STRAIGHT, 0))),
                new RoadConfig(RoadOrientation.BOTTOM, List.of(new LaneConfig(LaneDirection.RIGHT, 0)))
        );
        JunctionConfig junctionConfig = new JunctionConfig(roadConfigList);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> JunctionFactory.createJunction(junctionConfig)
        );

        // Check exception message
        assertEquals("In input file RoadOrientation - BOTTOM - appear more than once!", exception.getMessage());
    }

    @Test
    void giveLaneToNotExistRoad_whenCreateJunction_thenThrowException() {
        // Given
        List<RoadConfig> roadConfigList = List.of(
                new RoadConfig(RoadOrientation.BOTTOM, List.of(new LaneConfig(LaneDirection.STRAIGHT, 0))),
                new RoadConfig(RoadOrientation.LEFT, List.of(new LaneConfig(LaneDirection.RIGHT, 0)))
        );
        JunctionConfig junctionConfig = new JunctionConfig(roadConfigList);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> JunctionFactory.createJunction(junctionConfig)
        );

        // Check exception message
        assertEquals("Lane (BOTTOM, STRAIGHT) cannot be add, because junction doesn't contain end road for this lane!", exception.getMessage());
    }

    @Test
    void giveLaneWithoutBackwardLane_whenCreateJunction_thenThrowException() {
        // Given
        List<RoadConfig> roadConfigList = List.of(
                new RoadConfig(RoadOrientation.BOTTOM, List.of(new LaneConfig(LaneDirection.STRAIGHT, 0))),
                new RoadConfig(RoadOrientation.TOP, List.of(new LaneConfig(LaneDirection.BACKWARD, 0))),
                new RoadConfig(RoadOrientation.LEFT, List.of(new LaneConfig(LaneDirection.RIGHT, 0)))
        );
        JunctionConfig junctionConfig = new JunctionConfig(roadConfigList);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> JunctionFactory.createJunction(junctionConfig)
        );

        // Check exception message
        assertEquals("Lane (LEFT, RIGHT) cannot be added, because end road doesn't have BACKWARD lane!", exception.getMessage());
    }

    @Test
    void giveCorrectJunctionConfig_whenCreateJunction_thenReturnJunction() {
        // Given
        List<RoadConfig> roadConfigList = List.of(
                new RoadConfig(RoadOrientation.BOTTOM, List.of(new LaneConfig(LaneDirection.STRAIGHT, 0), new LaneConfig(LaneDirection.BACKWARD, 0))),
                new RoadConfig(RoadOrientation.TOP, List.of(new LaneConfig(LaneDirection.BACKWARD, 0), new LaneConfig(LaneDirection.STRAIGHT, 0))),
                new RoadConfig(RoadOrientation.RIGHT, List.of(new LaneConfig(LaneDirection.RIGHT, 0), new LaneConfig(LaneDirection.LEFT, 0)))
        );
        JunctionConfig junctionConfig = new JunctionConfig(roadConfigList);

        // When
        Junction junction = JunctionFactory.createJunction(junctionConfig);

        // Then
        assertNotNull(junction);
        assertEquals(3, junction.roadWithLanes().size());
        assertEquals(6, junction.allLanes().size());
    }
}
