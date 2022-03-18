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
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Polygon implements Geometry {

    private static final double EPSILON_PIXEL_DISTANCE = 5;
    private final List<GeoPosition> positionList = new CopyOnWriteArrayList<>();
    private final List<List<GeoPosition>> fillRectangles = new CopyOnWriteArrayList<>();

    private static HashMap<Integer, Double> zoomToEpsilon;

    static {
        zoomToEpsilon = new HashMap<Integer, Double>() {{
            put(0, 0D);
            put(1, 2D);
            put(2, 4D);
            put(3, 5D);
            put(4, 7D);
            put(5, 9D);
            put(6, 11D);
            put(7, 14D);
            put(8, 17D);
            put(9, 20D);
            put(10, 11D);
            put(11, 12D);
            put(12, 12D);
            put(13, 13D);
            put(14, 14D);
            put(15, 15D);
            put(16, 16D);
            put(17, 17D);
            put(18, 18D);
            put(19, 19D);
            put(20, 20D);
        }};
    }

    private final HashMap<Pair<Integer,TileFactory>, List<Point2D>> polygonHashMap = new HashMap<>();
    private final HashMap<Pair<Integer,TileFactory>, List<List<Point2D>>> fillHashMap = new HashMap<>();
    private final HashMap<Pair<Integer,TileFactory>, List<Point2D>> cache = new HashMap<>();

    public Polygon(List<GeoPosition> positionList) {
        this(positionList, new ArrayList<List<GeoPosition>>());
    }

    public Polygon(List<GeoPosition> positionList, List<List<GeoPosition>> fillRectangles) {
        this.positionList.addAll(positionList);
        this.fillRectangles.addAll(fillRectangles);
    }


    public Polygon() {

    }

    public List<GeoPosition> getPositionList() {
        return Collections.unmodifiableList(positionList);
    }


//    private HashMap<Integer, BufferedImage> imageCache = new HashMap<>();

    private boolean cachable;
    private BufferedImage cachedImg;
    private int x;
    private int y;

    public void setCacheable(boolean b){
        cachable = b;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... styles) {

        long clippingTime = System.nanoTime();

        List<Point2D> points = getPolygon(object.getTileFactory(), object.getZoom());

        Rectangle rectangle = g.getClipBounds();

        double delta = 5;

        List<Point2D> clipping = Arrays.asList(new Point2D[]{
                new Point2D.Double(rectangle.getX()- delta, rectangle.getY() - delta),
                new Point2D.Double(rectangle.getX() + rectangle.getWidth() + delta, rectangle.getY()-delta),
                new Point2D.Double(rectangle.getX() + rectangle.getWidth() + delta, rectangle.getY() + rectangle.getHeight() + delta),
                new Point2D.Double(rectangle.getX() - delta, rectangle.getY() + rectangle.getHeight() + delta)
        });

        points = clipPolygon(points, clipping);

//        System.out.println("first: " + points.get(0) + "last: " + points.get(points.size()-1)) ;

        if(points.size() == 0)
            return;

        List<List<Point2D>> fill = getFill(object.getTileFactory(), object.getZoom());
        List<List<Point2D>> clippedFill = new ArrayList<>();

        for (List<Point2D> fillPoints : fill) {
            clippedFill.add(clipPolygon(fillPoints, clipping));
        }



        double offx = points.get(0).getX();
        double offy = points.get(0).getY();

        int[] xpoints = new int[points.size()];
        int[] ypoints = new int[points.size()];

        for (int i = 0; i < points.size(); i++) {
            xpoints[i] = (int) (points.get(i).getX() - offx);
            ypoints[i] = (int) (points.get(i).getY() - offy);
        }


        clippingTime = System.nanoTime() - clippingTime;

        long drawingTime = System.nanoTime();

//        List<Point2D> points = getPoints(object.getTileFactory(), object.getZoom());
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.translate(offx, offy);

//        long geopositionTranslation = System.nanoTime();
//        geopositionTranslation = System.nanoTime() - geopositionTranslation;
//
//        if (points.size() > 0) {
//            long fillarray = System.nanoTime();
//


//            Point2D offset = points.get(0);
//            double off_x = offset.getX();
//            double off_y = offset.getY();
//
//
//            int[] xpoints = new int[points.size()];
//            int[] ypoints = new int[points.size()];
//
//            for (int i = 0; i < points.size(); i++) {
//                xpoints[i] = (int) (points.get(i).getX() - off_x);
//                ypoints[i] = (int) (points.get(i).getY() - off_y);
//            }
//            fillarray = System.nanoTime() - fillarray;
//
//            long translate = System.nanoTime();
//
//            g2d.translate((int) off_x, (int) off_y);
//            translate = System.nanoTime() - translate;
//
//            long drawPolygon = System.nanoTime();
            for (Style style : styles) {

                style.applyStroke(g2d);
                g2d.drawPolygon(xpoints, ypoints, xpoints.length);

//                drawPolygon(g2d, xpoints, ypoints, points.size(), zoomToEpsilon.get(object.getZoom()));
//                drawPolygon = System.nanoTime() - drawPolygon;


//                long drawFill = System.nanoTime();

                style.applyFill(g2d);

                if (fillRectangles.size() != 0) {

                    for (List<Point2D> polyPoints : clippedFill) {
                        int[] xs = new int[polyPoints.size()];
                        int[] ys = new int[polyPoints.size()];

                        for (int i = 0; i < polyPoints.size(); i++) {
                            xs[i] = (int) (polyPoints.get(i).getX() - offx);
                            ys[i] = (int) (polyPoints.get(i).getY() - offy);
                        }
                        g2d.fillPolygon( xs, ys, polyPoints.size());
                    }
                } else {
                    g2d.fillPolygon(xpoints, ypoints, points.size());
                }
//                if (fillRectangles.size() != 0) {
//                    for (GeoPosition[] rect : fillRectangles) {
//                        List<Point2D> rectPoints = new ArrayList<>();
//                        for (GeoPosition position : rect) {
//                            rectPoints.add(object.getTileFactory().geoToPixel(position, object.getZoom()));
//                        }
//                        int[] xs = new int[rectPoints.size()];
//                        int[] ys = new int[rectPoints.size()];
//
//                        for (int i = 0; i < rectPoints.size(); i++) {
//                            xs[i] = (int) (rectPoints.get(i).getX() - off_x);
//                            ys[i] = (int) (rectPoints.get(i).getY() - off_y);
//                        }
//                        fillPolygon(g2d, xs, ys, rectPoints.size());
//                    }
//                } else {
//                    fillPolygon(g2d, xpoints, ypoints, points.size());
//                }
//                drawFill = System.nanoTime() - drawFill;


//                long applyPoints = System.nanoTime();


//                applyPoints = System.nanoTime() - applyPoints;


//            System.out.println("geopositionTranslation: " + geopositionTranslation + " fillarray: " + fillarray + " "
//                    + " translate: " + translate + " drawPolygon: " + drawPolygon
//                    + " drawFill: " + drawFill + " applyPoints: " + applyPoints);
            }
            g2d.translate(-offx,- offy);

            for(Style style : styles){
                style.applyPoints(g2d, getPoints(object.getTileFactory(), object.getZoom()));
            }
//            g2d.translate(-(int) off_x, -(int) off_y);
            g2d.dispose();

        drawingTime = System.nanoTime() - drawingTime;
//        System.out.println("clippingtime: " + clippingTime + " drawingTIme:" + drawingTime + " fraction: " + clippingTime/(double)drawingTime);
    }

    private List<List<Point2D>> getFill(TileFactory factory, int zoom) {

        if(fillHashMap.get(new Pair<>(zoom,factory)) == null) {
            List<List<Point2D>> fill = new ArrayList<>();
            for (List<GeoPosition> lst : fillRectangles) {
                List<Point2D> points = new ArrayList<>();
                for (GeoPosition position : lst) {
                    points.add(factory.geoToPixel(position, zoom));
                }

                RamerDouglasPeucker.douglasPeucker(points, EPSILON_PIXEL_DISTANCE);
                fill.add(points);
            }
            fillHashMap.put(new Pair<>(zoom,factory), fill);
        }

        return fillHashMap.get(new Pair<>(zoom,factory));
    }

    private List<Point2D> getPolygon(TileFactory tileFactory, int zoom) {

        if(polygonHashMap.get(new Pair<>(zoom,tileFactory)) == null) {
            List<Point2D> points = getPoints(tileFactory, zoom);
            points = RamerDouglasPeucker.douglasPeucker(points, EPSILON_PIXEL_DISTANCE);
            polygonHashMap.put(new Pair<>(zoom,tileFactory), points);
        }
        return polygonHashMap.get(new Pair<>(zoom,tileFactory));
    }

    @Override
    public MapImage toImage(List<Style> styles, TileFactory object) {


        List<Point2D> points = new ArrayList<>();
        for (GeoPosition position : positionList) {
            points.add(object.geoToPixel(position, object.getInfo().getMinNonSuperTileZoom()));
        }

        double minx = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;

        double miny = Double.MAX_VALUE;
        double maxy = Double.MIN_VALUE;

        for (Point2D point2D : points) {
            minx = Math.min(point2D.getX(), minx);
            miny = Math.min(point2D.getY(), miny);
            maxx = Math.max(point2D.getX(), maxx);
            maxy = Math.max(point2D.getY(), maxy);
        }

        BufferedImage bufferedImage = new BufferedImage((int) (maxx - minx), (int) (maxy - miny), BufferedImage.TYPE_INT_ARGB);
        System.out.println("img size: " + bufferedImage.getWidth() + " " + bufferedImage.getHeight());
        Graphics2D g = bufferedImage.createGraphics();

        for (Style style : styles) {
            if (points.size() > 0) {


                int[] xpoints = new int[points.size()];
                int[] ypoints = new int[points.size()];

                for (int i = 0; i < points.size(); i++) {
                    xpoints[i] = (int) (points.get(i).getX() - minx);
                    ypoints[i] = (int) (points.get(i).getY() - miny);
                }
                Graphics2D g2d = (Graphics2D) g.create();
                style.applyStroke(g2d);
                drawPolygon(g2d, xpoints, ypoints, points.size(), EPSILON_PIXEL_DISTANCE);
                style.applyFill(g2d);

                if (fillRectangles.size() != 0) {
                    for (List<GeoPosition> poly : fillRectangles) {
                        List<Point2D> polyPoints = new ArrayList<>();
                        for (GeoPosition position : poly) {
                            polyPoints.add(object.geoToPixel(position, object.getInfo().getMinNonSuperTileZoom()));
                        }
                        int[] xs = new int[polyPoints.size()];
                        int[] ys = new int[polyPoints.size()];

                        for (int i = 0; i < polyPoints.size(); i++) {
                            xs[i] = (int) (polyPoints.get(i).getX() - minx);
                            ys[i] = (int) (polyPoints.get(i).getY() - miny);
                        }
                        fillPolygon(g2d, xs, ys, polyPoints.size());
                    }
                } else {
                    fillPolygon(g2d, xpoints, ypoints, points.size());
                }
                g2d.dispose();


                g2d = (Graphics2D) g.create();
                style.applyPoints(g2d, points);
                g2d.dispose();
            }

        }
        GeoPosition position = object.pixelToGeo(new Point2D.Double(minx + ((maxx - minx) / 2d), miny + ((maxy - miny) / 2d)), object.getInfo().getMinNonSuperTileZoom());
        MapImage image = new MapImage(bufferedImage, position);
        double resolution_meters_per_pixel = object.geoToResolution(position, object.getInfo().getMinNonSuperTileZoom());

        image.setHeight(resolution_meters_per_pixel * image.getImage().getHeight());
        image.setWidth(resolution_meters_per_pixel * image.getImage().getWidth());
        return image;
    }

    @Override
    public Rectangle getBounds(TileFactory factory, int zoomLevel) {

        double minx = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;

        double miny = Double.MAX_VALUE;
        double maxy = Double.MIN_VALUE;


        for (Point2D point2D : getPoints(factory, zoomLevel)) {
            minx = Math.min(point2D.getX(), minx);
            miny = Math.min(point2D.getY(), miny);
            maxx = Math.max(point2D.getX(), maxx);
            maxy = Math.max(point2D.getY(), maxy);
        }

        return new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));
    }

    private List<Point2D> getPoints(TileFactory factory, int zoomLevel) {
        if (cache.get(new Pair<>(zoomLevel,factory)) == null) {
            List<Point2D> points = new ArrayList<>();
            for (GeoPosition position : positionList) {
                points.add(factory.geoToPixel(position, zoomLevel));
            }
            cache.put(new Pair<>(zoomLevel,factory), points);
        }
        return cache.get(new Pair<>(zoomLevel,factory));
    }

