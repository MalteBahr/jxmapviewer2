package org.jxmapviewer.painter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.geom.Feature;
import org.jxmapviewer.geom.Geometry;
import org.jxmapviewer.viewer.GeoPosition;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CachedPainter extends AbstractPainter<JXMapViewer> {
    private final Set<Feature<? extends Geometry>> features = new CopyOnWriteArraySet<>();
    private final double ms;
    private long time = System.currentTimeMillis();
    private BufferedImage bufferedImage;
    private Rectangle viewportBounds;
    public CachedPainter(long ms){
        this.ms = ms;
    }

    public boolean addFeature(Feature<? extends Geometry> feature){
        return features.add(feature);
    }

    public boolean removeFeature(Feature<? extends Geometry> feature){
        return features.remove(feature);
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer object, int width, int height) {

        throw new UnsupportedOperationException();

//        int count = 0;
//        if(System.currentTimeMillis() - time > ms || bufferedImage == null ) {
//            time = System.currentTimeMillis();
//            System.out.println(count++);
//            viewportBounds = object.getViewportBounds();
//            System.out.println(count++);
//            bufferedImage = new BufferedImage((int)5000, (int)5000, BufferedImage.TYPE_INT_ARGB);
//
//            System.out.println(count++);
//            Graphics2D graphics2D = bufferedImage.createGraphics();
//
//            graphics2D.setBackground(Color.white);
//
//            System.out.println(count++);
//            ;
//            System.out.println(count++);
//            graphics2D.setClip(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
//            System.out.println(count++);
//
//            graphics2D.translate(-viewportBounds.getX(), -viewportBounds.getY());
//            System.out.println(count++);
//            for (Feature<? extends Geometry> f : features) {
//                //TODO new bounds
//                if (f.getGeom().getBounds(object.getTileFactory(), object.getZoom()) == null || f.getGeom().getBounds(object.getTileFactory(), object.getZoom()).intersects(increaseViewerBounds(viewportBounds))) {
//                    f.doPaint(graphics2D, object, width, height);
//                }
//            }
//            System.out.println(count++);
//            graphics2D.translate(viewportBounds.getX(), viewportBounds.getY());
//            System.out.println(count++);
//            graphics2D.dispose();
//            System.out.println(count++);
//        }
//
//        System.out.println("getX: " + viewportBounds.getX());
//        g.translate(-viewportBounds.getX(),- viewportBounds.getY());
//
////        for (int i = -100; i < 100; i++) {
//
//        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D graphics2D = image.createGraphics();
//
//
//            graphics2D.setColor(new Color(255, 255, 255, 255));
//            graphics2D.setStroke(new BasicStroke(20));
//        System.out.println("geotopixel: " + object.getTileFactory().geoToPixel(new GeoPosition(53., 9.), object.getZoom()));
//            graphics2D.drawLine((int) object.getTileFactory().geoToPixel(new GeoPosition(53.,9.), object.getZoom()).getX(), (int) object.getTileFactory().geoToPixel(new GeoPosition(53.,9.), object.getZoom()).getY(), (int) object.getTileFactory().geoToPixel(new GeoPosition(54.,9.), object.getZoom()).getX(), (int) object.getTileFactory().geoToPixel(new GeoPosition(54.,9.), object.getZoom()).getY());
//
////        }
//        graphics2D.dispose();
//        g.translate(viewportBounds.getX(), viewportBounds.getY());
//        g.dispose();
//        System.out.println("feature count drawn: " + count);
    }


//    @Override
//    protected void doPaint(Graphics2D g, JXMapViewer object, int width, int height) {
//        Rectangle viewportBounds = object.getViewportBounds();
//
//        // TODO: No clipping??
//
//
//        System.out.println(g.getClip());
//
//        g.translate(-viewportBounds.getX(), -viewportBounds.getY());
//
//        int count = 0;
//        for( Feature<? extends Geometry> f : features){
//            if(f.getGeom().getBounds(object.getTileFactory(), object.getZoom()) == null || f.getGeom().getBounds(object.getTileFactory(), object.getZoom()).intersects(increaseViewerBounds(viewportBounds))){
//                f.doPaint(g,object,width,height);
//                count++;
//            }
//        }
//        g.translate(viewportBounds.getX(), viewportBounds.getY());
////        System.out.println("feature count drawn: " + count);
//    }

    private Rectangle increaseViewerBounds(Rectangle viewportBounds) {
        Rectangle rectangle = new Rectangle(viewportBounds);
        rectangle.grow(viewportBounds.width,viewportBounds.height);
        return rectangle;
    }
}
