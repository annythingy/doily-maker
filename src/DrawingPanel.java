import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Stack;

/**
 * This is where all the drawing happens.
 * The class acts as its own listener for convenience
 * (so that the addition of new strokes and points happens in the same class they're drawn in).
 */
public class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {

    private int n = 8;                         //number of sectors, there has to be some initial number
    private int penSize;                       //last chosen width of the brush
    private Line stroke;
    private Color currColour;                  //last chosen colour
    private boolean reflection;
    private boolean back = true;               //visible black background
    private boolean visibleSections = true;    //visible sector lines
    private Stack<Line> history;               //stores all the strokes made in the current doily
    private GalleryPanel gallery;

    public DrawingPanel(int width, int height, GalleryPanel gallery) {
        this.gallery = gallery;
        setPreferredSize(new Dimension(width, height));
        history = new Stack<>();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        double angle = 360d / n;
        int c = getWidth() / 2;     //since the drawing area is fixed as a square, the center points are the same

        //draws the optional elements
        if (back) {
            g2d.setColor(Color.black);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        if (visibleSections) {
            g2d.setColor(Color.gray);
            for (int i = 0; i < n; i++) {
                g2d.drawLine(c, c, c, 0);
                g2d.rotate(Math.toRadians(angle), c, c);
            }
        }

        g2d.setColor(Color.white);  //if no colour is chosen, white is the initial choice
        //goes through all recorded strokes and draws lines between all pairs of sequential points and rotates the result, n times
        for (Line stroke : history) {
            for (int r = 0; r < n; r++) {
                for (int i = 0; i < stroke.getPoints().size() - 1; i++) {
                    Point p1 = stroke.getPoints().get(i);
                    Point p2 = stroke.getPoints().get(i + 1);
                    g2d.setColor(stroke.getColour());
                    g2d.setStroke(new BasicStroke(stroke.getWidth(), 1, 1));
                    g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                    if (stroke.isReflected())   //draws a second line with the coordinates rotated 180 degrees and flipped vertically
                        g2d.drawLine((int) (p1.getX() - (p1.getX() - c) * 2), (int) p1.getY(), (int) (p2.getX() - (p2.getX() - c) * 2), (int) (p2.getY()));
                }
                g2d.rotate(Math.toRadians(360d / n), c, c);
            }
        }
    }

    //creates a buffered image of the drawing and adds it to the gallery in a Doily panel
    public void saveImg() {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        paint(img.getGraphics());
        Image thumbnail = img.getScaledInstance(200, 200, BufferedImage.SCALE_SMOOTH);
        gallery.addDoily(new Doily(thumbnail, img, gallery));
        try {
            File dir = new File("Gallery");
            if(!dir.exists()) dir.mkdir();
            if (dir.exists()&&ImageIO.write(img, "png", new File("Gallery/" + System.currentTimeMillis() + ".png")))
                JOptionPane.showMessageDialog(this, "Your doily was successfully saved ^_^", "Hooray!", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Something went wrong :(", "Oops!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    /*
    every pressing of the mouse creates a new separate line
    that is constantly re-added to the history stack
    so that the current line being drawn is visible
    */
    public void mousePressed(MouseEvent e) {
        stroke = new Line(penSize, currColour, reflection);
        history.push(stroke);
        repaint();
    }

    @Override
    /*
    to draw a single point
    simply add a Line object with only 2 points in its list
     */
    public void mouseClicked(MouseEvent e) {
        stroke = new Line(penSize, currColour, reflection);
        for (int i = 0; i < 2; i++) stroke.getPoints().add(e.getPoint());
        history.push(stroke);
        repaint();
    }

    //new points are constantly added to the current stroke
    @Override
    public void mouseDragged(MouseEvent e) {
        stroke.getPoints().add(e.getPoint());
        repaint();
    }

    //GETTERS + SETTERS

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public boolean isReflection() {
        return reflection;
    }

    public void setReflection(boolean reflection) {
        this.reflection = reflection;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public boolean isVisibleSections() {
        return visibleSections;
    }

    public void setVisibleSections(boolean visibleSections) {
        this.visibleSections = visibleSections;
    }

    public Stack<Line> getHistory() {
        return history;
    }

    public void setCurrColour(Color currColour) {
        this.currColour = currColour;
    }

    public void setPenSize(int penSize) {
        this.penSize = penSize;
    }

    //empty methods that just need to be somewhere :/

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}