//    private BufferedImage getImage(List<Style> styles, TileFactory factory, int zoomlevel) {
//
//        BufferedImage image = getImage(styles, factory);
//
//
//
//        MapImage mapImage = toImage(styles, factory);
//        imageCache.put(zoomlevel,mapImage.getImage());
//        return mapImage.getImage();
//    }
//
//    private BufferedImage getImage(List<Style> styles, TileFactory factory) {
//        if(fullImage == null) {
//            MapImage mapImage = toImage(styles, factory);
//            fullImage = mapImage.getImage();
//        }
//        return fullImage;
//    }


    private void fillPolygon(Graphics2D g, int[] xs, int[] ys, int n) {

//        java.awt.Polygon p = new java.awt.Polygon(xs, ys, n);
//        Area a = new Area(p);

//        g.fill();
//        g.fillPolygon(xs,ys,n);
//        java.awt.Polygon polygon = new java.awt.Polygon(xs, ys, n);
//        g.fill(polygon);
    }


    private void drawPolygon(Graphics2D g, int[] xs, int[] ys, int n, double epsilon) {
        long time = System.nanoTime();
//        System.out.println("clip: " + g.getClipBounds());

        java.awt.Polygon polygon = new java.awt.Polygon(xs, ys, n);

        List<Point2D> polygonLst = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            polygonLst.add(new Point2D.Double(xs[i], ys[i]));
        }

        Rectangle rectangle = g.getClipBounds();
        List<Point2D> clip = new ArrayList<>();
        clip.add(new Point2D.Double(rectangle.getX(), rectangle.getY()));
        clip.add(new Point2D.Double(rectangle.getX() + rectangle.getWidth(), rectangle.getY()));
        clip.add(new Point2D.Double(rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight()));
        clip.add(new Point2D.Double(rectangle.getX(), rectangle.getY() + rectangle.getHeight()));

        long timeClip = System.nanoTime();

        RamerDouglasPeucker.douglasPeucker(polygonLst, epsilon);

        polygonLst = clipPolygon(polygonLst, clip);
        timeClip = System.nanoTime() - timeClip;

        int[] xs2 = new int[polygonLst.size()];
        int[] ys2 = new int[polygonLst.size()];

        for (int i = 0; i < polygonLst.size(); i++) {
            xs2[i] = (int) polygonLst.get(i).getX();
            ys2[i] = (int) polygonLst.get(i).getY();
        }
