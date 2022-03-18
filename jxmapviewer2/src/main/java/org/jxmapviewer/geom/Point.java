package org.jxmapviewer.geom;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class Point implements Geometry{

    private final GeoPosition position;

    public Point(GeoPosition position) {
        this.position = position;
    }
    public Point(){
        this.position = new GeoPosition(0., 0.);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... styles) {
        Point2D point = object.getTileFactory().geoToPixel(position, object.getZoom());

        for(Style style : styles) {
            style.applyStroke(g);


            int x = (int) point.getX();
            int y = (int) point.getY();
            g.drawLine(x, y, x, y);
        }

    }

    @Override
    public MapImage toImage(List<Style> styles, TileFactory object) {
        return null;
    }

    @Override
    public Rectangle getBounds(TileFactory factory, int zoomLevel) {
        return null;
    }


    public GeoPosition getPosition() {
        return position;
    }
}
