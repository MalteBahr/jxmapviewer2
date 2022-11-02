package org.jxmapviewer.geom;

import javafx.util.Pair;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.util.GeoUtil;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileFactoryCache {

    private final TileFactory tileFactory;
    private HashMap<GeoPosition, Double>[] resolutions;
    private HashMap<GeoPosition, Point2D>[] pixel;
    private Pair<List<GeoPosition>,List<Point2D>>[] lists;

    public TileFactoryCache(int zoomLevels, TileFactory tileFactory) {
        this.tileFactory = tileFactory;
        resolutions = new HashMap[zoomLevels];
        pixel = new HashMap[zoomLevels];
        lists = new Pair[zoomLevels];
        for(int i = 0; i < zoomLevels; i++){
            resolutions[i] = new HashMap<>();
            pixel[i] = new HashMap<>();
        }
    }

    public double geoToResolution(GeoPosition position, int zoom) {
        if(resolutions[zoom].get(position) == null)
            resolutions[zoom].put(position, geoToResolutionPrivate(position, zoom));
        return resolutions[zoom].get(position);
    }

    public double geoToResolutionPrivate(GeoPosition position, int zoom) {
        return GeoUtil.getResolution(position, zoom, tileFactory.getInfo());
    }

    public Point2D geoToPixel(GeoPosition position, int zoom){
        if(pixel[zoom].get(position) == null)
            pixel[zoom].put(position, geoToPixelPrivate(position, zoom));
        return pixel[zoom].get(position);
    }



    public Point2D geoToPixelPrivate(GeoPosition c, int zoomLevel) {
        return GeoUtil.getBitmapCoordinate(c, zoomLevel, tileFactory.getInfo());
    }

    public TileFactory getTileFactory() {
        return tileFactory;
    }


    public List<Point2D> geoToPixel(List<GeoPosition> positions, int zoom){
        if(lists[zoom] == null || lists[zoom].getKey() != positions){
            List<Point2D> points = new ArrayList<>();
            for(GeoPosition position : positions){
                points.add(geoToPixel(position, zoom));
            }

            lists[zoom] = new Pair<>(positions,points);
        }
        return lists[zoom].getValue();
    }
}
