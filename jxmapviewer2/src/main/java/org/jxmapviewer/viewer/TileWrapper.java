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

//        ScheduledService<Void> service = new ScheduledService<Void>() {
//            @Override
//            protected Task<Void> createTask() {
//                return new Task<Void>() {
//                    @Override
//                    protected Void call() throws Exception {
//                        System.out.println("tile:"  + (tile.image.get() == null));
//                        System.out.println("supertile:"  + (superTile.image.get() == null));
//                        return null;
//                    }
//                };
//            }
//        };
//        service.setOnFailed((evt) -> {
//            if (evt.getSource().getException() != null) {
//                evt.getSource().getException().printStackTrace();
//            }
//        });
//        service.setPeriod(Duration.millis(5000));
//        service.start();

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
