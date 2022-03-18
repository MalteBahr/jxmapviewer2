package org.jxmapviewer.jfx;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Group;
import javafx.scene.shape.Shape3D;
import lombok.Data;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.Objects;

public class ShapeHandler {

    ObservableList<ShapeWrapper> shapes = FXCollections.observableArrayList();

    public void addShape(Shape3D shape3D, GeoPosition position) {
        addShape(shape3D,position,0);
    }


    public void addShape(Shape3D shape3D, GeoPosition position, double height) {
        shapes.add(new ShapeWrapper(shape3D, position, height));
    }

    public boolean remove(Shape3D shape3D) {
     return shapes.remove(new ShapeWrapper(shape3D));
    }


    @Data
    static class ShapeWrapper {
        private final Shape3D shape3D;
        private final Group group;
        private final GeoPosition position;
        private final double height;

        public ShapeWrapper(Shape3D shape3D) {
            this(shape3D, new GeoPosition(0, 0), 0);
        }

        public ShapeWrapper(Shape3D shape3D, GeoPosition position, double height) {
            this.shape3D = shape3D;
            this.group = new Group(shape3D);
            this.position = position;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ShapeWrapper)) return false;
            ShapeWrapper that = (ShapeWrapper) o;
            return shape3D.equals(that.shape3D);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shape3D);
        }
    }


    public ObservableList<ShapeWrapper> getShapes() {
        return shapes;
    }


    public void addInvalidationListener(InvalidationListener listener) {
        shapes.addListener(listener);
    }

    public void removeInvalidationListener(InvalidationListener listener) {
        shapes.removeListener(listener);
    }

}
