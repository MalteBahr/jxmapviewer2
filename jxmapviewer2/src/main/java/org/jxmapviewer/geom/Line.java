package org.jxmapviewer.geom;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;

import java.awt.*;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Line implements Geometry{

    private final List<GeoPosition> positionList = new CopyOnWriteArrayList<>();
    private float widthInMeters;
    private TileFactoryCache cachedFactory;
    public Line(List<GeoPosition> positionList, float widthInMeters) {
        this.widthInMeters = widthInMeters;
        this.positionList.addAll(positionList);
    }

    public Line(List<GeoPosition> positionList) {
        this(positionList, -1);
    }

    public Line(){
        this(new ArrayList<GeoPosition>());
    }

    public float getWidthInMeters() {
        return widthInMeters;
    }

    public void setWidthInMeters(float widthInMeters) {
        this.widthInMeters = widthInMeters;
    }



    @Override
    public void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... styles) {

        synchronized (positionList) {
            if (cachedFactory == null || cachedFactory.getTileFactory() != object.getTileFactory()) {
                cachedFactory = new TileFactoryCache(object.getTileFactory().getInfo().getTotalMapZoom()+1, object.getTileFactory());
            }

            if(positionList.size() == 0)
                return;

            float strokeWidth = -1;
            if(widthInMeters != -1){

                 // meters/pixel
                double resolution = (float) cachedFactory.geoToResolution(positionList.get(0),object.getZoom());

                strokeWidth = (float)( getWidthInMeters() / resolution);

                // StrokeWidth(px) = x*(m)/(px)

                //  5m    / 0.5 m/px


            }

            double offx = cachedFactory.geoToPixel(positionList.get(0), object.getZoom()).getX();
            double offy = cachedFactory.geoToPixel(positionList.get(0), object.getZoom()).getY();
            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.translate(offx,offy);
            for (Style style : styles) {
                if (strokeWidth != -1) {
                    style.applyStroke(g2d, strokeWidth);
                }else
                    style.applyStroke(g2d);



                g2d.translate(offx,offy);
                int n = positionList.size();
                int[] xpoints = new int[n];
                int[] ypoints = new int[n];
                for(int i = 0; i < n; i++){
                    Point2D pos = cachedFactory.geoToPixel(positionList.get(i), object.getZoom());
                    xpoints[i] = (int) (Math.round(pos.getX() - offx));
                    ypoints[i] = (int) (Math.round(pos.getY() - offy));
                }


//                Path2D path = new Path2D.Double();
//
//                path.moveTo(xpoints[0], ypoints[0]);
//                for(int i = 1; i < xpoints.length; ++i) {
//                    path.lineTo(xpoints[i], ypoints[i]);
//                }
////                path.closePath();


                g2d.drawPolyline(xpoints, ypoints, n);
                g2d.translate(-offx, -offy);
//                if (positionList.size() > 0) {
//                    Point2D start = object.getTileFactory().geoToPixel(positionList.get(0), object.getZoom());
//                    for (int i = 1; i < positionList.size(); i++) {
//                        Point2D end = object.getTileFactory().geoToPixel(positionList.get(i), object.getZoom());
//                        drawLine(g2d, start, end);
//                        start = end;
//                    }
//                }

                style.applyPoints(g2d, cachedFactory.geoToPixel(positionList,object.getZoom()));

            }
//
            g2d.dispose();
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


    private void drawLine(Graphics2D g, Point2D a, Point2D b){
        g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
    }
}
