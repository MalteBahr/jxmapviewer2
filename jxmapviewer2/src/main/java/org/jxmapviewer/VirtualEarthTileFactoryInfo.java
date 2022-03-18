
package org.jxmapviewer;

import org.jxmapviewer.viewer.TileFactoryInfo;

/*******************************************************************************
 * http://www.viavirtualearth.com/vve/Articles/RollYourOwnTileServer.ashx
 * @author Fabrizio Giudici
 * @version $Id: MicrosoftVirtualEarthProvider.java 115 2007-11-08 22:04:36Z
 * fabriziogiudici $
 ********************************************************************************/
public class VirtualEarthTileFactoryInfo extends TileFactoryInfo
{
    /**
     * Use road map
     */
    public final static MVEMode MAP = new MVEMode("map", "map", "r", ".png");

    /**
     * Use satellite map
     */
    public final static MVEMode SATELLITE = new MVEMode("satellite", "satellite", "a", ".jpeg");

    /**
     * Use hybrid map
     */
    public final static MVEMode HYBRID = new MVEMode("hybrid", "hybrid", "h", ".jpeg");

    /**
     * Use dark map
     */
    public final static MVEMode DARK = new MVEMode("dark", "dark", "cstl=WD", ".png",true);

    /**
     * The map mode
     */
    public static class MVEMode
    {
        private String type;
        private String ext;
        private String name;
        private String label;
        private boolean newURL;

        private MVEMode(final String name, final String label, final String type, final String ext)
        {
            this(name, label, type, ext,false);
        }
        private MVEMode(final String name, final String label, final String type, final String ext, boolean newURL){
            this.type = type;
            this.ext = ext;
            this.name = name;
            this.label = label;
            this.newURL = newURL;
        }

    }

    private final static int TOP_ZOOM_LEVEL = 19;

    private final static int MAX_ZOOM_LEVEL = 18;

    private final static int MIN_ZOOM_LEVEL = 0;

    private final static int TILE_SIZE = 256;

    private static final int VIRTUAL_ZOOM_LEVELS = 3;


    private MVEMode mode;

    /**
     * @param mode the mode
     */
    public VirtualEarthTileFactoryInfo(MVEMode mode)
    {
        super("Virtual Earth", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL+VIRTUAL_ZOOM_LEVELS, TOP_ZOOM_LEVEL+VIRTUAL_ZOOM_LEVELS, TILE_SIZE, false, false, "", "", "", "");
        setMinNonSuperTileZoomLevel(VIRTUAL_ZOOM_LEVELS);
        this.mode = mode;
    }

    /**
     * @return the name of the selected mode
     */
    public String getModeName()
    {
        return mode.name;
    }

    /**
     * @return the label of the selected mode
     */
    public String getModeLabel()
    {
        return mode.label;
    }

    @Override
    public String getTileUrl(final int x, final int y, final int zoom)
    {
        final String quad = tileToQuadKey(x, y, TOP_ZOOM_LEVEL - 0 - zoom +VIRTUAL_ZOOM_LEVELS);

        if(mode.newURL){
            return "http://ak.dynamic.t0.tiles.virtualearth.net/comp/ch/"+quad+"?mkt=en-US&it=G,L&"+mode.type+"&og=1555&n=z.png";
        }else {
            return "http://" + mode.type + quad.charAt(quad.length() - 1) +
                    ".ortho.tiles.virtualearth.net/tiles/"
                    + mode.type + quad + mode.ext + "?g=1";
        }
    }

    private String tileToQuadKey(final int tx, final int ty, final int zl)
    {
        String quad = "";

        for (int i = zl; i > 0; i--)
        {
            int mask = 1 << (i - 1);
            int cell = 0;

            if ((tx & mask) != 0)
            {
                cell++;
            }

            if ((ty & mask) != 0)
            {
                cell += 2;
            }

            quad += cell;
        }

        return quad;
    }

    @Override
    public String getAttribution() {
        return "\u00A9 Microsoft";
    }

    @Override
    public String getLicense() {
        return "https://www.microsoft.com/en-us/maps/licensing";
    }


}

