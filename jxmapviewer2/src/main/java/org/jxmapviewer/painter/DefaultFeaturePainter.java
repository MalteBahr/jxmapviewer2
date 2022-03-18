package org.jxmapviewer.painter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.geom.Feature;
import org.jxmapviewer.geom.Geometry;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultFeaturePainter extends AbstractPainter<JXMapViewer> {

    private final Set<Feature<? extends Geometry>> features = new CopyOnWriteArraySet<>();


    public boolean addFeature(Feature<? extends Geometry> feature){
        return features.add(feature);
    }

    public boolean removeFeature(Feature<? extends Geometry> feature){
        return features.remove(feature);
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer object, int width, int height) {
        Rectangle viewportBounds = object.getViewportBounds();

        // TODO: No clipping??

        g.translate(-viewportBounds.getX(), -viewportBounds.getY());

        int count = 0;
        for( Feature<? extends Geometry> f : features){
            if(f.getGeom().getBounds(object.getTileFactory(), object.getZoom()) == null || f.getGeom().getBounds(object.getTileFactory(), object.getZoom()).intersects(viewportBounds)){
                f.doPaint(g,object,width,height);
                count++;
            }
        }
        g.translate(viewportBounds.getX(), viewportBounds.getY());
//        System.out.println("feature count drawn: " + count);
    }
}
