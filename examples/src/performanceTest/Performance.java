package performanceTest;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.util.GeoUtil;

import javax.swing.*;

/**
 * A simple sample application that shows
 * a OSM map of Europe
 * @author Martin Steiger
 */
public class Performance
{
    /**
     * @param args the program args (ignored)
     */
    public static void main(String[] args)
    {
        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);


        // Set the focus
        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 1");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        long N = 100000;

        for(int j = 0; j < 10; j++) {
            long time = System.nanoTime();
            for (int i = 0; i < N; i++) {
                info.getMapCenterInPixelsAtZoom(3);
            }
            System.out.println("getmapcenter\t\t\t" + (System.nanoTime() - time));


            time = System.nanoTime();
            for (int i = 0; i < N; i++) {
                info.getLongitudeDegreeWidthInPixels(3);
            }
            System.out.println("longitudewidthinpixels\t" + (System.nanoTime() - time));

            time = System.nanoTime();
            double angle = (Math.PI / 180.0);
            for (int i = 0; i < N; i++) {
                double e = Math.sin((frankfurt.getLatitude()+Math.random()) * angle);
            }
            System.out.println("sinfunction\t\t\t\t" + (System.nanoTime() - time));

            time = System.nanoTime();
            for (int i = 0; i < N; i++) {
                info.getLongitudeRadianWidthInPixels(3);
            }
            System.out.println("longituderadianwidth\t" + (System.nanoTime() - time));

            time = System.nanoTime();
            for (int i = 0; i < N; i++) {
                double e = Math.log((1 + Math.random()) / (1 - Math.random())) * -1;
            }
            System.out.println("y\t\t\t\t\t\t" + (System.nanoTime() - time));

        }
    }
}
