package org.jxmapviewer.geom;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Feature<T extends Geometry> {

    private T geom;
    private final List<Style> styles = new CopyOnWriteArrayList<>();
    Callback callback = null;


    public Feature(T geom, Style... styles){
        this.geom = geom;
        this.styles.addAll(Arrays.asList(styles));
    }


    public Feature(T geom){
        this.geom = geom;
    }


    public void setStyles(Style... style){
        this.styles.clear();
        this.styles.addAll(Arrays.asList(style));
    }

    public List<Style> getStyles(){
        return styles;
    }



    public T getGeom() {
        return geom;
    }

    public void setGeom(T geom) {
        this.geom = geom;
    }

    public void doPaint(Graphics2D g, JXMapViewer object, int width, int height){

        for(Style style : styles) {
            Graphics2D g2d = (Graphics2D) g.create();
            geom.paint(g2d, object, width, height, style);
            g2d.dispose();
        }
        if(callback != null){
            callback.call(geom.toImage(styles, object.getTileFactory()));
            callback = null;
        }
    }

    public void toImage(Callback callback){
        this.callback = callback;
    }

    public interface Callback{
        void call(MapImage image);
    }
}
