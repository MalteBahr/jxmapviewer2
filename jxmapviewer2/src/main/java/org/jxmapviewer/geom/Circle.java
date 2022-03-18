package org.jxmapviewer.geom;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Circle extends Point{

    private final int radius;

    public Circle(GeoPosition position, int radius) {
        super(position);
        this.radius = radius;
    }

    public Circle(){
        super(new GeoPosition(0., 0.));
        this.radius = 5;
    }


    @Override
    public void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... styles) {
        Point2D point = object.getTileFactory().geoToPixel(getPosition(), object.getZoom());

        for(Style style : styles) {
            Graphics2D g2d = (Graphics2D) g.create();



            style.applyFill(g2d);
            paintFill(g2d, point);

            g2d.dispose();
            g2d = (Graphics2D) g.create();

            style.applyStroke(g2d);
            paintStroke(g2d, point);
            g2d.dispose();
        }
    }

    private void paintFill(Graphics2D g, Point2D point) {

        int x = (int) point.getX();
        int y = (int) point.getY();

        Ellipse2D ellipse2D = new Ellipse2D.Double(x - radius / 2d, y - radius / 2d, radius, radius);

        g.fill(ellipse2D);    }

    private void paintStroke(Graphics2D g, Point2D point) {

        int x = (int) point.getX();
        int y = (int) point.getY();

        Ellipse2D ellipse2D = new Ellipse2D.Double(x - radius / 2d, y - radius / 2d, radius, radius);

        g.draw(ellipse2D);
    }
}