//        System.out.println("clipped polygon size: " + xs2.length);

        time = System.nanoTime() - time;


        long polygont = System.nanoTime();

        g.drawPolygon(xs2, ys2, xs2.length);

        polygont = System.nanoTime() - polygont;

//        System.out.println("time taken: Clipping+allocation: " + time +  " clipping only: " + timeClip+ " drawing: " + polygont);

    }


    private List<Point2D> clipPolygon(List<Point2D> subject, List<Point2D> clipper) {
        List<Point2D> result = new ArrayList<>(subject);
        int len = clipper.size();
        for (int i = 0; i < len; i++) {

            int len2 = result.size();
            List<Point2D> input = result;
            result = new ArrayList<>(len2);

            Point2D A = clipper.get((i + len - 1) % len);
            Point2D B = clipper.get(i);

            for (int j = 0; j < len2; j++) {

                Point2D P = input.get((j + len2 - 1) % len2);
                Point2D Q = input.get(j);

                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P))
                        result.add(intersection(A, B, P, Q));
                    result.add(Q);
                } else if (isInside(A, B, P))
                    result.add(intersection(A, B, P, Q));
            }
        }
        return result;
    }

    private boolean isInside(Point2D a, Point2D b, Point2D c) {
        return (a.getX() - c.getX()) * (b.getY() - c.getY()) > (a.getY() - c.getY()) * (b.getX() - c.getX());
    }

    private Point2D intersection(Point2D a, Point2D b, Point2D p, Point2D q) {
        double A1 = b.getY() - a.getY();
        double B1 = a.getX() - b.getX();
        double C1 = A1 * a.getX() + B1 * a.getY();

        double A2 = q.getY() - p.getY();
        double B2 = p.getX() - q.getX();
        double C2 = A2 * p.getX() + B2 * p.getY();

        double det = A1 * B2 - A2 * B1;
        double x = (B2 * C1 - B1 * C2) / det;
        double y = (A1 * C2 - A2 * C1) / det;

        return new Point2D.Double(x, y);
    }


//    private void drawLine(Graphics2D g, Point2D a, Point2D b){
//        g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
//    }
}
