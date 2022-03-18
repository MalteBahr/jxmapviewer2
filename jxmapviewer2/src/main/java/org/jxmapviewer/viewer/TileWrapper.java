package org.jxmapviewer.viewer;

import java.awt.image.BufferedImage;

public class TileWrapper {

    private final Tile tile;
    private final Tile superTile;
    private final TileFactory tileFactory;

    public TileWrapper(Tile tile, Tile superTile, TileFactory tileFactory){
        this.tile = tile;
        this.superTile = superTile;
        this.tileFactory = tileFactory;
    }


    public BufferedImage getImage(){
        if(tile.isLoaded()){
            if(tile.getImage() != null)
                return tile.getImage();
        }else{
            tile.isNeeded();
            if(superTile.getImage() != null)
                return superTile.getImage();
        }
        return EmptyTile.get(tileFactory.getTileSize(tile.getZoom()));
    }





}
