package org.jxmapviewer.style;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public interface Style {

    void applyStroke(Graphics2D g);
    void applyFill(Graphics2D g);
    void applyPoints(Graphics2D g, List<Point2D> points);

    void applyStroke(Graphics2D g, float strokeWidth);
}
