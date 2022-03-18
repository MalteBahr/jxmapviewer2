package org.jxmapviewer.input;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import org.jxmapviewer.JXMapViewer;

/**
 * Used to pan using press and drag mouse gestures
 * @author joshy
 */
public class PanMouseInputListener extends MouseInputAdapter
{
    private Point prev;
    private JXMapViewer viewer;
    private Cursor priorCursor;

    private double dx = 0;
    private double dy = 0;

    private final double DECELERATION = 0.95;

    private Timer timer = null;

    /**
     * @param viewer the jxmapviewer
     */
    public PanMouseInputListener(JXMapViewer viewer)
    {
        this.viewer = viewer;
    }

    @Override
    public void mousePressed(MouseEvent evt)
    {
        if (!SwingUtilities.isLeftMouseButton(evt))
            return;
        if (!viewer.isPanningEnabled())
            return;
        if(timer != null)
            timer.stop();
        timer = null;
        dx = 0;
        dy = 0;
        prev = evt.getPoint();
        priorCursor = viewer.getCursor();
        viewer.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent evt)
    {
//        System.out.println("panemouselistener is consume: " + evt.isConsumed());

        if (!SwingUtilities.isLeftMouseButton(evt))
            return;
        if (!viewer.isPanningEnabled())
            return;
        if(evt.isConsumed())
            return;
        
        Point current = evt.getPoint();
        double x = viewer.getCenter().getX();
        double y = viewer.getCenter().getY();




        if(prev != null){
            double vx = prev.x - current.x;
            double vy = prev.y - current.y;
            double alpha = - viewer.getAngle();
            double vx2 = Math.cos(alpha)*vx - Math.sin(alpha)*vy;
            double vy2 = Math.sin(alpha)*vx + Math.cos(alpha)*vy;

            dx = vx2;
            dy = vy2;

            x+= vx2;
            y+= vy2;
        }




//        if(prev != null){
//                x += prev.x - current.x;
//                y += prev.y - current.y;
//        }

        y = validateHeight(y);

        prev = current;
        viewer.setCenter(new Point2D.Double(x, y));
//        System.out.println("repaint");
        evt.consume();
    }

    long lastTimeActionPerformed;

    @Override
    public void mouseReleased(MouseEvent evt)
    {
        if (!SwingUtilities.isLeftMouseButton(evt))
            return;

        final int milliseconds = 5;

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
//                System.out.println(Math.abs(dx));
                if(Math.abs(dx) <= 0.5 && Math.abs(dy) <= 0.5){
                    timer.stop();
                    timer = null;
                }
                double x = viewer.getCenter().getX() + dx;
                double y = viewer.getCenter().getY() + dy;
                y = validateHeight(y);
                viewer.setCenter(new Point2D.Double(x, y));
//                viewer.repaint();

                long dt = System.nanoTime() - lastTimeActionPerformed;
                dx *= Math.pow(DECELERATION, ( (dt * 1e-6) / milliseconds ));
                dy *= Math.pow(DECELERATION, ( (dt * 1e-6) / milliseconds ));

                lastTimeActionPerformed = System.nanoTime();
            }
        };

        if(timer != null)
            timer.stop();
        timer = new Timer(milliseconds, taskPerformer);
        lastTimeActionPerformed = System.nanoTime();
        timer.start();


        prev = null;
        viewer.setCursor(priorCursor);



    }



    private double validateHeight(double y){
        int maxHeight = (int) (viewer.getTileFactory().getMapSize(viewer.getZoom()).getHeight() * viewer
                .getTileFactory().getTileSize(viewer.getZoom()));
        if (y > maxHeight)
        {
            y = maxHeight;
        }
        return y;
    }
}
