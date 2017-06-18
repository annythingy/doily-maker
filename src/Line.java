import java.awt.*;
import java.util.LinkedList;

/**
 * Each instance of this class represents a distinct stroke made by the mouse.
 * This class makes it possible to draw strokes with different features independently.
 */

public class Line {

    private int width;
    private Color colour;
    private boolean reflected;
    private LinkedList<Point> points;

    //each stroke has its own thickness, colour and a flag for being reflected.
    public Line(int width, Color colour, boolean reflected) {
        this.width = width;
        this.colour = colour;
        this.reflected = reflected;
        points = new LinkedList<>();
    }

    //getters from here on...
    public LinkedList<Point> getPoints() {
        return points;
    }

    public Color getColour() {
        return colour;
    }

    public int getWidth() {
        return width;
    }

    public boolean isReflected() {
        return reflected;
    }
}
