
package org.jxmapviewer.input;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Listens to single mouse clicks on the map and returns the GeoPosition
 *
 * @author Richard Eigenmann
 */
public abstract class MapMouseListener extends MouseAdapter {

    private final JXMapViewer viewer;

    /**
     * Creates a mouse listener for the jxmapviewer which returns the
     * GeoPosition of the the point where the mouse was clicked.
     *
     * @param viewer the jxmapviewer
     */
    public MapMouseListener(JXMapViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * Gets called on mouseClicked events, calculates the GeoPosition and fires
     * the mapClicked method that the extending class needs to implement.
     * 
     * @param evt the mouse event
     */
    @Override
    public void mouseDragged(MouseEvent evt) {

        final boolean left = SwingUtilities.isLeftMouseButton(evt);

        if(evt.isConsumed())
            return;

        if ((left)) {
            if(mouseDragged(getGeoPosition(evt.getX(),evt.getY()))){
                evt.consume();
            }
        }




    }

    @Override
    public void mousePressed(MouseEvent evt) {
        final boolean left = SwingUtilities.isLeftMouseButton(evt);
        if(evt.isConsumed())
            return;

        if ((left)) {
            if(mousePressed(getGeoPosition(evt.getX(),evt.getY()))){
                evt.consume();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        final boolean left = SwingUtilities.isLeftMouseButton(evt);
        if(evt.isConsumed())
            return;

        if ((left)) {
            if(mouseReleased(getGeoPosition(evt.getX(),evt.getY()))){
                evt.consume();
            }
        }
    }

    private GeoPosition getGeoPosition(int screenX, int screenY){
            Rectangle bounds = viewer.getViewportBounds();
            int x = bounds.x + screenX;
            int y = bounds.y + screenY;
            Point pixelCoordinates = new Point(x, y);
        return viewer.getTileFactory().pixelToGeo(pixelCoordinates, viewer.getZoom());
    }


    protected abstract boolean mousePressed(GeoPosition location);

    /**
     * This method needs to be implemented in the extending class to handle the
     * map clicked event.
     * 
     * @param location The {@link GeoPosition} of the click event
     */
    public abstract boolean mouseDragged(GeoPosition location);


    public abstract boolean mouseReleased(GeoPosition location);




}
