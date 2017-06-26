import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;

/**
 * This class is responsible for most of the drawing settings.
 * It is divided into 3 sections, ordered in a simple flow layout:
 * button section, sections for number values with a colour chooser button underneath, and section of toggles.
 */
public class ControlPanel extends JPanel {

    private DrawingPanel drawing;
    //those components as class variables let me modify them from other methods
    private JButton colourPick;
    private JSlider penSize;

    public ControlPanel(DrawingPanel drawing) {
        this.drawing = drawing;
        //fill with all needed panels and sub-panels
        addButtons();
        addSettings();
        addToggles();
    }

    /*
    adds buttons regarding the fate of the drawing:
    save, erase, or just undo the last step
     */
    public void addButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        JButton undo = new JButton("Undo");
        JButton clear = new JButton("Clear");
        JButton save = new JButton("Save");
        //make sure all the buttons are the same size
        undo.setMaximumSize(new Dimension(100, 30));
        clear.setMaximumSize(new Dimension(100, 30));
        save.setMaximumSize(new Dimension(100, 30));

        undo.addActionListener(e -> {
            if (drawing.getHistory().size() > 0) drawing.getHistory().pop();
            drawing.repaint();
        });

        clear.addActionListener(e -> {
            //make sure the user doesn't wipe the drawing by accident
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure?", ":'(", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                //reset the drawing, as well as the settings (except reflection because I like it toggled)
                colourPick.setBackground(null);
                colourPick.setForeground(null);
                drawing.getHistory().clear();
                drawing.setCurrColour(Color.white);
                drawing.setPenSize(1);
                penSize.setValue(1);
                drawing.repaint();
            }
        });

        save.addActionListener(e -> drawing.saveImg());

        buttons.add(undo);
        buttons.add(Box.createRigidArea(new Dimension(0, 10)));
        buttons.add(clear);
        buttons.add(Box.createRigidArea(new Dimension(0, 10)));
        buttons.add(save);
        add(buttons);
    }

    /*
    adds a settings panel divided in two parts -
    number values and colour choice
     */
    public void addSettings() {
        JPanel settings = new JPanel(new GridLayout(2,1));
        JPanel values = new JPanel();                   //upper half
        colourPick = new JButton("Colour Chooser"); //lower half
        penSize = new JSlider(0, 10, 1);
        SpinnerModel spin = new SpinnerNumberModel(4, 1, 100, 1);
        JSpinner sectors = new JSpinner(spin);
        //the spinner is set to whatever the starting value of n is (to avoid inconsistency)
        sectors.setValue(drawing.getN());

        //setting up the pen width slider
        penSize.setMinorTickSpacing(1);
        penSize.setMajorTickSpacing(2);
        penSize.setPaintTicks(true);
        penSize.setPaintTrack(false);
        penSize.setPaintLabels(true);
        //it skips every other value since the space is limited and the difference is minimal
        penSize.addChangeListener(e -> drawing.setPenSize(penSize.getValue() * 2));

        spin.addChangeListener(e -> {
            drawing.setN((Integer) spin.getValue());
            drawing.repaint();
        });

        colourPick.addActionListener(e -> {
            Color c = JColorChooser.showDialog(null, "Pick a colour", null);
            drawing.setCurrColour(c);
            colourPick.setBackground(c);
            //make the name of the button visible regardless of the colour
            if (colourPick.getBackground().getRed() > 100 && colourPick.getBackground().getGreen() > 100 && colourPick.getBackground().getBlue() > 100 || colourPick.getBackground().getGreen() > 205)
                //the green shades create some uncertainty, that's why I added an extra condition
                colourPick.setForeground(Color.black);
            else colourPick.setForeground(Color.white);
        });

        values.add(new JLabel("Brush width:"));
        values.add(penSize);
        values.add(new JLabel("Sectors:"));
        values.add(sectors);
        settings.add(values);
        settings.add(colourPick);
        add(settings);
    }

    /*
    adds toggles that change the structure of the drawing:
    whether or not it has a background or visible sectors,
    and whether or not the next lines should be reflected
     */
    public void addToggles() {
        JPanel toggles = new JPanel();
        toggles.setLayout(new BoxLayout(toggles, BoxLayout.PAGE_AXIS));

        JCheckBox reflect = new JCheckBox("Reflection");
        JCheckBox back = new JCheckBox("Black background");
        JCheckBox sectorLines = new JCheckBox("Visible sector lines");

        //by default only reflection should be off, but just in case the booleans get changed in the other class
        reflect.setSelected(drawing.isReflection());
        back.setSelected(drawing.isBack());
        sectorLines.setSelected(drawing.isVisibleSections());

        reflect.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) drawing.setReflection(true);
            else drawing.setReflection(false);
            drawing.repaint();
        });
        back.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) drawing.setBack(true);
            else drawing.setBack(false);
            drawing.repaint();
        });
        sectorLines.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) drawing.setVisibleSections(true);
            else drawing.setVisibleSections(false);
            drawing.repaint();
        });

        toggles.add(reflect);
        toggles.add(back);
        toggles.add(sectorLines);
        add(toggles);
    }
}
