package org.jxmapviewer.viewer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

public class EmptyTile extends Tile{
    private final int tilesize;
    private static Color color = Color.GRAY;
    private static BufferedImage img;

    public EmptyTile(int x, int y, int zoom, int tilesize) {
        super(x, y, zoom);
        this.tilesize = tilesize;
    }

    public static void setColor(Color color) {
        EmptyTile.color = color;
    }

    public static BufferedImage get(int tileSize) {
        if(img == null || img.getWidth() != tileSize){
            BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig2 = image.createGraphics();

            ig2.setBackground(color);
            ig2.clearRect(0, 0, tileSize, tileSize);
            img = image;
            return image;
        }
        return img;
    }

    @Override
    public synchronized boolean isLoaded() {
        return true;
    }

    @Override
    synchronized void setLoaded(boolean loaded) {
    }

    @Override
    public synchronized boolean loadingFailed() {
        return false;
    }

    @Override
    synchronized void setLoadingFailed(boolean fail) {

    }

    @Override
    public BufferedImage getImage() {
        BufferedImage img = image.get();
        if(img == null) {

        }
        return img;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void setLoading(boolean isLoading) {
    }
}
