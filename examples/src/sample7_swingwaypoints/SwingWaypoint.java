package sample7_swingwaypoints;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * A waypoint that is represented by a button on the map.
 *
 * @author Daniel Stahr
 */
public class SwingWaypoint extends DefaultWaypoint {
    private final JButton button;
    private String text = "";

    public SwingWaypoint(String text, GeoPosition coord) {
        super(coord);
        this.text = text;
        button = new JButton(text.substring(0, 1));
        button.setSize(24, 24);
        button.setPreferredSize(new Dimension(24, 24));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);
    }

    public SwingWaypoint(BufferedImage img, GeoPosition coord) {
        super(coord);
        ImageIcon icon  = new ImageIcon(img.getScaledInstance(24, 24,0));
        button = new JButton(icon);
        button.setSize(24, 24);
        button.setPreferredSize(new Dimension(24, 24));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);
    }

    public SwingWaypoint(BufferedImage img, GeoPosition coord, int width, int height) {
        super(coord);
        ImageIcon icon  = new ImageIcon(img.getScaledInstance(width, height,0));
        button = new JButton(icon);
        button.setSize(width, height);
        button.setPreferredSize(new Dimension(width, height));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);
    }

    JButton getButton() {
        return button;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JOptionPane.showMessageDialog(button, "You clicked on " + text);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
