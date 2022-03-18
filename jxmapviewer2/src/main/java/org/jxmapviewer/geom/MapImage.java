package org.jxmapviewer.geom;

import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MapImage {


    private final BufferedImage img;
    private Point2D center;
    private GeoPosition position;




    private double theta = 0;
    private double width = 10;
    private double height = 10;

    public MapImage(BufferedImage image, GeoPosition position){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage compatibleImage = config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);

        this.img = compatibleImage;
        this.position = position;
        this.center = new Point2D.Double(img.getWidth()/2D,img.getHeight()/2D);
    }

    public MapImage(BufferedImage image){
        this.img = image;
    }



    public GeoPosition getPosition() {
        return position;
    }

    public void setPosition(GeoPosition position) {
        this.position = position;
    }

    public double getWidthInMeters() {
        return width;
    }

    public double getHeightInMeters() {
        return height;
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

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center){
        this.center = center;
    }
}
