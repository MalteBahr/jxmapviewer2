
package org.jxmapviewer.input;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Listens to single mouse clicks on the map and returns the GeoPosition
 *
 * @author Richard Eigenmann
 */
public abstract class MapClickListener extends MouseAdapter {

    private final JXMapViewer viewer;

    /**
     * Creates a mouse listener for the jxmapviewer which returns the
     * GeoPosition of the the point where the mouse was clicked.
     * 
     * @param viewer the jxmapviewer
     */
    public MapClickListener(JXMapViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Gets called on mouseClicked events, calculates the GeoPosition and fires
     * the mapClicked method that the extending class needs to implement.
     * 
     * @param evt the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent evt) {
        final boolean left = SwingUtilities.isLeftMouseButton(evt);
        final boolean singleClick = (evt.getClickCount() == 1);
        if(evt.isConsumed())
            return;

        if ((left && singleClick)) {
            Rectangle bounds = viewer.getViewportBounds();
            int x = bounds.x + evt.getX();
            int y = bounds.y + evt.getY();

            Point2D center = viewer.getCenter();
            double vx =  (x - center.getX());
            double vy = (y - center.getY());

            double alpha = - viewer.getAngle();
            double vx2 = Math.cos(alpha)*vx - Math.sin(alpha)*vy;
            double vy2 = Math.sin(alpha)*vx + Math.cos(alpha)*vy;

            double x2 = center.getX() + vx2;
            double y2 = center.getY() + vy2;

            Point pixelCoordinates = new Point((int)x2,(int) y2);
            mapClicked(viewer.getTileFactory().pixelToGeo(pixelCoordinates, viewer.getZoom()));
        }



    }

    /**
     * This method needs to be implemented in the extending class to handle the
     * map clicked event.
     * 
     * @param location The {@link GeoPosition} of the click event
     */
    public abstract void mapClicked(GeoPosition location);
}
