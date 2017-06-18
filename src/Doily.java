import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * A panel that holds a picture which can be either viewed in a pop-up window or deleted
 */
public class Doily extends JPanel {
    private int ab = 200;           //the side length of the square thumbnail
    private boolean selected;
    private Image thumb;            //the image shown in the gallery
    private BufferedImage img;      //the image shown in the pop-up
    private GalleryPanel gallery;

    public Doily(Image thumb, BufferedImage img, GalleryPanel gallery) {
        this.thumb = thumb;
        this.img = img;
        this.gallery = gallery;
        addListener();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //present the image in the panel and locate it in the middle
        g2d.drawImage(thumb, getWidth() / 2 - ab / 2, getHeight() / 2 - ab / 2, ab, ab, null);
    }

    /*
    invokes a pop-up window to show the doily in its full size
    and clears the selection
    */
    public void viewDoily() {
        JDialog dialog = new JDialog();
        dialog.add(new JLabel(new ImageIcon(img)));
        dialog.setTitle("Погледни това съвършенство!");
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocation(450, 45);
        setBackground(null);
    }

    /*
    make the Doily clickable,
    indicate selection by changing the background to gray
     */
    public void addListener() {
        addMouseListener(new MouseListener() {
            @Override
            //significantly better performance that mouseClicked
            public void mousePressed(MouseEvent e) {
                //deselect all doilies in the array list
                for (Doily panel : gallery.getDoilies()) {
                    panel.setBackground(null);
                    panel.setSelected(false);
                }
                //then select the current one and display options
                setSelected(true);
                setBackground(Color.gray);
                gallery.showButtons();
            }
            //did not use mouseClicked because it reacts poorly when the mouse movement is more dynamic
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
