import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.Simulation;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.model.Lane;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class with tests to - Simulation
 */
public class SimulationTest {

    // Variable for priority queue
    private final PriorityQueue<Lane> priorityQueue = new PriorityQueue<>(Simulation.LANE_COMPARATOR);

    @BeforeEach
    void setUp() {
        // Clean priority queue
        priorityQueue.clear();
    }

    @Test
    void giveLaneWages_whenAddingToPriorityQueue_thenReturnCorrectPriorityQueue() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Lane highWage = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        highWage.setLaneWage(10.0);
        highWage.setHasPriority(0);
        highWage.setTimeInPriorityQueue(0);
        Lane lowWage = new Lane(2, RoadOrientation.BOTTOM, LaneDirection.STRAIGHT, 0);
        lowWage.setLaneWage(5.0);
        lowWage.setHasPriority(0);
        lowWage.setTimeInPriorityQueue(0);

        // When
        priorityQueue.add(lowWage);
        priorityQueue.add(highWage);

        // Then
        assertEquals(2, priorityQueue.size());
        assertEquals(highWage, priorityQueue.poll());
        assertEquals(lowWage, priorityQueue.poll());
    }

    @Test
    void giveLanesSameWageAndPriority_whenAddingToPriorityQueue_thenReturnCorrectPriorityQueue() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Lane highPriority = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        highPriority.setLaneWage(10.0);
        highPriority.setHasPriority(1);
        highPriority.setTimeInPriorityQueue(0);
        Lane lowPriority = new Lane(2, RoadOrientation.BOTTOM, LaneDirection.STRAIGHT, 0);
        lowPriority.setLaneWage(10.0);
        lowPriority.setHasPriority(0);
        lowPriority.setTimeInPriorityQueue(0);

        // When
        priorityQueue.add(lowPriority);
        priorityQueue.add(highPriority);

        // Then
        assertEquals(2, priorityQueue.size());
        assertEquals(highPriority, priorityQueue.poll());
        assertEquals(lowPriority, priorityQueue.poll());
    }

    @Test
    void giveLanesSameWageAndSamePriorityAndTimeInPriorityQueue_whenAddingToPriorityQueue_thenReturnCorrectPriorityQueue() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Lane lowTimeInPriorityQueueLane = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        lowTimeInPriorityQueueLane.setLaneWage(10.0);
        lowTimeInPriorityQueueLane.setHasPriority(1);
        lowTimeInPriorityQueueLane.setTimeInPriorityQueue(0);
        Lane highTimeInPriorityQueueLane = new Lane(2, RoadOrientation.BOTTOM, LaneDirection.STRAIGHT, 0);
        highTimeInPriorityQueueLane.setLaneWage(10.0);
        highTimeInPriorityQueueLane.setHasPriority(1);
        highTimeInPriorityQueueLane.setTimeInPriorityQueue(1);

        // When
        priorityQueue.add(highTimeInPriorityQueueLane);
        priorityQueue.add(lowTimeInPriorityQueueLane);

        // Then
        assertEquals(2, priorityQueue.size());
        assertEquals(highTimeInPriorityQueueLane, priorityQueue.poll());
        assertEquals(lowTimeInPriorityQueueLane, priorityQueue.poll());
    }

    @Test
    void giveDifferentValues_whenAddingToPriorityQueue_thenReturnCorrectPriorityQueue() {
        // Given
        Lane lane1 = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        lane1.setTimeInPriorityQueue(1);
        lane1.setLaneWage(10.0);
        lane1.setHasPriority(0);

        Lane lane2 = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        lane2.setTimeInPriorityQueue(2);
        lane2.setLaneWage(1.0);
        lane2.setHasPriority(1);

        Lane lane3 = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        lane3.setTimeInPriorityQueue(1);
        lane3.setLaneWage(5.0);
        lane3.setHasPriority(0);

        Lane lane4 = new Lane(1, RoadOrientation.TOP, LaneDirection.STRAIGHT, 0);
        lane4.setTimeInPriorityQueue(1);
        lane4.setLaneWage(5.0);
        lane4.setHasPriority(1);

        // When
        priorityQueue.add(lane1);
        priorityQueue.add(lane2);
        priorityQueue.add(lane3);
        priorityQueue.add(lane4);

        // Then
        assertEquals(4, priorityQueue.size());
        assertEquals(lane2, priorityQueue.poll());
        assertEquals(lane1, priorityQueue.poll());
        assertEquals(lane4, priorityQueue.poll());
        assertEquals(lane3, priorityQueue.poll());
    }
}
