
package org.jxmapviewer;


import org.jxmapviewer.viewer.TileFactoryInfo;

/*******************************************************************************
 * https://docs.mapbox.com/api/maps/raster-tiles/
 * @author Malte Bahrenburg
 ********************************************************************************/
public class MapboxTileFactoryInfo extends TileFactoryInfo
{


    /**
     * Use road map
     */
//    public final static MVEMode MAP = new MVEMode("map", "map", "r", ".png");

    /**
     * Use satellite map
     */
    public final static RasterTiles SATELLITE = new RasterTiles("mapbox.satellite", ".png");

    /**
     * Use hybrid map
     */

//    public final static MVEMode HYBRID = new MVEMode("hybrid", "hybrid", "h", ".jpeg");

    /**
     * Use dark map
     */
//    public final static MVEMode DARK = new MVEMode("dark", "dark", "cstl=WD", ".png",true);

    /**
     * The map mode
     */
        interface URLProvider
    {
        String getTileUrl(final int x, final int y, final int zoom);
        int getTileSize();
        int getMaxZoomLevel();
    }

    public static class RasterTiles implements URLProvider{
            private final String type;
            private final String format;

        public RasterTiles(String type, String format) {
            this.type = type;
            this.format = format;
        }

        @Override
        public String getTileUrl(int x, int y, int zoom) {
                return "https://api.mapbox.com/v4/" + type + "/" + zoom + "/" + x + "/" + y + format;
        }

        @Override
        public int getTileSize() {
            return 256;
        }

        @Override
        public int getMaxZoomLevel() {
            return 22;
        }
    }

    public static class StaticTiles implements URLProvider{
        private final String username;
        private final String style_id;
        private final boolean zoom;

        public StaticTiles(String username, String style_id, boolean zoom) {
            this.username = username;
            this.style_id = style_id;
            this.zoom = zoom;
        }


        @Override
        public String getTileUrl(int x, int y, int zoom) {
            return "https://api.mapbox.com/styles/v1/" + username + "/"+ style_id +"/tiles/" + zoom + "/" + x + "/" + y + (this.zoom?"@2x":"");
        }

        @Override
        public int getTileSize() {
            return this.zoom?1024:512;
        }

        @Override
        public int getMaxZoomLevel() {
            return this.zoom?21:22;
        }

    }



    private final int MAX_ZOOM_LEVEL;

    private final static int MIN_ZOOM_LEVEL = 0;

    private final int TILE_SIZE;

    private static final int VIRTUAL_ZOOM_LEVELS = 0;

    private final String key;

    private URLProvider mode;

    /**
     * @param mode the mode
     */
    public MapboxTileFactoryInfo(URLProvider mode, String key)
    {
        super("Mapbox", MIN_ZOOM_LEVEL, mode.getMaxZoomLevel()+VIRTUAL_ZOOM_LEVELS, mode.getMaxZoomLevel()+VIRTUAL_ZOOM_LEVELS, mode.getTileSize(), false, false, "", "", "", "");
        setMinNonSuperTileZoomLevel(VIRTUAL_ZOOM_LEVELS);
        this.key = key;
        this.mode = mode;
        this.TILE_SIZE = mode.getTileSize();
        this.MAX_ZOOM_LEVEL = mode.getMaxZoomLevel();
    }
    @Override
    public String getTileUrl(final int x, final int y, final int zoom)
    {
        int invZoom = MAX_ZOOM_LEVEL - zoom + VIRTUAL_ZOOM_LEVELS;
        return mode.getTileUrl(x,y,invZoom) + "?access_token=" + key;
    }

    @Override
    public String getAttribution() {
        return "Mapbox";
    }

    @Override
    public String getLicense() {
        return "https://mapbox.com";
    }


}

