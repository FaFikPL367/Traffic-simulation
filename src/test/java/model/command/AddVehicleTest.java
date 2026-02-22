package model.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.Simulation;
import org.project.enums.RoadOrientation;
import org.project.model.Junction;
import org.project.model.Lane;
import org.project.model.Vehicle;
import org.project.model.command.AddVehicle;

import java.util.ArrayDeque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class with tests to - AddVehicle
 */
@ExtendWith(MockitoExtension.class)
public class AddVehicleTest {

    @Test
    void giveNullStartRoadOrientation_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = null;
        RoadOrientation endRoadOrientation = RoadOrientation.TOP;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("StartRoadOrientation wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveNullEndRoadOrientation_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = RoadOrientation.RIGHT;
        RoadOrientation endRoadOrientation = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("EndRoadOrientation wasn't given or given value is incorrect!", exception.getMessage());
    }

    @Test
    void giveSameStartAndEndRoadDirection_whenParsingAddVehicle_thenThrowException() {
        // Given
        RoadOrientation startRoadOrientation = RoadOrientation.RIGHT;
        RoadOrientation endRoadOrientation = RoadOrientation.RIGHT;

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AddVehicle(startRoadOrientation, endRoadOrientation)
        );

        // Check exception message
        assertEquals("StartRoadOrientation and EndRoadOrientation can't be the same!", exception.getMessage());
    }

    // Test - execute - method
    @Mock
    private Simulation simulationMock;

    @Mock
    private Junction junctionMock;

    @Mock
    private Lane correctLaneMock;

    @Mock
    private Lane wrongLaneMock;

    @Test
    void giveCorrectVehicleAndLane_whenExecuteAddVehicleCommand_thenVehicleAddedCorrectly() {
        // Prepare all to test
        AddVehicle addVehicle = new AddVehicle(RoadOrientation.BOTTOM, RoadOrientation.TOP);
        Queue<Vehicle> targetLaneVehicleList = new ArrayDeque<>();

        when(simulationMock.getJunction()).thenReturn(junctionMock);
        when(simulationMock.getPriorityLanes()).thenReturn(new PriorityQueue<>(Simulation.LANE_COMPARATOR));
        when(junctionMock.allLanes()).thenReturn(List.of(correctLaneMock));
        when(correctLaneMock.getStartRoadOrientation()).thenReturn(RoadOrientation.BOTTOM);
        when(correctLaneMock.getEndRoadOrientation()).thenReturn(RoadOrientation.TOP);
        when(correctLaneMock.getVehicles()).thenReturn(targetLaneVehicleList);

        // When
        addVehicle.execute(simulationMock);

        // Then
        assertEquals(1, targetLaneVehicleList.size()); // test list size

        Vehicle addedVehicle = targetLaneVehicleList.poll();
        assertNotNull(addedVehicle);
        assertEquals(RoadOrientation.BOTTOM, addedVehicle.getStartRoadDirection());
        assertEquals(RoadOrientation.TOP, addedVehicle.getEndRoadDirection());
    }

    @Test
    void giveWrongVehicleAndLane_whenExecuteAddVehicleCommand_thenZeroVehicleAdded() {
        // Prepare all to test
        AddVehicle addVehicle = new AddVehicle(RoadOrientation.BOTTOM, RoadOrientation.TOP);

        when(simulationMock.getJunction()).thenReturn(junctionMock);
        when(junctionMock.allLanes()).thenReturn(List.of(wrongLaneMock));
        when(wrongLaneMock.getStartRoadOrientation()).thenReturn(RoadOrientation.BOTTOM);
        when(wrongLaneMock.getEndRoadOrientation()).thenReturn(RoadOrientation.RIGHT);

        // When
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> addVehicle.execute(simulationMock)
        );

        // Then
        verify(wrongLaneMock, never()).getVehicles();
        assertEquals("Vehicle (Vehicle (1) (BOTTOM, TOP)) can't put in declared lane for him, because this lane doesn't exist!", exception.getMessage());
    }
}
