package org.jxmapviewer.geom;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.viewer.TileFactory;

import java.awt.*;
import java.util.List;

public interface Geometry {

    void paint(Graphics2D g, JXMapViewer object, int width, int height, Style... style);

    MapImage toImage(List<Style> styles, TileFactory object);

    Rectangle getBounds(TileFactory factory, int zoomLevel);

//    Rectangle getBounds(TileFactory factory, int zoomLevel);

}
