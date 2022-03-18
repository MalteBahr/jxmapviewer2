package org.jxmapviewer.geom;

import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FixedImage {

    private final BufferedImage img;
    private Point2D center;
    private Point location;
    private double theta = 0;

    public FixedImage(BufferedImage image, int x, int y){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage compatibleImage = config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);

        this.img = compatibleImage;
        this.location = new Point(x, y);
        this.center = new Point2D.Double(img.getWidth()/2D,img.getHeight()/2D);
    }



    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public BufferedImage getImage() {
        return img;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center){
        this.center = center;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(int x, int y) {
        this.location = new Point(x, y);
    }

    public BufferedImage getImg() {
        return img;
    }
}
