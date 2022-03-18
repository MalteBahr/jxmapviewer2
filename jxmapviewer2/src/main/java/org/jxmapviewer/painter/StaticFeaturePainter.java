package org.jxmapviewer.painter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.geom.Feature;
import org.jxmapviewer.geom.Geometry;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StaticFeaturePainter extends AbstractPainter<JXMapViewer> {

    private final Set<Feature<? extends Geometry>> features = new CopyOnWriteArraySet<>();

    public StaticFeaturePainter(){
        super(true);
    }

    public boolean addFeature(Feature<? extends Geometry> feature){
        boolean b = features.add(feature);
        setDirty(true);
        return b;
    }

    public boolean removeFeature(Feature<? extends Geometry> feature){
        boolean b = features.remove(feature);
        setDirty(true);
        return b;
    }


    @Override
    public void paint(Graphics2D g, JXMapViewer obj, int width, int height) {
        Rectangle viewportBounds = obj.getViewportBounds();
        // TODO: No clipping??

//        if (zoom != obj.getZoom()) {
        if(Math.abs(viewportBounds.getX()- (width*4/3d) - offsetx) > width/2d ||
                Math.abs(viewportBounds.getY()- (height*4/3d) - offsety)>height/2d ||
                zoom != obj.getZoom()){
            setDirty(true);
        }else{
        }
//            setDirty(true);

        offsetxBefore = offsetx;
        offsetyBefore = offsety;
        g.translate(- ( viewportBounds.getX() -offsetxBefore ), - ( viewportBounds.getY() - offsetyBefore));
        super.paint(g, obj, width*4, height*4);
        g.translate(( viewportBounds.getX() - offsetxBefore), (  viewportBounds.getY() - offsetyBefore));
    }

    double offsetxBefore;
    double offsetyBefore;


    int zoom = -1;
    double offsetx;
    double offsety;

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer object, int width, int height) {
        // TODO: No clipping??
        Rectangle viewportBounds = object.getViewportBounds();
        offsetx = viewportBounds.getX()- width/3d;
        offsety = viewportBounds.getY() - height/3d;
        zoom = object.getZoom();
        g.translate(-offsetx, -offsety);


        int count = 0;
        for( Feature<? extends Geometry> f : features){
            if(f.getGeom().getBounds(object.getTileFactory(), object.getZoom()) == null || f.getGeom().getBounds(object.getTileFactory(), object.getZoom()).intersects(viewportBounds)){
                f.doPaint(g,object,width,height);
                count++;
            }
        }
        g.translate(offsetx,offsety);

//        System.out.println("feature count drawn: " + count);
    }
}
