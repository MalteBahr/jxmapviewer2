package sample10_featurePainter.sample7_swingwaypoints;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.geom.*;
import org.jxmapviewer.geom.Point;
import org.jxmapviewer.input.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.DefaultFeaturePainter;
import org.jxmapviewer.style.Style;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;
import sample3_interaction.SelectionAdapter;
import sample3_interaction.SelectionPainter;
import sample7_swingwaypoints.SwingWaypoint;
import sample7_swingwaypoints.SwingWaypointOverlayPainter;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A sample application demonstrating usage of Swing components as waypoints
 * using JXMapViewer.
 *
 * @author Daniel Stahr
 */
public class Sample7 {
    public static void main(String[] args) {
        // Create a TileFactoryInfo for OSM
        TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        final JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition frankfurt = new GeoPosition(50,  7, 0, 8, 41, 0);
        GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
        GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
        GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
        GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);

        // Set the focus
        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(frankfurt);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));


        Style style = new Style() {
            @Override
            public void applyStroke(Graphics2D graphics2D) {
                graphics2D.setPaint(Color.blue);
                graphics2D.setStroke(new BasicStroke(0));
            }

            @Override
            public void applyFill(Graphics2D graphics2D) {
                graphics2D.setPaint(Color.orange);
            }
            @Override
            public void applyPoints(Graphics2D graphics2D, List<Point2D> list) {
                int radius = 20;
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                        RenderingHints.VALUE_STROKE_PURE);
                graphics2D.setPaint(Color.orange);
                for(Point2D pt : list) {
                    graphics2D.translate( (int)(pt.getX()), (int)(pt.getY()));
                    Ellipse2D.Double circle = new Ellipse2D.Double( - radius,  - radius, 2 * radius, 2 * radius);
                    graphics2D.fill(circle);
                    graphics2D.translate(-(int)(pt.getX()),-(int)(pt.getY()));
                }
            }

            @Override
            public void applyStroke(Graphics2D g, float strokeWidth) {

            }
        };

        ArrayList<GeoPosition> line = new ArrayList<>();
        line.add(frankfurt);
        line.add(wiesbaden);

        // Create waypoints from the geo-positions
        Set<Feature<Geometry>> waypoints = new HashSet<>(Arrays.asList(
                new Feature<Geometry>(new Line(line),style),
                new Feature<Geometry>(new Point(mainz),style),
                new Feature<Geometry>(new Point(darmstadt),style),
                new Feature<Geometry>(new Point(offenbach),style)
                ));

        final Feature<Point> center = new Feature<Point>(new Point(new GeoPosition(53.,9.)),style);

        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
        Runnable runner = new Runnable() {
            @Override
            public void run() {
//                System.out.println("running");
                center.setGeom(new Point(mapViewer.getCenterPosition()));
                mapViewer.repaint();
            }
        };

        scheduledService.scheduleAtFixedRate(runner,0,100, TimeUnit.MILLISECONDS);

        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);



        // Set the overlay painter
        DefaultFeaturePainter featurePainter = new DefaultFeaturePainter();
        for(Feature<Geometry> pointFeature : waypoints){
            featurePainter.addFeature(pointFeature);
        }
        featurePainter.addFeature(center);


        mapViewer.setOverlayPainter(new CompoundPainter<JXMapViewer>(featurePainter,sp));

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 7");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
