package jfx_sample1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jxmapviewer.jfx.JFXMapViewer;

public class Sample1 extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SubScene scene = new JFXMapViewer(1000, 1000);
        primaryStage.setScene(new Scene(new StackPane(scene), 1000, 1000));
        primaryStage.show();
    }
}
