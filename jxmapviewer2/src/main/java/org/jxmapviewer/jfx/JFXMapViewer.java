package org.jxmapviewer.jfx;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.empty.EmptyTileFactory;

public class JFXMapViewer extends SubScene {

    private final Canvas canvas;
    private final Pane root;
    private final Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

    private DoubleProperty zoomLevel = new SimpleDoubleProperty(10);

    /**
     * The position, in <I>map coordinates</I> of the center point. This is defined as the distance from the top and
     * left edges of the map in pixels. Dragging the map component will change the center position. Zooming in/out will
     * cause the center to be recalculated so as to remain in the center of the new "map".
     */
    private ObjectProperty<Point2D> center = new SimpleObjectProperty<>(new Point2D(0, 0));

    /**
     * Factory used by this component to grab the tiles necessary for painting the map.
     */
    private ObjectProperty<TileFactory> factory = new SimpleObjectProperty<>();


    private final ShapeHandler shapeHandler = new ShapeHandler();


    public JFXMapViewer(double width, double height){
        super(new Pane(),width,height,true, SceneAntialiasing.BALANCED);

        factory.set(new EmptyTileFactory());

        Image image = new Image(JFXMapViewer.class.getResource("/images/test.png").toExternalForm());

        this.root = (Pane) getRoot();
        canvas = new Canvas(width,height);
        root.getChildren().add(canvas);



        AnimationTimer timer = new AnimationTimer() {
            int count = 0;
            @Override
            public void handle(long now) {
                if (count++ < 100) {
                    System.out.println(count);
                    canvas.getGraphicsContext2D().drawImage(image,0,0);
                }
//                canvas.setLayoutX(canvas.getLayoutX() +1 );

                update();
            }
        };

        timer.start();

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.getTransforms().addAll(new Translate(0, 20, -20), new Rotate(30,Rotate.X_AXIS), new Rotate(15,Rotate.Z_AXIS));
        this.setCamera(camera);

    }


    private void update(){
        shapeHandler.shapes.forEach((shapeWrapper -> {
            Point2D point2D = getFactory().geoToPixel(shapeWrapper.getPosition(), getZoomLevel());
            shapeWrapper.getGroup().setTranslateX(point2D.getX());
            shapeWrapper.getGroup().setTranslateY(point2D.getY());
            double scale = 1/getFactory().geoToResolution(shapeWrapper.getPosition(), getZoomLevel());

            shapeWrapper.getGroup().setTranslateZ(shapeWrapper.getHeight() * scale);
            shapeWrapper.getGroup().setScaleX(scale);
            shapeWrapper.getGroup().setScaleY(scale);
            shapeWrapper.getGroup().setScaleZ(scale);
        }));
    }


    public TileFactory getFactory() {
        return factory.get();
    }

    public ObjectProperty<TileFactory> factoryProperty() {
        return factory;
    }

    public void setFactory(TileFactory factory) {
        this.factory.set(factory);
    }

    public void setAngle(double angle) {
        zRotate.setAngle(Math.toDegrees(angle));
    }

    public  double getAngle() {
        return Math.toRadians(zRotate.getAngle());
    }

    public DoubleBinding angleProperty() {
        return angleProperty().multiply((Math.PI / 180d));
    }

    public Point2D getCenter() {
        return center.get();
    }

    public ObjectProperty<Point2D> centerProperty() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center.set(center);
    }

    public double getZoomLevel() {
        return zoomLevel.get();
    }

    public DoubleProperty zoomLevelProperty() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel.set(zoomLevel);
    }

    public Rectangle getViewportBounds() {
        return new Rectangle(0, 0, 1000, 1000);
    }
}
