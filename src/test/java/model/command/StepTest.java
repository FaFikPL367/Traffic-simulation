package model.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.Simulation;
import org.project.TrafficController;
import org.project.enums.LaneDirection;
import org.project.enums.RoadOrientation;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.command.Step;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Class with tests for - Step - command
 */
@ExtendWith(MockitoExtension.class)
public class StepTest {

    @Mock
    private Simulation simulationMock;

    @Mock
    private Junction junctionMock;

    public static final Comparator<Lane> LANE_COMPARATOR = Comparator
            .comparingDouble(Lane::getLaneWage)
            .thenComparingInt(Lane::getHasPriority)
            .thenComparingInt(Lane::getTimeInPriorityQueue)
            .reversed();


    @Test
    void giveMaxPassedVehicleNotEqualZero_whenStepCommand_thenDoContinueVehicleDrive() {
        // Given
        when(simulationMock.getMaxPassedVehicleOnMostImportantLane()).thenReturn(5);

        try(MockedStatic<TrafficController> mockedController = mockStatic(TrafficController.class)) {
            // Create step command
            Step step = new Step();

            // When
            step.execute(simulationMock);

            // Then
            mockedController.verify(() -> TrafficController.continueVehicleDrive(simulationMock, step.id()), times(1));
            verify(simulationMock, never()).getJunction();
        }
    }

    @Test
    void giveMaxPassedVehicleEqualZero_whenStepCommand_thenCalculateGreenLightsAgain() {
        // Given
        when(simulationMock.getMaxPassedVehicleOnMostImportantLane()).thenReturn(0);

        // Create some lanes
        Lane priorityLaneMock = mock(Lane.class);
        Lane noConflictLaneMock = mock(Lane.class);
        Lane backwardLaneMock = mock(Lane.class);

        // Configure lanes
        when(priorityLaneMock.getLaneDirection()).thenReturn(LaneDirection.STRAIGHT);
        when(priorityLaneMock.getEndRoadOrientation()).thenReturn(RoadOrientation.TOP);
        when(priorityLaneMock.getVehicleCount()).thenReturn(10);
        when(priorityLaneMock.getHasPriority()).thenReturn(1);

        when(noConflictLaneMock.getLaneDirection()).thenReturn(LaneDirection.RIGHT);
        when(noConflictLaneMock.getEndRoadOrientation()).thenReturn(RoadOrientation.RIGHT);
        when(noConflictLaneMock.getVehicleCount()).thenReturn(4);
        when(noConflictLaneMock.getHasPriority()).thenReturn(0);

        // Configure backward lane (each road has backward lane)
        when(backwardLaneMock.getLaneDirection()).thenReturn(LaneDirection.BACKWARD);

        // Create list with all lanes and priority queue
        List<Lane> allLanes = List.of(priorityLaneMock, noConflictLaneMock, backwardLaneMock);

        // Create priority queue
        PriorityQueue<Lane> priorityQueue = new PriorityQueue<>(LANE_COMPARATOR);

        priorityQueue.add(priorityLaneMock);
        priorityQueue.add(noConflictLaneMock);

        List<Lane> lastGreenLanes = new ArrayList<>();
        Map<RoadOrientation, List<Lane>> roadWithLanesMap = Map.of(
                RoadOrientation.TOP, List.of(backwardLaneMock),
                RoadOrientation.RIGHT, List.of(backwardLaneMock)
        );

        // Mock what simulation need to return
        when(simulationMock.getJunction()).thenReturn(junctionMock);
        when(junctionMock.allLanes()).thenReturn(allLanes);
        when(junctionMock.roadWithLanes()).thenReturn(roadWithLanesMap);

        when(simulationMock.getPriorityLanes()).thenReturn(priorityQueue);
        when(simulationMock.getLastGreenLanes()).thenReturn(lastGreenLanes);

        when(priorityLaneMock.getLaneWithConflict()).thenReturn(List.of());

        // Done single step of simulation
        try (MockedStatic<TrafficController> mockedController = mockStatic(TrafficController.class)) {
            Step stepCommand = new Step();

            // When
            stepCommand.execute(simulationMock);

            // Then
            // Verify if wages was counted properly
            verify(priorityLaneMock, times(1)).countNewWage();
            verify(noConflictLaneMock, times(1)).countNewWage();
            verify(backwardLaneMock, never()).countNewWage(); // BACKWARD ma być ignorowany!

            // Verify if list with green lights was filled properly
            assertEquals(2, lastGreenLanes.size());

            // Verify if maxPassedVehicle was counted properly
            verify(simulationMock).setMaxPassedVehicleOnMostImportantLane(5);

            mockedController.verify(() -> TrafficController.continueVehicleDrive(simulationMock, stepCommand.id()), times(1));
        }
    }
}
