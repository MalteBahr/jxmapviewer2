package org.jxmapviewer.viewer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

public class VirtualTile extends Tile {
    private static final Logger log = LoggerFactory.getLogger(VirtualTile.class);
    private final Tile t;
    private final int tilesize;
    private SoftReference<BufferedImage> resizedImage;

    public VirtualTile(int tpx, int tpy, int zoom, Tile t, int tilesize) {
        super(tpx,tpy,zoom);
        log.trace(tpx + " " +tpy + " " +zoom + " " +t+ " " +tilesize);
        this.t = t;
        this.tilesize = tilesize;
    }

    @Override
    public synchronized boolean isLoaded() {
        return t.isLoaded();
    }

    @Override
    synchronized void setLoaded(boolean loaded) {
        t.setLoaded(loaded);
    }

    @Override
    public synchronized boolean loadingFailed() {
        return t.loadingFailed();
    }

    @Override
    synchronized void setLoadingFailed(boolean fail) {
        t.setLoadingFailed(fail);
    }

    @Override
    public BufferedImage getImage() {

        if(resizedImage != null && resizedImage.get() != null)
            return resizedImage.get();

        int factor = (int)Math.pow(2, t.getZoom() - getZoom());
        int offX = (getX() % factor) * tilesize / factor;
        int offY = (getY() % factor) * tilesize / factor;

        BufferedImage img = t.getImage();

        if(img != null) {

            BufferedImage res = img.getSubimage(offX, offY, tilesize / factor, tilesize / factor);


            resizedImage = new SoftReference<>(resize(res, tilesize, tilesize));
            return resizedImage.get();
        }
        return null;
    }
    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        log.trace("resize operation" + this);
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }


    @Override
    public boolean isLoading() {
        return t.isLoading();
    }

    @Override
    public void setLoading(boolean isLoading) {
        t.setLoading(isLoading);
    }

    @Override
    public Priority getPriority() {
        return t.getPriority();
    }

    @Override
    public void setPriority(Priority priority) {
        t.setPriority(priority);
    }

}
