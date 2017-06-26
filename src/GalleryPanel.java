import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Holds all the "saved" drawings. It is allowed to have up to 12 images.
 * They can be viewed in full size or deleted.
 */
public class GalleryPanel extends JPanel {

    private JPanel pictures;
    private JPanel buttonPanel;
    private ArrayList<Doily> doilies;   //used for convenience when selecting(see Doily class)

    public GalleryPanel() {
        doilies = new ArrayList<>();
        setPreferredSize(new Dimension(1900, 900));
        setLayout(new BorderLayout());
        addPanels();
        hideButtons();
    }

    //fills the gallery panel with the needed panels
    public void addPanels() {
        pictures = new JPanel(new GridLayout(3,4));
        buttonPanel = new JPanel(); //only visible when a doily has been selected
        JButton view = new JButton("View");
        JButton delete = new JButton("Wipe recent doilies");

        view.addActionListener(e -> {
            for (Doily sd : doilies) {
                //the background of the Doily panel determines if it's selected
                if (sd.isSelected()) sd.viewDoily();    //will open a pop-up window
            }
            hideButtons();
        });
        delete.addActionListener(e -> {
            //give the user the option to reconsider
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to empty the recent gallery?", ":'(", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                for (Component sd : pictures.getComponents()) {
                    pictures.remove(sd);
                }
                hideButtons();
                revalidate();
                repaint();
            }
        });

        buttonPanel.add(view);
        buttonPanel.add(delete);
        add(pictures, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addDoily(Doily doily) {
        pictures.add(doily);
        doilies.add(doily);
    }

    //the following two methods toggle the visibility of the buttons at the bottom
    public void showButtons() {
        buttonPanel.setVisible(true);
    }

    public void hideButtons() {
        buttonPanel.setVisible(false);
    }

    public ArrayList<Doily> getDoilies() {
        return doilies;
    }
}
