import java.awt.*;
import javax.swing.*;

/**
 * The main frame of the doily maker program.
 * This is where the latter executes.
 * The frame has 2 tabs - one for drawing and one for viewing drawings.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        super("Doily Maker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPanels();
        this.pack();
    }

    public static void main(String[] args) {
        MainFrame df = new MainFrame();
        df.setVisible(true);
    }

    //arrange all the panels
    public void addPanels() {
        int squareSide = 860;    //making sure that the drawing panel is square and the other tab is of the same size
        JPanel main = new JPanel();
        JTabbedPane tabs = new JTabbedPane();
        JPanel playGround = new JPanel(new BorderLayout());
        GalleryPanel gallery = new GalleryPanel();
        DrawingPanel drawing = new DrawingPanel(squareSide, squareSide, gallery);
        ControlPanel control = new ControlPanel(drawing);

        gallery.setPreferredSize(new Dimension(playGround.getWidth(), playGround.getHeight()));

        playGround.add(drawing, BorderLayout.CENTER);
        playGround.add(control, BorderLayout.SOUTH);
        tabs.addTab("Drawing", playGround);
        tabs.addTab("Recent works", gallery);
        main.add(tabs);
        setContentPane(main);
    }
}
