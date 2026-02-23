package org.project.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.project.enums.RoadOrientation;
import org.project.gui.record.Point;
import org.project.model.Junction;
import org.project.model.Lane;

import java.util.*;

/**
 * Class with methods to render junction
 */
public class JunctionRender {

    // Static values
    private static final int LINE_WIDTH = 40;
    private static final int ROAD_LENGTH = 400;

    // Variable for corners center of junction
    private static List<Point> corners = new ArrayList<>();

    public static void drawJunction(Junction junction, Canvas canvas) {
        // Get graphics context
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // Get width and height
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        // Set GREEN color to canvas
        graphicsContext.setFill(Color.FORESTGREEN);
        graphicsContext.fillRect(0, 0, width, height);

        // Determine corners of center of junction
        determineCorners(junction, width, height);

        // Draw rectangle based on corners
        graphicsContext.setFill(Color.DARKGRAY);
        graphicsContext.fillPolygon(
                corners.stream().mapToDouble(Point::x).toArray(), corners.stream().mapToDouble(Point::y).toArray(), corners.size()
        );

        // Draw roads from center
        drawRoads(junction, graphicsContext);
    }

    private static void determineCorners(Junction junction, double width, double height) {
        // Get center point of canvas
        double centerX = width / 2;
        double centerY = height / 2;

        // Map to save width of each road
        Map<RoadOrientation, Double> roadsWidth = new HashMap<>();

        // Get width of each road
        for (Map.Entry<RoadOrientation, List<Lane>> entry : junction.roadWithLanes().entrySet()) {
            // Count width of road
            double roadWidth = LINE_WIDTH * entry.getValue().size();

            // Add to map
            roadsWidth.put(entry.getKey(), roadWidth);
        }

        // Check if each road has width
        for (RoadOrientation roadOrientation : RoadOrientation.values()) {
            if (!roadsWidth.containsKey(roadOrientation))
                roadsWidth.put(roadOrientation, roadsWidth.get(RoadOrientation.oppositeRoad(roadOrientation)));
        }

        // Calculate middle point on each side
        Point top = new Point(centerX, centerY - roadsWidth.get(RoadOrientation.RIGHT) / 2);
        Point bottom = new Point(centerX, centerY + roadsWidth.get(RoadOrientation.RIGHT) / 2);
        Point right = new Point(centerX + roadsWidth.get(RoadOrientation.TOP) / 2, centerY);
        Point left = new Point(centerX - roadsWidth.get(RoadOrientation.TOP) / 2, centerY);

        // Calculate corners
        corners.add(new Point(left.x(), top.y())); // top-left
        corners.add(new Point(right.x(), top.y())); // top-right
        corners.add(new Point(right.x(), bottom.y())); // bottom-right
        corners.add(new Point(left.x(), bottom.y())); // bottom-left
    }

    private static void drawRoads(Junction junction, GraphicsContext graphicsContext) {
        int i = 0;
        for (RoadOrientation roadOrientation : RoadOrientation.values()) {
            if (!junction.roadWithLanes().containsKey(roadOrientation)) {
                i++; continue;
            }


        }
    }
}
