package org.jxmapviewer.painter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.geom.FixedImage;
import org.jxmapviewer.geom.MapImage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DefaultImagePainter extends AbstractPainter<JXMapViewer>{

    private final Map<Integer, Double> zoomToMPPixel;

    private List<MapImage> images = new ArrayList<>();
    private List<FixedImage> fixedImages = new ArrayList<>();

    public DefaultImagePainter(){
       zoomToMPPixel =  new HashMap<Integer,Double>() {{
            put(0, 156412D);
            put(1, 78206D);
            put(2, 39103D);
            put(3, 19551D);
            put(4, 9776D);
            put(5, 4888D);
            put(6, 2444D);
            put(7, 1222D);
            put(8, 610.984D);
            put(9, 305.492D);
            put(10, 152.746D);
            put(11, 76.373D);
            put(12, 38.187D);
            put(13, 19.093D);
            put(14, 9.547D);
            put(15, 4.773D);
            put(16, 2.387D);
            put(17, 1.193D);
            put(18, 0.596D);
            put(19, 0.298D);
            put(20, 0.149D);
        }};
    }


    public boolean addImage(MapImage image){
        return images.add(image);
    }

    public boolean removeImage(MapImage image){
        return images.remove(image);
    }

    public boolean addImage(FixedImage image){
        return fixedImages.add(image);
    }

    public boolean removeImage(FixedImage image){
        return fixedImages.remove(image);
    }



    @Override
    protected void doPaint(Graphics2D g, JXMapViewer jxMapViewer, int width, int height) {
        Rectangle viewportBounds = jxMapViewer.getViewportBounds();

        g.translate(-viewportBounds.getX(), -viewportBounds.getY());
        for( MapImage mapImage : images) {

            Point2D center_on_map = jxMapViewer.getTileFactory().geoToPixel(
                    mapImage.getPosition(), jxMapViewer.getZoom());


            double resolution = jxMapViewer.getTileFactory().geoToResolution(mapImage.getPosition(),jxMapViewer.getZoom());

            // 10m = r(m/p) * x(p)
            // 10m/r(m/p) = x(p)

//
//            button.setLocation(buttonX - button.getWidth() / 2, buttonY - button.getHeight() / 2);
//
//            double scale = 400000 / zoomToMPPixel.get(jxMapViewer.getZoom());

            double scaleWidth = (mapImage.getWidthInMeters()/mapImage.getImage().getWidth()) / resolution;
            double scaleHeight = (mapImage.getHeightInMeters()/mapImage.getImage().getHeight()) / resolution;

//            System.out.println(1/scaleWidth);

            AffineTransform at = new AffineTransform();
            at.rotate(mapImage.getTheta(),scaleWidth*mapImage.getCenter().getX(),scaleHeight* mapImage.getCenter().getY());
            at.scale(scaleWidth,scaleHeight);

            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            g.drawImage(mapImage.getImage(),
                    scaleOp,
                    (int) (center_on_map.getX() - (scaleWidth * mapImage.getCenter().getX())),
                    (int) (center_on_map.getY()- (scaleHeight * mapImage.getCenter().getY()))
            );
        }
        Point2D center_on_map = jxMapViewer.getCenter();
        for (FixedImage image : fixedImages) {

            Point2D centerAdj = new Point2D.Double(
                    center_on_map.getX() + image.getLocation().x,
                    center_on_map.getY() + image.getLocation().y);
            AffineTransform at = new AffineTransform();
            at.rotate(image.getTheta(),image.getCenter().getX(),image.getCenter().getY());
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            g.drawImage(image.getImg(),scaleOp,
                    (int) (centerAdj.getX() - image.getCenter().getX()),
                    (int) (centerAdj.getY() - image.getCenter().getY()));
        }
        g.translate(viewportBounds.getX(), viewportBounds.getY());

    }



    public Image scaleAndRotate(BufferedImage image, int sizeX, int sizeY, double degrees) {

        double scaleWidth = (double)sizeX / (double)image.getWidth();
        double scaleHeight = (double)sizeY / (double)image.getHeight();
        BufferedImage img = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        AffineTransform at = new AffineTransform();
        at.scale(scaleWidth, scaleHeight);
        at.rotate(Math.toRadians(degrees), image.getWidth() / 2d, image.getHeight() / 2d);
        g2d.setTransform(at);
        g2d.drawImage(image, sizeX / 2, sizeY / 2, null);
        g2d.dispose();

        return img;

    }
}
