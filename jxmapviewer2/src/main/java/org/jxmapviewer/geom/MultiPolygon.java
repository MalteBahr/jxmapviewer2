package org.jxmapviewer.geom;

import javafx.util.Pair;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.util.RamerDouglasPeucker;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class MultiPolygon implements Geometry {

    private static final double EPSILON_PIXEL_DISTANCE = 5;
    private final List<Polygon> polygons = new CopyOnWriteArrayList<>();

    private static HashMap<Integer, Double> zoomToEpsilon;

    public MultiPolygon(List<Polygon> polygons) {
        this.polygons.addAll(polygons);
    }


    public MultiPolygon() {
        this(List.of());
    }


    public List<Polygon> getPolygons() {
        return Collections.unmodifiableList(polygons);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... styles) {
        for (Polygon polygon : polygons) {
            polygon.paint(g, object, width, height, styles);
        }
    }

    @Override
    public MapImage toImage(List<Style> styles, TileFactory object) {
        return null;
    }

    @Override
    public Rectangle getBounds(TileFactory factory, int zoomLevel) {

        double minx = Double.MAX_VALUE;
        double maxx = -Double.MAX_VALUE;

        double miny = Double.MAX_VALUE;
        double maxy = -Double.MAX_VALUE;


        List<Rectangle> bounds = polygons.stream().map(p -> p.getBounds(factory, zoomLevel)).toList();


        for (Rectangle rectangle : bounds) {
            minx = Math.min(rectangle.getX(), minx);
            miny = Math.min(rectangle.getY(), miny);
            maxx = Math.max(rectangle.getX() + rectangle.getWidth(), maxx);
            maxy = Math.max(rectangle.getY() + rectangle.getHeight(), maxy);
        }

        return new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));
    }
}
